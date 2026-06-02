package com.apcapital.ecsystem.dto.response;

/**
 * 库存变更结果响应。
 *
 * <p>用于商家补货后返回商品标识、SKU 以及最新库存数量。
 */
public record InventoryResponse(Long productId, String sku, Integer quantity) {
}
