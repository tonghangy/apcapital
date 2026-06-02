package com.apcapital.ecsystem.controller;

import com.apcapital.ecsystem.dto.request.AddInventoryRequest;
import com.apcapital.ecsystem.dto.request.MerchantRegisterRequest;
import com.apcapital.ecsystem.dto.response.ApiResponse;
import com.apcapital.ecsystem.dto.response.InventoryResponse;
import com.apcapital.ecsystem.dto.response.MerchantBalanceResponse;
import com.apcapital.ecsystem.dto.response.MerchantResponse;
import com.apcapital.ecsystem.dto.response.OrderResponse;
import com.apcapital.ecsystem.service.InventoryService;
import com.apcapital.ecsystem.service.MerchantService;
import com.apcapital.ecsystem.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MerchantController 是预存账户电商系统中的类。
 *
 * <p>提供商家侧接口：商家注册、补货、余额查询、订单查询。
 */
@RestController
@RequestMapping("/api/v1/merchants")
public class MerchantController {

    private final MerchantService merchantService;
    private final InventoryService inventoryService;
    private final OrderService orderService;

    /**
     * 创建实例并注入所需依赖。
     *
     * <p>该构造方法用于完成组件初始化，确保运行期可直接使用。
     *
     * @param merchantService 商家领域服务
     * @param inventoryService 库存领域服务
     * @param orderService 订单查询服务
     */
    public MerchantController(
        MerchantService merchantService,
        InventoryService inventoryService,
        OrderService orderService
    ) {
        this.merchantService = merchantService;
        this.inventoryService = inventoryService;
        this.orderService = orderService;
    }

    /**
     * 商家注册入口。
     *
     * <p>注册完成后会生成商家账户，初始余额为 0。
     *
     * @param request 注册请求
     * @return 商家注册结果
     */
    @PostMapping
    public ApiResponse<MerchantResponse> register(@Valid @RequestBody MerchantRegisterRequest request) {
        return ApiResponse.success(merchantService.register(request));
    }

    /**
     * 查询商家账户余额。
     *
     * @param merchantId 商家主键
     * @return 商家余额结果
     */
    @GetMapping("/{merchantId}/balance")
    public ApiResponse<MerchantBalanceResponse> getBalance(@PathVariable Long merchantId) {
        return ApiResponse.success(merchantService.getBalance(merchantId));
    }

    /**
     * 商家补货入口。
     *
     * <p>若 SKU 首次出现则创建商品；若已存在则在当前库存基础上累加数量。
     *
     * @param merchantId 商家主键
     * @param request 补货请求
     * @return 库存更新结果
     */
    @PostMapping("/{merchantId}/inventory")
    public ApiResponse<InventoryResponse> addInventory(
        @PathVariable Long merchantId,
        @Valid @RequestBody AddInventoryRequest request
    ) {
        return ApiResponse.success(inventoryService.addInventory(merchantId, request));
    }

    /**
     * 查询商家侧订单列表。
     *
     * <p>用于商家后台查看本店铺成交订单。
     *
     * @param merchantId 商家主键
     * @return 订单列表
     */
    @GetMapping("/{merchantId}/orders")
    public ApiResponse<List<OrderResponse>> listOrders(@PathVariable Long merchantId) {
        return ApiResponse.success(orderService.listByMerchant(merchantId));
    }
}
