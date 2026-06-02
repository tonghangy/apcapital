package com.apcapital.ecsystem.exception;

/**
 * ErrorCode 是预存账户电商系统中的枚举。
 *
 * <p>该类型属于异常处理层，用于统一错误行为。
 */
public enum ErrorCode {
    INVALID_ARGUMENT("INVALID_ARGUMENT", "Invalid request argument"),
    USER_NOT_FOUND("USER_NOT_FOUND", "User not found"),
    MERCHANT_NOT_FOUND("MERCHANT_NOT_FOUND", "Merchant not found"),
    ACCOUNT_NOT_FOUND("ACCOUNT_NOT_FOUND", "Account not found"),
    PRODUCT_NOT_FOUND("PRODUCT_NOT_FOUND", "Product not found"),
    INSUFFICIENT_BALANCE("INSUFFICIENT_BALANCE", "Insufficient balance"),
    INSUFFICIENT_STOCK("INSUFFICIENT_STOCK", "Insufficient stock"),
    CONCURRENT_UPDATE("CONCURRENT_UPDATE", "Concurrent update detected"),
    LOCK_ACQUIRE_FAILED("LOCK_ACQUIRE_FAILED", "Failed to acquire distributed lock"),
    ORDER_NOT_FOUND("ORDER_NOT_FOUND", "Order not found"),
    INTERNAL_ERROR("INTERNAL_ERROR", "Internal server error");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 执行 `code` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public String code() {
        return code;
    }

    /**
     * 执行 `message` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public String message() {
        return message;
    }
}
