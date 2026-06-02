package com.apcapital.ecsystem.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.apcapital.ecsystem.entity.Merchant;
import com.apcapital.ecsystem.entity.Order;
import com.apcapital.ecsystem.entity.SettlementRecord;
import com.apcapital.ecsystem.mapper.MerchantMapper;
import com.apcapital.ecsystem.mapper.OrderMapper;
import com.apcapital.ecsystem.mapper.SettlementRecordMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * SettlementServiceImplTest 是预存账户电商系统中的类。
 *
 * <p>该类型属于服务实现层，用于编排核心业务事务。
 */
@ExtendWith(MockitoExtension.class)
class SettlementServiceImplTest {

    @Mock
    private OrderMapper orderMapper;
    @Mock
    private MerchantMapper merchantMapper;
    @Mock
    private SettlementRecordMapper settlementRecordMapper;

    private SettlementServiceImpl settlementService;

    @BeforeEach
    void setUp() {
        settlementService = new SettlementServiceImpl(orderMapper, merchantMapper, settlementRecordMapper);
    }

    @Test
    void settleForDate_shouldInsertMatchedRecord() {
        LocalDate date = LocalDate.of(2026, 6, 1);
        Order order = new Order();
        order.setMerchantId(1L);
        order.setTotalAmount(new BigDecimal("100.00"));
        order.setCreatedAt(LocalDateTime.of(2026, 6, 1, 10, 0));

        Merchant merchant = new Merchant();
        merchant.setId(1L);
        merchant.setBalance(new BigDecimal("100.00"));

        when(orderMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(List.of(order));
        when(merchantMapper.selectList(null)).thenReturn(List.of(merchant));

        settlementService.settleForDate(date);

        ArgumentCaptor<SettlementRecord> captor = ArgumentCaptor.forClass(SettlementRecord.class);
        verify(settlementRecordMapper).insert(captor.capture());
        SettlementRecord record = captor.getValue();
        assertEquals(1L, record.getMerchantId());
        assertEquals(new BigDecimal("100.00"), record.getTotalSalesAmount());
        assertEquals(Boolean.TRUE, record.getMatched());
    }

    @Test
    void listByMerchant_shouldDelegateToMapper() {
        when(settlementRecordMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(List.of());
        assertEquals(0, settlementService.listByMerchant(2L).size());
    }
}
