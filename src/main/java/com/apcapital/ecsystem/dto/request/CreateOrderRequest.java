package com.apcapital.ecsystem.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * CreateOrderRequest 是预存账户电商系统中的记录类型。
 *
 * <p>该类型属于请求 DTO 层，用于描述 API 输入参数。
 */
public record CreateOrderRequest(
    @NotNull(message = "userId is required") Long userId,
    @NotNull(message = "merchantId is required") Long merchantId,
    @NotEmpty(message = "items cannot be empty") List<@Valid OrderItemRequest> items
) {

    /**
     * OrderItemRequest 是预存账户电商系统中的记录类型。
     *
     * <p>该类型属于请求 DTO 层，用于描述 API 输入参数。
     */
    public record OrderItemRequest(
        @NotBlank(message = "sku cannot be blank") String sku,
        @NotNull(message = "quantity is required")
        @Min(value = 1, message = "quantity must be at least 1")
        Integer quantity
    ) {
    }
}
