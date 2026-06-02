package com.apcapital.ecsystem.dto.response;

/**
 * 用户注册结果响应。
 *
 * <p>用于在用户完成注册后返回用户主键和名称。
 */
public record UserResponse(Long id, String name) {
}
