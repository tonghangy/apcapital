package com.apcapital.ecsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Order 是预存账户电商系统中的类。
 *
 * <p>该类型属于实体层，用于映射数据库表结构。
 */
@TableName("orders")
public class Order {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long merchantId;
    private String orderNo;
    private BigDecimal totalAmount;
    private String status;
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
     * 返回 `getUserId` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 通过 `setUserId` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param userId 本方法的输入参数
     */
    public void setUserId(Long userId) {
        this.userId = userId;
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
     * 返回 `getOrderNo` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 通过 `setOrderNo` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param orderNo 本方法的输入参数
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 返回 `getTotalAmount` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * 通过 `setTotalAmount` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param totalAmount 本方法的输入参数
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * 返回 `getStatus` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public String getStatus() {
        return status;
    }

    /**
     * 通过 `setStatus` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param status 本方法的输入参数
     */
    public void setStatus(String status) {
        this.status = status;
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
