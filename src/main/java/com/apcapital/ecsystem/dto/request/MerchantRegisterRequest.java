package com.apcapital.ecsystem.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 商家注册请求参数。
 *
 * <p>用于接收商家注册时提交的名称信息。
 */
public record MerchantRegisterRequest(
    @NotBlank(message = "name cannot be blank")
    String name
) {
}
