package com.apcapital.ecsystem.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 用户注册请求参数。
 *
 * <p>用于接收用户注册时提交的基础信息。
 */
public record UserRegisterRequest(
    @NotBlank(message = "name cannot be blank")
    String name
) {
}
