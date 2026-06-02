package com.apcapital.ecsystem.service;

import com.apcapital.ecsystem.dto.request.CreateOrderRequest;
import com.apcapital.ecsystem.dto.response.OrderResponse;
import java.util.List;

/**
 * OrderService 是预存账户电商系统中的接口。
 *
 * <p>面向交易域提供订单能力，负责承接下单、订单查询、用户侧订单列表、
 * 商家侧订单列表等核心用例。
 */
public interface OrderService {

    /**
     * 创建订单并驱动完整交易流程。
     *
     * <p>该流程会串行完成：库存校验、金额计算、用户扣款、商家入账、
     * 库存扣减、订单及明细落库，并返回最终下单结果。
     *
     * @param request 下单请求，包含用户、商家、SKU 与购买数量
     * @return 下单完成后的订单摘要（订单号、总金额、状态）
     */
    OrderResponse createOrder(CreateOrderRequest request);

    /**
     * 按订单主键查询订单摘要信息。
     *
     * @param orderId 订单主键
     * @return 订单摘要
     */
    OrderResponse getOrder(Long orderId);

    /**
     * 查询指定用户的历史订单列表。
     *
     * @param userId 用户主键
     * @return 该用户可见的订单摘要集合
     */
    List<OrderResponse> listByUser(Long userId);

    /**
     * 查询指定商家的历史订单列表。
     *
     * @param merchantId 商家主键
     * @return 该商家关联的订单摘要集合
     */
    List<OrderResponse> listByMerchant(Long merchantId);
}
