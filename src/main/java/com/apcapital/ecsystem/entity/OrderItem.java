package com.apcapital.ecsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * OrderItem 是预存账户电商系统中的类。
 *
 * <p>该类型属于实体层，用于映射数据库表结构。
 */
@TableName("order_items")
public class OrderItem {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private Long productId;
    private String sku;
    private Integer quantity;
    private BigDecimal price;
    private LocalDateTime createdAt;

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
     * 返回 `getOrderId` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 通过 `setOrderId` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param orderId 本方法的输入参数
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 返回 `getProductId` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * 通过 `setProductId` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param productId 本方法的输入参数
     */
    public void setProductId(Long productId) {
        this.productId = productId;
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
}
