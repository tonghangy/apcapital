package com.apcapital.ecsystem.dto.response;

/**
 * ApiResponse 是预存账户电商系统中的记录类型。
 *
 * <p>该类型属于响应 DTO 层，用于描述 API 输出结果。
 */
public record ApiResponse<T>(boolean success, String code, String message, T data) {

    /**
     * 执行 `success` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param data 本方法的输入参数
     * @return 本方法的返回结果
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "OK", "success", data);
    }

    /**
     * 执行 `failed` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param code 本方法的输入参数
     * @param message 本方法的输入参数
     * @return 本方法的返回结果
     */
    public static <T> ApiResponse<T> failed(String code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }
}
