package com.apcapital.ecsystem.service.impl;

import com.apcapital.ecsystem.dto.request.MerchantRegisterRequest;
import com.apcapital.ecsystem.dto.response.MerchantBalanceResponse;
import com.apcapital.ecsystem.dto.response.MerchantResponse;
import com.apcapital.ecsystem.entity.Merchant;
import com.apcapital.ecsystem.exception.BusinessException;
import com.apcapital.ecsystem.exception.ErrorCode;
import com.apcapital.ecsystem.mapper.MerchantMapper;
import com.apcapital.ecsystem.service.MerchantService;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * MerchantServiceImpl 是预存账户电商系统中的类。
 *
 * <p>该类型属于服务实现层，用于编排核心业务事务。
 */
@Service
public class MerchantServiceImpl implements MerchantService {

    private final MerchantMapper merchantMapper;

    /**
     * 创建实例并注入所需依赖。
     *
     * <p>该构造方法用于完成组件初始化，确保运行期可直接使用。
     * @param merchantMapper 本方法的输入参数
     */
    public MerchantServiceImpl(MerchantMapper merchantMapper) {
        this.merchantMapper = merchantMapper;
    }

    /**
     * 执行 `register` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param request 本方法的输入参数
     * @return 本方法的返回结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MerchantResponse register(MerchantRegisterRequest request) {
        Merchant merchant = new Merchant();
        merchant.setName(request.name());
        merchant.setBalance(BigDecimal.ZERO);
        merchant.setVersion(0);
        merchantMapper.insert(merchant);
        return new MerchantResponse(merchant.getId(), merchant.getName(), merchant.getBalance());
    }

    /**
     * 返回 `getBalance` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param merchantId 本方法的输入参数
     * @return 本方法的返回结果
     */
    @Override
    public MerchantBalanceResponse getBalance(Long merchantId) {
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new BusinessException(ErrorCode.MERCHANT_NOT_FOUND);
        }
        return new MerchantBalanceResponse(merchantId, merchant.getBalance());
    }

    /**
     * 执行 `creditBalance` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param merchantId 本方法的输入参数
     * @param amount 本方法的输入参数
     */
    @Override
    public void creditBalance(Long merchantId, BigDecimal amount) {
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new BusinessException(ErrorCode.MERCHANT_NOT_FOUND);
        }
        merchant.setBalance(merchant.getBalance().add(amount));
        int updated = merchantMapper.updateById(merchant);
        if (updated != 1) {
            throw new BusinessException(ErrorCode.CONCURRENT_UPDATE, "merchant balance update conflict");
        }
    }
}
