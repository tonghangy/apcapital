package com.apcapital.ecsystem.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.apcapital.ecsystem.dto.request.RechargeRequest;
import com.apcapital.ecsystem.dto.response.UserBalanceResponse;
import com.apcapital.ecsystem.entity.User;
import com.apcapital.ecsystem.entity.UserAccount;
import com.apcapital.ecsystem.exception.BusinessException;
import com.apcapital.ecsystem.mapper.UserAccountMapper;
import com.apcapital.ecsystem.mapper.UserMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * AccountServiceImplTest 是预存账户电商系统中的类。
 *
 * <p>该类型属于服务实现层，用于编排核心业务事务。
 */
@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private UserAccountMapper userAccountMapper;

    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl(userMapper, userAccountMapper);
    }

    @Test
    void recharge_shouldIncreaseBalance() {
        User user = new User();
        user.setId(1L);
        UserAccount account = new UserAccount();
        account.setUserId(1L);
        account.setBalance(new BigDecimal("100.00"));

        when(userMapper.selectById(1L)).thenReturn(user);
        when(userAccountMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(account);
        when(userAccountMapper.updateById(account)).thenReturn(1);

        UserBalanceResponse response = accountService.recharge(1L, new RechargeRequest(new BigDecimal("20.00")));
        assertEquals(new BigDecimal("120.00"), response.balance());
    }

    @Test
    void debitBalance_shouldThrowWhenInsufficient() {
        User user = new User();
        user.setId(1L);
        UserAccount account = new UserAccount();
        account.setUserId(1L);
        account.setBalance(new BigDecimal("10.00"));

        when(userMapper.selectById(1L)).thenReturn(user);
        when(userAccountMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(account);

        assertThrows(BusinessException.class, () -> accountService.debitBalance(1L, new BigDecimal("20.00")));
    }

    @Test
    void getBalance_shouldReturnCurrentValue() {
        User user = new User();
        user.setId(3L);
        UserAccount account = new UserAccount();
        account.setUserId(3L);
        account.setBalance(new BigDecimal("45.50"));

        when(userMapper.selectById(3L)).thenReturn(user);
        when(userAccountMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(account);

        UserBalanceResponse response = accountService.getBalance(3L);
        assertEquals(new BigDecimal("45.50"), response.balance());
    }
}
