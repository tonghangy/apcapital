package com.apcapital.ecsystem.dto.response;

import java.math.BigDecimal;

/**
 * 商家余额查询响应。
 *
 * <p>用于商家查看当前账户累计入账金额（来自用户下单支付）。
 */
public record MerchantBalanceResponse(Long merchantId, BigDecimal balance) {
}
