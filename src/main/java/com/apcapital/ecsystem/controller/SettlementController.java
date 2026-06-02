package com.apcapital.ecsystem.controller;

import com.apcapital.ecsystem.dto.response.ApiResponse;
import com.apcapital.ecsystem.entity.SettlementRecord;
import com.apcapital.ecsystem.service.SettlementService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * SettlementController 是预存账户电商系统中的类。
 *
 * <p>提供结算域接口，支持手动触发日结和查询商家结算记录。
 */
@RestController
@RequestMapping("/api/v1/settlements")
public class SettlementController {

    private final SettlementService settlementService;

    /**
     * 创建实例并注入所需依赖。
     *
     * <p>该构造方法用于完成组件初始化，确保运行期可直接使用。
     *
     * @param settlementService 结算领域服务
     */
    public SettlementController(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    /**
     * 手动触发指定日期的结算任务。
     *
     * <p>用于补跑结算或人工校验场景，和定时任务能力互补。
     *
     * @param date 待结算的业务日期
     * @return 空响应（成功/失败通过统一响应体表示）
     */
    @PostMapping("/run")
    public ApiResponse<Void> runSettlement(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        settlementService.settleForDate(date);
        return ApiResponse.success(null);
    }

    /**
     * 查询商家结算记录列表。
     *
     * @param merchantId 商家主键
     * @return 结算记录集合
     */
    @GetMapping("/merchants/{merchantId}")
    public ApiResponse<List<SettlementRecord>> listByMerchant(@PathVariable Long merchantId) {
        return ApiResponse.success(settlementService.listByMerchant(merchantId));
    }
}
