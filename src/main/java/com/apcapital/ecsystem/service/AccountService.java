package com.apcapital.ecsystem.service;

import com.apcapital.ecsystem.dto.request.RechargeRequest;
import com.apcapital.ecsystem.dto.response.UserBalanceResponse;
import java.math.BigDecimal;

/**
 * AccountService 是预存账户电商系统中的接口。
 *
 * <p>负责用户预存资金账户能力，覆盖充值、余额查询、交易扣款三类场景。
 */
public interface AccountService {

    /**
     * 向用户预存账户充值。
     *
     * @param userId 用户主键
     * @param request 充值请求（金额）
     * @return 充值后的最新余额
     */
    UserBalanceResponse recharge(Long userId, RechargeRequest request);

    /**
     * 查询用户当前可用余额。
     *
     * @param userId 用户主键
     * @return 当前余额快照
     */
    UserBalanceResponse getBalance(Long userId);

    /**
     * 在下单交易中扣减用户预存余额。
     *
     * @param userId 用户主键
     * @param amount 需要扣减的交易金额
     */
    void debitBalance(Long userId, BigDecimal amount);
}
