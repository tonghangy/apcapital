package com.apcapital.ecsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.apcapital.ecsystem.entity.Merchant;
import com.apcapital.ecsystem.entity.Order;
import com.apcapital.ecsystem.entity.SettlementRecord;
import com.apcapital.ecsystem.mapper.MerchantMapper;
import com.apcapital.ecsystem.mapper.OrderMapper;
import com.apcapital.ecsystem.mapper.SettlementRecordMapper;
import com.apcapital.ecsystem.service.SettlementService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * SettlementServiceImpl 是预存账户电商系统中的类。
 *
 * <p>该类型属于服务实现层，用于编排核心业务事务。
 */
@Service
public class SettlementServiceImpl implements SettlementService {

    private final OrderMapper orderMapper;
    private final MerchantMapper merchantMapper;
    private final SettlementRecordMapper settlementRecordMapper;

    /**
     * 创建实例并注入所需依赖。
     *
     * <p>该构造方法用于完成组件初始化，确保运行期可直接使用。
     * @param orderMapper 本方法的输入参数
     * @param merchantMapper 本方法的输入参数
     * @param settlementRecordMapper 本方法的输入参数
     */
    public SettlementServiceImpl(
        OrderMapper orderMapper,
        MerchantMapper merchantMapper,
        SettlementRecordMapper settlementRecordMapper
    ) {
        this.orderMapper = orderMapper;
        this.merchantMapper = merchantMapper;
        this.settlementRecordMapper = settlementRecordMapper;
    }

    /**
     * 通过 `settleForDate` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param settlementDate 本方法的输入参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void settleForDate(LocalDate settlementDate) {
        LocalDateTime start = settlementDate.atStartOfDay();
        LocalDateTime end = LocalDateTime.of(settlementDate, LocalTime.MAX);

        List<Order> dailyOrders = orderMapper.selectList(
            new LambdaQueryWrapper<Order>()
                .ge(Order::getCreatedAt, start)
                .le(Order::getCreatedAt, end)
        );

        Map<Long, BigDecimal> merchantSalesMap = dailyOrders.stream()
            .collect(Collectors.groupingBy(
                Order::getMerchantId,
                Collectors.reducing(BigDecimal.ZERO, Order::getTotalAmount, BigDecimal::add)
            ));

        List<Merchant> merchants = merchantMapper.selectList(null);
        for (Merchant merchant : merchants) {
            BigDecimal totalSales = merchantSalesMap.getOrDefault(merchant.getId(), BigDecimal.ZERO);
            BigDecimal balance = merchant.getBalance() == null ? BigDecimal.ZERO : merchant.getBalance();
            BigDecimal difference = balance.subtract(totalSales);

            SettlementRecord record = new SettlementRecord();
            record.setMerchantId(merchant.getId());
            record.setSettlementDate(settlementDate);
            record.setTotalSalesAmount(totalSales);
            record.setMerchantBalance(balance);
            record.setDifference(difference);
            record.setMatched(difference.compareTo(BigDecimal.ZERO) == 0);
            settlementRecordMapper.insert(record);
        }
    }

    /**
     * 通过 `listByMerchant` 查询列出业务数据。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param merchantId 本方法的输入参数
     * @return 本方法的返回结果
     */
    @Override
    public List<SettlementRecord> listByMerchant(Long merchantId) {
        return settlementRecordMapper.selectList(
            new LambdaQueryWrapper<SettlementRecord>()
                .eq(SettlementRecord::getMerchantId, merchantId)
                .orderByDesc(SettlementRecord::getSettlementDate)
        );
    }
}
