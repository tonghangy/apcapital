package com.apcapital.ecsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * SettlementRecord 是预存账户电商系统中的类。
 *
 * <p>该类型属于实体层，用于映射数据库表结构。
 */
@TableName("settlement_records")
public class SettlementRecord {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long merchantId;
    private LocalDate settlementDate;
    private BigDecimal totalSalesAmount;
    private BigDecimal merchantBalance;
    private Boolean isMatched;
    private BigDecimal difference;
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
     * 返回 `getSettlementDate` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    /**
     * 通过 `setSettlementDate` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param settlementDate 本方法的输入参数
     */
    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }

    /**
     * 返回 `getTotalSalesAmount` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public BigDecimal getTotalSalesAmount() {
        return totalSalesAmount;
    }

    /**
     * 通过 `setTotalSalesAmount` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param totalSalesAmount 本方法的输入参数
     */
    public void setTotalSalesAmount(BigDecimal totalSalesAmount) {
        this.totalSalesAmount = totalSalesAmount;
    }

    /**
     * 返回 `getMerchantBalance` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public BigDecimal getMerchantBalance() {
        return merchantBalance;
    }

    /**
     * 通过 `setMerchantBalance` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param merchantBalance 本方法的输入参数
     */
    public void setMerchantBalance(BigDecimal merchantBalance) {
        this.merchantBalance = merchantBalance;
    }

    /**
     * 返回 `getMatched` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public Boolean getMatched() {
        return isMatched;
    }

    /**
     * 通过 `setMatched` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param matched 本方法的输入参数
     */
    public void setMatched(Boolean matched) {
        isMatched = matched;
    }

    /**
     * 返回 `getDifference` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public BigDecimal getDifference() {
        return difference;
    }

    /**
     * 通过 `setDifference` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param difference 本方法的输入参数
     */
    public void setDifference(BigDecimal difference) {
        this.difference = difference;
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
