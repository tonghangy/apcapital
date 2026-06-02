package com.apcapital.ecsystem.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * RechargeRequest 是预存账户电商系统中的记录类型。
 *
 * <p>该类型属于请求 DTO 层，用于描述 API 输入参数。
 */
public record RechargeRequest(
    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.01", message = "amount must be positive")
    BigDecimal amount
) {
}
