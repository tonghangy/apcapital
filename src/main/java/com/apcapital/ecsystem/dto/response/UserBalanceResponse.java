package com.apcapital.ecsystem.dto.response;

import java.math.BigDecimal;

/**
 * 用户余额查询响应。
 *
 * <p>用于在充值、下单扣款后向调用方返回用户当前可用余额。
 */
public record UserBalanceResponse(Long userId, BigDecimal balance) {
}
