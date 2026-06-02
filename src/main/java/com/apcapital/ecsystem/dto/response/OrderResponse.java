package com.apcapital.ecsystem.dto.response;

import java.math.BigDecimal;

/**
 * 订单创建/查询响应。
 *
 * <p>用于返回订单号、支付总金额和订单状态，供前端展示下单结果。
 */
public record OrderResponse(Long orderId, String orderNo, BigDecimal totalAmount, String status) {
}
