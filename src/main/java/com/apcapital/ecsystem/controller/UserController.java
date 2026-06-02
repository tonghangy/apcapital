package com.apcapital.ecsystem.controller;

import com.apcapital.ecsystem.dto.request.RechargeRequest;
import com.apcapital.ecsystem.dto.request.UserRegisterRequest;
import com.apcapital.ecsystem.dto.response.ApiResponse;
import com.apcapital.ecsystem.dto.response.OrderResponse;
import com.apcapital.ecsystem.dto.response.UserBalanceResponse;
import com.apcapital.ecsystem.dto.response.UserResponse;
import com.apcapital.ecsystem.service.AccountService;
import com.apcapital.ecsystem.service.OrderService;
import com.apcapital.ecsystem.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController 是预存账户电商系统中的类。
 *
 * <p>提供用户侧接口：注册、充值、余额查询、订单查询。
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final AccountService accountService;
    private final OrderService orderService;

    /**
     * 创建实例并注入所需依赖。
     *
     * <p>该构造方法用于完成组件初始化，确保运行期可直接使用。
     * @param userService 本方法的输入参数
     * @param accountService 本方法的输入参数
     * @param orderService 本方法的输入参数
     */
    public UserController(UserService userService, AccountService accountService, OrderService orderService) {
        this.userService = userService;
        this.accountService = accountService;
        this.orderService = orderService;
    }

    /**
     * 用户注册入口。
     *
     * <p>注册成功后会同步初始化一条预存账户记录，余额默认为 0。
     *
     * @param request 注册请求
     * @return 注册结果
     */
    @PostMapping
    public ApiResponse<UserResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        return ApiResponse.success(userService.register(request));
    }

    /**
     * 查询用户预存账户余额。
     *
     * @param userId 用户主键
     * @return 用户余额结果
     */
    @GetMapping("/{userId}/balance")
    public ApiResponse<UserBalanceResponse> getBalance(@PathVariable Long userId) {
        return ApiResponse.success(accountService.getBalance(userId));
    }

    /**
     * 用户充值入口。
     *
     * <p>充值金额会直接累加到用户预存账户，可用于后续下单支付。
     *
     * @param userId 用户主键
     * @param request 充值请求
     * @return 充值后的余额结果
     */
    @PostMapping("/{userId}/recharge")
    public ApiResponse<UserBalanceResponse> recharge(@PathVariable Long userId, @Valid @RequestBody RechargeRequest request) {
        return ApiResponse.success(accountService.recharge(userId, request));
    }

    /**
     * 查询用户侧订单列表。
     *
     * <p>用于个人中心展示该用户发起的历史订单。
     *
     * @param userId 用户主键
     * @return 订单列表
     */
    @GetMapping("/{userId}/orders")
    public ApiResponse<List<OrderResponse>> listOrders(@PathVariable Long userId) {
        return ApiResponse.success(orderService.listByUser(userId));
    }
}
