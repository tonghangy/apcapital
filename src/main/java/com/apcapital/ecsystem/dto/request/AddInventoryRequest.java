package com.apcapital.ecsystem.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * AddInventoryRequest 是预存账户电商系统中的记录类型。
 *
 * <p>该类型属于请求 DTO 层，用于描述 API 输入参数。
 */
public record AddInventoryRequest(
    @NotBlank(message = "sku cannot be blank") String sku,
    @NotBlank(message = "name cannot be blank") String name,
    @NotNull(message = "price is required")
    @DecimalMin(value = "0.01", message = "price must be positive")
    BigDecimal price,
    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "quantity must be at least 1")
    Integer quantity
) {
}
