package com.apcapital.ecsystem.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.apcapital.ecsystem.config.RocketMQConfig;
import com.apcapital.ecsystem.dto.request.CreateOrderRequest;
import com.apcapital.ecsystem.dto.request.CreateOrderRequest.OrderItemRequest;
import com.apcapital.ecsystem.dto.response.OrderResponse;
import com.apcapital.ecsystem.entity.Order;
import com.apcapital.ecsystem.entity.Product;
import com.apcapital.ecsystem.exception.BusinessException;
import com.apcapital.ecsystem.mapper.OrderItemMapper;
import com.apcapital.ecsystem.mapper.OrderMapper;
import com.apcapital.ecsystem.service.AccountService;
import com.apcapital.ecsystem.service.InventoryService;
import com.apcapital.ecsystem.service.MerchantService;
import com.apcapital.ecsystem.util.OrderNoGenerator;
import com.apcapital.ecsystem.util.RedisLockUtil;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;

/**
 * OrderServiceImplTest 是预存账户电商系统中的类。
 *
 * <p>该类型属于服务实现层，用于编排核心业务事务。
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private AccountService accountService;
    @Mock
    private MerchantService merchantService;
    @Mock
    private InventoryService inventoryService;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private OrderNoGenerator orderNoGenerator;
    @Mock
    private RedisLockUtil redisLockUtil;
    @Mock
    private ObjectProvider<RocketMQTemplate> rocketProvider;

    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        RocketMQConfig config = new RocketMQConfig();
        config.setEnabled(false);
        orderService = new OrderServiceImpl(
            accountService,
            merchantService,
            inventoryService,
            orderMapper,
            orderItemMapper,
            orderNoGenerator,
            redisLockUtil,
            rocketProvider,
            config
        );
    }

    @Test
    void createOrder_shouldCompleteMainFlow() {
        Product product = new Product();
        product.setId(9L);
        product.setMerchantId(2L);
        product.setSku("SKU001");
        product.setPrice(new BigDecimal("20.00"));
        product.setQuantity(10);

        when(redisLockUtil.tryLock(anyString(), any(Duration.class))).thenReturn(true);
        when(inventoryService.getByMerchantAndSku(2L, "SKU001")).thenReturn(product);
        when(orderNoGenerator.generate()).thenReturn("ORDTEST0001");
        doAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L);
            return 1;
        }).when(orderMapper).insert(any(Order.class));

        CreateOrderRequest request = new CreateOrderRequest(1L, 2L, List.of(new OrderItemRequest("SKU001", 2)));
        OrderResponse response = orderService.createOrder(request);

        assertEquals(1L, response.orderId());
        assertEquals(new BigDecimal("40.00"), response.totalAmount());
        verify(accountService).debitBalance(1L, new BigDecimal("40.00"));
        verify(merchantService).creditBalance(2L, new BigDecimal("40.00"));
        verify(inventoryService).deductStock(product, 2);
    }

    @Test
    void createOrder_shouldThrowWhenLockFails() {
        when(redisLockUtil.tryLock(anyString(), any(Duration.class))).thenReturn(false);
        CreateOrderRequest request = new CreateOrderRequest(1L, 2L, List.of(new OrderItemRequest("SKU001", 1)));
        assertThrows(BusinessException.class, () -> orderService.createOrder(request));
    }

    @Test
    void getOrder_shouldThrowWhenMissing() {
        when(orderMapper.selectById(99L)).thenReturn(null);
        assertThrows(BusinessException.class, () -> orderService.getOrder(99L));
    }

    @Test
    void listByUser_shouldMapResponses() {
        Order order = new Order();
        order.setId(7L);
        order.setOrderNo("ORD7");
        order.setTotalAmount(new BigDecimal("33.00"));
        order.setStatus("COMPLETED");

        when(orderMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(List.of(order));
        assertEquals(1, orderService.listByUser(1L).size());
    }
}
