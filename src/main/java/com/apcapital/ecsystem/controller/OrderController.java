package com.apcapital.ecsystem.controller;

import com.apcapital.ecsystem.dto.request.CreateOrderRequest;
import com.apcapital.ecsystem.dto.response.ApiResponse;
import com.apcapital.ecsystem.dto.response.OrderResponse;
import com.apcapital.ecsystem.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OrderController 是预存账户电商系统中的类。
 *
 * <p>提供订单域对外接口，核心是“创建订单”和“查询订单”。
 */
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * 创建实例并注入所需依赖。
     *
     * <p>该构造方法用于完成组件初始化，确保运行期可直接使用。
     *
     * @param orderService 订单领域服务
     */
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 创建订单入口。
     *
     * <p>该接口会触发完整交易链路：锁控制、余额扣减、商家入账、库存扣减、
     * 订单落库与消息通知。
     *
     * @param request 下单请求
     * @return 下单结果
     */
    @PostMapping
    public ApiResponse<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        return ApiResponse.success(orderService.createOrder(request));
    }

    /**
     * 查询单笔订单详情。
     *
     * @param orderId 订单主键
     * @return 订单摘要
     */
    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponse> getById(@PathVariable Long orderId) {
        return ApiResponse.success(orderService.getOrder(orderId));
    }
}
