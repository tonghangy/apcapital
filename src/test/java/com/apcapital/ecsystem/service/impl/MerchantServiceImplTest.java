package com.apcapital.ecsystem.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import com.apcapital.ecsystem.dto.request.MerchantRegisterRequest;
import com.apcapital.ecsystem.dto.response.MerchantBalanceResponse;
import com.apcapital.ecsystem.entity.Merchant;
import com.apcapital.ecsystem.exception.BusinessException;
import com.apcapital.ecsystem.mapper.MerchantMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * MerchantServiceImplTest 是预存账户电商系统中的类。
 *
 * <p>该类型属于服务实现层，用于编排核心业务事务。
 */
@ExtendWith(MockitoExtension.class)
class MerchantServiceImplTest {

    @Mock
    private MerchantMapper merchantMapper;

    private MerchantServiceImpl merchantService;

    @BeforeEach
    void setUp() {
        merchantService = new MerchantServiceImpl(merchantMapper);
    }

    @Test
    void register_shouldCreateMerchant() {
        doAnswer(invocation -> {
            Merchant merchant = invocation.getArgument(0);
            merchant.setId(1L);
            return 1;
        }).when(merchantMapper).insert(any(Merchant.class));

        assertEquals(1L, merchantService.register(new MerchantRegisterRequest("Shop")).id());
    }

    @Test
    void getBalance_shouldThrowWhenMissing() {
        when(merchantMapper.selectById(9L)).thenReturn(null);
        assertThrows(BusinessException.class, () -> merchantService.getBalance(9L));
    }

    @Test
    void creditBalance_shouldUpdateAmount() {
        Merchant merchant = new Merchant();
        merchant.setId(2L);
        merchant.setBalance(new BigDecimal("10.00"));
        merchant.setVersion(0);
        when(merchantMapper.selectById(2L)).thenReturn(merchant);
        when(merchantMapper.updateById(merchant)).thenReturn(1);

        merchantService.creditBalance(2L, new BigDecimal("5.00"));

        MerchantBalanceResponse response = merchantService.getBalance(2L);
        assertEquals(new BigDecimal("15.00"), response.balance());
    }
}
