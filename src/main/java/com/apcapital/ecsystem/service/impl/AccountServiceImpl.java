package com.apcapital.ecsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.apcapital.ecsystem.dto.request.RechargeRequest;
import com.apcapital.ecsystem.dto.response.UserBalanceResponse;
import com.apcapital.ecsystem.entity.User;
import com.apcapital.ecsystem.entity.UserAccount;
import com.apcapital.ecsystem.exception.BusinessException;
import com.apcapital.ecsystem.exception.ErrorCode;
import com.apcapital.ecsystem.mapper.UserAccountMapper;
import com.apcapital.ecsystem.mapper.UserMapper;
import com.apcapital.ecsystem.service.AccountService;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AccountServiceImpl 是预存账户电商系统中的类。
 *
 * <p>该类型属于服务实现层，用于编排核心业务事务。
 */
@Service
public class AccountServiceImpl implements AccountService {

    private final UserMapper userMapper;
    private final UserAccountMapper userAccountMapper;

    /**
     * 创建实例并注入所需依赖。
     *
     * <p>该构造方法用于完成组件初始化，确保运行期可直接使用。
     * @param userMapper 本方法的输入参数
     * @param userAccountMapper 本方法的输入参数
     */
    public AccountServiceImpl(UserMapper userMapper, UserAccountMapper userAccountMapper) {
        this.userMapper = userMapper;
        this.userAccountMapper = userAccountMapper;
    }

    /**
     * 执行 `recharge` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param userId 本方法的输入参数
     * @param request 本方法的输入参数
     * @return 本方法的返回结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserBalanceResponse recharge(Long userId, RechargeRequest request) {
        ensureUserExists(userId);
        UserAccount account = getAccount(userId);
        account.setBalance(account.getBalance().add(request.amount()));
        if (userAccountMapper.updateById(account) != 1) {
            throw new BusinessException(ErrorCode.CONCURRENT_UPDATE, "account recharge conflict");
        }
        return new UserBalanceResponse(userId, account.getBalance());
    }

    /**
     * 返回 `getBalance` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param userId 本方法的输入参数
     * @return 本方法的返回结果
     */
    @Override
    public UserBalanceResponse getBalance(Long userId) {
        ensureUserExists(userId);
        UserAccount account = getAccount(userId);
        return new UserBalanceResponse(userId, account.getBalance());
    }

    /**
     * 执行 `debitBalance` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param userId 本方法的输入参数
     * @param amount 本方法的输入参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void debitBalance(Long userId, BigDecimal amount) {
        ensureUserExists(userId);
        UserAccount account = getAccount(userId);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE);
        }
        account.setBalance(account.getBalance().subtract(amount));
        if (userAccountMapper.updateById(account) != 1) {
            throw new BusinessException(ErrorCode.CONCURRENT_UPDATE, "account debit conflict");
        }
    }

    /**
     * 执行 `ensureUserExists` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param userId 本方法的输入参数
     */
    private void ensureUserExists(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
    }

    /**
     * 返回 `getAccount` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param userId 本方法的输入参数
     * @return 本方法的返回结果
     */
    private UserAccount getAccount(Long userId) {
        UserAccount account = userAccountMapper.selectOne(
            new LambdaQueryWrapper<UserAccount>().eq(UserAccount::getUserId, userId)
        );
        if (account == null) {
            throw new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        return account;
    }
}
