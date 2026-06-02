package com.apcapital.ecsystem.exception;

/**
 * BusinessException 是预存账户电商系统中的类。
 *
 * <p>该类型属于异常处理层，用于统一错误行为。
 */
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    /**
     * 创建实例并注入所需依赖。
     *
     * <p>该构造方法用于完成组件初始化，确保运行期可直接使用。
     * @param errorCode 本方法的输入参数
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.message());
        this.errorCode = errorCode;
    }

    /**
     * 创建实例并注入所需依赖。
     *
     * <p>该构造方法用于完成组件初始化，确保运行期可直接使用。
     * @param errorCode 本方法的输入参数
     * @param message 本方法的输入参数
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * 返回 `getErrorCode` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
