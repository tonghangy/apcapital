package com.apcapital.ecsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.apcapital.ecsystem.config.RocketMQConfig;
import com.apcapital.ecsystem.dto.request.CreateOrderRequest;
import com.apcapital.ecsystem.dto.request.CreateOrderRequest.OrderItemRequest;
import com.apcapital.ecsystem.dto.response.OrderResponse;
import com.apcapital.ecsystem.entity.Order;
import com.apcapital.ecsystem.entity.OrderItem;
import com.apcapital.ecsystem.entity.Product;
import com.apcapital.ecsystem.exception.BusinessException;
import com.apcapital.ecsystem.exception.ErrorCode;
import com.apcapital.ecsystem.mapper.OrderItemMapper;
import com.apcapital.ecsystem.mapper.OrderMapper;
import com.apcapital.ecsystem.service.AccountService;
import com.apcapital.ecsystem.service.InventoryService;
import com.apcapital.ecsystem.service.MerchantService;
import com.apcapital.ecsystem.service.OrderService;
import com.apcapital.ecsystem.util.OrderNoGenerator;
import com.apcapital.ecsystem.util.RedisLockUtil;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * OrderServiceImpl 是预存账户电商系统中的类。
 *
 * <p>该类型属于服务实现层，用于编排核心业务事务。
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final AccountService accountService;
    private final MerchantService merchantService;
    private final InventoryService inventoryService;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderNoGenerator orderNoGenerator;
    private final RedisLockUtil redisLockUtil;
    private final ObjectProvider<RocketMQTemplate> rocketMQTemplateProvider;
    private final RocketMQConfig rocketMQConfig;

    /**
     * 创建实例并注入所需依赖。
     *
     * <p>该构造方法用于完成组件初始化，确保运行期可直接使用。
     * @param accountService 本方法的输入参数
     * @param merchantService 本方法的输入参数
     * @param inventoryService 本方法的输入参数
     * @param orderMapper 本方法的输入参数
     * @param orderItemMapper 本方法的输入参数
     * @param orderNoGenerator 本方法的输入参数
     * @param redisLockUtil 本方法的输入参数
     * @param rocketMQTemplateProvider 本方法的输入参数
     * @param rocketMQConfig 本方法的输入参数
     */
    public OrderServiceImpl(
        AccountService accountService,
        MerchantService merchantService,
        InventoryService inventoryService,
        OrderMapper orderMapper,
        OrderItemMapper orderItemMapper,
        OrderNoGenerator orderNoGenerator,
        RedisLockUtil redisLockUtil,
        ObjectProvider<RocketMQTemplate> rocketMQTemplateProvider,
        RocketMQConfig rocketMQConfig
    ) {
        this.accountService = accountService;
        this.merchantService = merchantService;
        this.inventoryService = inventoryService;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.orderNoGenerator = orderNoGenerator;
        this.redisLockUtil = redisLockUtil;
        this.rocketMQTemplateProvider = rocketMQTemplateProvider;
        this.rocketMQConfig = rocketMQConfig;
    }

    /**
     * 创建订单并执行完整交易链路。
     *
     * <p>典型场景：用户在商家店铺选择 SKU 下单。系统会在同一事务中完成
     * 用户扣款、商家入账、库存扣减、订单落库，并在成功后发送订单完成消息。
     *
     * @param request 下单请求
     * @return 订单创建结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderResponse createOrder(CreateOrderRequest request) {
        // 第 1 步：基于用户和 SKU 构造稳定锁键，确保同一用户同一批商品不会并发下单。
        List<String> lockKeys = buildLockKeys(request);
        tryAcquireLocks(lockKeys);
        try {
            // 第 2 步：按 SKU 加载商品快照（包含价格和可用库存）。
            Map<String, Product> products = request.items().stream()
                .collect(Collectors.toMap(
                    OrderItemRequest::sku,
                    item -> inventoryService.getByMerchantAndSku(request.merchantId(), item.sku()),
                    (a, b) -> a
                ));

            // 第 3 步：逐行计算数量 * 单价，汇总订单总金额。
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (OrderItemRequest item : request.items()) {
                Product product = products.get(item.sku());
                BigDecimal lineAmount = product.getPrice().multiply(BigDecimal.valueOf(item.quantity()));
                totalAmount = totalAmount.add(lineAmount);
            }

            // 第 4 步：同一事务内完成资金流转：用户预存账户扣款、商家账户入账。
            accountService.debitBalance(request.userId(), totalAmount);
            merchantService.creditBalance(request.merchantId(), totalAmount);

            // 第 5 步：支付成功后扣减库存。
            for (OrderItemRequest item : request.items()) {
                inventoryService.deductStock(products.get(item.sku()), item.quantity());
            }

            // 第 6 步：落库订单主记录。
            Order order = new Order();
            order.setUserId(request.userId());
            order.setMerchantId(request.merchantId());
            order.setOrderNo(orderNoGenerator.generate());
            order.setTotalAmount(totalAmount);
            order.setStatus("COMPLETED");
            orderMapper.insert(order);

            // 第 7 步：落库订单明细，供审计和结算使用。
            for (OrderItemRequest item : request.items()) {
                Product product = products.get(item.sku());
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(order.getId());
                orderItem.setProductId(product.getId());
                orderItem.setSku(product.getSku());
                orderItem.setQuantity(item.quantity());
                orderItem.setPrice(product.getPrice());
                orderItemMapper.insert(orderItem);
            }

            // 第 8 步：如果启用 RocketMQ，发送订单完成事件。
            sendOrderCompletedEvent(order);
            return new OrderResponse(order.getId(), order.getOrderNo(), totalAmount, order.getStatus());
        } finally {
            // 第 9 步：无论成功或失败都释放锁，避免锁泄漏。
            releaseLocks(lockKeys);
        }
    }

    /**
     * 查询单笔订单。
     *
     * @param orderId 订单主键
     * @return 订单摘要
     */
    @Override
    public OrderResponse getOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        return toResponse(order);
    }

    /**
     * 查询用户历史订单列表。
     *
     * @param userId 用户主键
     * @return 用户订单列表
     */
    @Override
    public List<OrderResponse> listByUser(Long userId) {
        return orderMapper.selectList(new LambdaQueryWrapper<Order>().eq(Order::getUserId, userId))
            .stream()
            .map(this::toResponse)
            .toList();
    }

    /**
     * 查询商家历史订单列表。
     *
     * @param merchantId 商家主键
     * @return 商家订单列表
     */
    @Override
    public List<OrderResponse> listByMerchant(Long merchantId) {
        return orderMapper.selectList(new LambdaQueryWrapper<Order>().eq(Order::getMerchantId, merchantId))
            .stream()
            .map(this::toResponse)
            .toList();
    }

    /**
     * 将订单实体转换为对外响应对象。
     *
     * @param order 订单实体
     * @return 订单响应对象
     */
    private OrderResponse toResponse(Order order) {
        return new OrderResponse(order.getId(), order.getOrderNo(), order.getTotalAmount(), order.getStatus());
    }

    /**
     * 构建本次下单需要获取的锁键集合。
     *
     * <p>包含用户维度锁 + 商品维度锁，用于限制并发下单冲突。
     *
     * @param request 下单请求
     * @return 锁键列表
     */
    private List<String> buildLockKeys(CreateOrderRequest request) {
        List<String> keys = new ArrayList<>();
        keys.add("user:" + request.userId());
        keys.addAll(request.items().stream()
            .map(item -> "product:" + request.merchantId() + ":" + item.sku())
            .distinct()
            .sorted(Comparator.naturalOrder())
            .toList());
        return keys;
    }

    /**
     * 顺序尝试获取全部分布式锁。
     *
     * @param lockKeys 待获取的锁键列表
     */
    private void tryAcquireLocks(List<String> lockKeys) {
        for (String key : lockKeys) {
            boolean locked = redisLockUtil.tryLock(key, Duration.ofSeconds(5));
            if (!locked) {
                throw new BusinessException(ErrorCode.LOCK_ACQUIRE_FAILED, "failed to acquire lock: " + key);
            }
        }
    }

    /**
     * 释放已申请的分布式锁。
     *
     * @param lockKeys 待释放的锁键列表
     */
    private void releaseLocks(List<String> lockKeys) {
        for (String key : lockKeys) {
            redisLockUtil.unlock(key);
        }
    }

    /**
     * 发送订单完成事件。
     *
     * <p>用于后续异步扩展（如积分、通知、报表等），未开启 MQ 时直接跳过。
     *
     * @param order 已完成订单
     */
    private void sendOrderCompletedEvent(Order order) {
        if (!rocketMQConfig.isEnabled()) {
            return;
        }
        RocketMQTemplate template = rocketMQTemplateProvider.getIfAvailable();
        if (template != null) {
            template.convertAndSend(rocketMQConfig.getOrderCompletedTopic(), order.getOrderNo());
        }
    }
}
