package com.apcapital.ecsystem.component;

import com.apcapital.ecsystem.service.SettlementService;
import java.time.LocalDate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * SettlementJob 是预存账户电商系统中的类。
 *
 * <p>该类型属于基础组件层，用于定时任务与消息处理。
 */
@Component
public class SettlementJob {

    private final SettlementService settlementService;

    /**
     * 创建实例并注入所需依赖。
     *
     * <p>该构造方法用于完成组件初始化，确保运行期可直接使用。
     * @param settlementService 本方法的输入参数
     */
    public SettlementJob(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    /**
     * 执行 `dailySettlement` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     */
    @Scheduled(cron = "${app.settlement.cron:0 0 2 * * *}")
    public void dailySettlement() {
        settlementService.settleForDate(LocalDate.now().minusDays(1));
    }
}
