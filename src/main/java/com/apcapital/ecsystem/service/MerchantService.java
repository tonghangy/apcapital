package com.apcapital.ecsystem.service;

import com.apcapital.ecsystem.dto.request.MerchantRegisterRequest;
import com.apcapital.ecsystem.dto.response.MerchantBalanceResponse;
import com.apcapital.ecsystem.dto.response.MerchantResponse;
import java.math.BigDecimal;

/**
 * MerchantService 是预存账户电商系统中的接口。
 *
 * <p>负责商家主体和商家资金账户能力，覆盖商家注册、余额查询、交易入账。
 */
public interface MerchantService {

    /**
     * 注册商家主体并初始化商家资金账户。
     *
     * @param request 商家注册请求
     * @return 注册成功后的商家信息
     */
    MerchantResponse register(MerchantRegisterRequest request);

    /**
     * 查询商家当前账户余额。
     *
     * @param merchantId 商家主键
     * @return 商家余额快照
     */
    MerchantBalanceResponse getBalance(Long merchantId);

    /**
     * 在用户支付成功后为商家账户入账。
     *
     * @param merchantId 商家主键
     * @param amount 需要入账的金额
     */
    void creditBalance(Long merchantId, BigDecimal amount);
}
