package com.apcapital.ecsystem.service;

import com.apcapital.ecsystem.entity.SettlementRecord;
import java.time.LocalDate;
import java.util.List;

/**
 * SettlementService 是预存账户电商系统中的接口。
 *
 * <p>负责商家日结对账能力，核心目标是验证“订单销售额”和“商家账户余额”一致性。
 */
public interface SettlementService {

    /**
     * 按业务日期执行结算。
     *
     * <p>通常由定时任务在每天凌晨触发，也支持手动触发补跑。
     *
     * @param settlementDate 结算业务日期
     */
    void settleForDate(LocalDate settlementDate);

    /**
     * 查询指定商家的结算记录。
     *
     * @param merchantId 商家主键
     * @return 结算记录列表
     */
    List<SettlementRecord> listByMerchant(Long merchantId);
}
