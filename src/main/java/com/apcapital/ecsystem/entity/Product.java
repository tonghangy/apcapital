package com.apcapital.ecsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Product 是预存账户电商系统中的类。
 *
 * <p>该类型属于实体层，用于映射数据库表结构。
 */
@TableName("products")
public class Product {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long merchantId;
    private String sku;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    @Version
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 返回 `getId` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public Long getId() {
        return id;
    }

    /**
     * 通过 `setId` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param id 本方法的输入参数
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 返回 `getMerchantId` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public Long getMerchantId() {
        return merchantId;
    }

    /**
     * 通过 `setMerchantId` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param merchantId 本方法的输入参数
     */
    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    /**
     * 返回 `getSku` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public String getSku() {
        return sku;
    }

    /**
     * 通过 `setSku` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param sku 本方法的输入参数
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * 返回 `getName` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public String getName() {
        return name;
    }

    /**
     * 通过 `setName` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param name 本方法的输入参数
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 返回 `getPrice` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 通过 `setPrice` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param price 本方法的输入参数
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 返回 `getQuantity` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 通过 `setQuantity` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param quantity 本方法的输入参数
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * 返回 `getVersion` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * 通过 `setVersion` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param version 本方法的输入参数
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * 返回 `getCreatedAt` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 通过 `setCreatedAt` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param createdAt 本方法的输入参数
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 返回 `getUpdatedAt` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 通过 `setUpdatedAt` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param updatedAt 本方法的输入参数
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
