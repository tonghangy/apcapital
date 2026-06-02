package com.apcapital.ecsystem.service;

import com.apcapital.ecsystem.dto.request.AddInventoryRequest;
import com.apcapital.ecsystem.dto.response.InventoryResponse;
import com.apcapital.ecsystem.entity.Product;

/**
 * InventoryService 是预存账户电商系统中的接口。
 *
 * <p>负责商家商品库存能力，覆盖补货、按 SKU 查品、下单扣减库存。
 */
public interface InventoryService {

    /**
     * 商家补货：若 SKU 不存在则创建，存在则累加库存并更新价格信息。
     *
     * @param merchantId 商家主键
     * @param request 补货请求（SKU、名称、单价、补货数量）
     * @return 补货后的库存快照
     */
    InventoryResponse addInventory(Long merchantId, AddInventoryRequest request);

    /**
     * 按商家与 SKU 查询商品库存实体。
     *
     * <p>实现层可优先命中缓存，未命中再回源数据库。
     *
     * @param merchantId 商家主键
     * @param sku 商品 SKU
     * @return 商品库存实体
     */
    Product getByMerchantAndSku(Long merchantId, String sku);

    /**
     * 在交易流程中扣减库存。
     *
     * @param product 目标商品实体
     * @param quantity 需扣减数量
     */
    void deductStock(Product product, Integer quantity);
}
