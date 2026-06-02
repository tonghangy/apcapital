package com.apcapital.ecsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Merchant 是预存账户电商系统中的类。
 *
 * <p>该类型属于实体层，用于映射数据库表结构。
 */
@TableName("merchants")
public class Merchant {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private BigDecimal balance;
    @Version
    private Integer version;
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
     * 返回 `getBalance` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * 通过 `setBalance` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param balance 本方法的输入参数
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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
}
