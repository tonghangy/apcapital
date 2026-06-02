package com.apcapital.ecsystem.exception;

import com.apcapital.ecsystem.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandler 是预存账户电商系统中的类。
 *
 * <p>该类型属于异常处理层，用于统一错误行为。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常（可预期异常）。
     *
     * <p>在日志中输出请求信息和业务定位，方便快速定位到具体业务类和行号。
     *
     * @param request 当前 HTTP 请求
     * @param ex 业务异常
     * @return 统一错误响应（HTTP 400）
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(HttpServletRequest request, BusinessException ex) {
        Optional<StackTraceElement> businessFrame = findBusinessFrame(ex);
        if (businessFrame.isPresent()) {
            StackTraceElement frame = businessFrame.get();
            log.warn(
                "业务异常: method={} uri={} code={} message={} location={}.{}:{}",
                request.getMethod(),
                request.getRequestURI(),
                ex.getErrorCode().code(),
                ex.getMessage(),
                frame.getClassName(),
                frame.getMethodName(),
                frame.getLineNumber()
            );
        } else {
            log.warn(
                "业务异常: method={} uri={} code={} message={}",
                request.getMethod(),
                request.getRequestURI(),
                ex.getErrorCode().code(),
                ex.getMessage()
            );
        }
        return ResponseEntity.badRequest()
            .body(ApiResponse.failed(ex.getErrorCode().code(), ex.getMessage()));
    }

    /**
     * 处理参数校验异常。
     *
     * @param request 当前 HTTP 请求
     * @param ex 参数校验异常
     * @return 统一错误响应（HTTP 400）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(HttpServletRequest request, MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
            .orElse("Validation failed");
        log.warn("参数校验失败: method={} uri={} message={}", request.getMethod(), request.getRequestURI(), message);
        return ResponseEntity.badRequest().body(ApiResponse.failed(ErrorCode.INVALID_ARGUMENT.code(), message));
    }

    /**
     * 处理兜底异常（不可预期异常）。
     *
     * <p>会完整打印异常栈，并额外输出第一条业务代码定位信息。
     *
     * @param request 当前 HTTP 请求
     * @param ex 未捕获异常
     * @return 统一错误响应（HTTP 500）
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(HttpServletRequest request, Exception ex) {
        Optional<StackTraceElement> businessFrame = findBusinessFrame(ex);
        if (businessFrame.isPresent()) {
            StackTraceElement frame = businessFrame.get();
            log.error(
                "系统异常: method={} uri={} location={}.{}:{}",
                request.getMethod(),
                request.getRequestURI(),
                frame.getClassName(),
                frame.getMethodName(),
                frame.getLineNumber(),
                ex
            );
        } else {
            log.error("系统异常: method={} uri={}", request.getMethod(), request.getRequestURI(), ex);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.failed(ErrorCode.INTERNAL_ERROR.code(), ex.getMessage()));
    }

    /**
     * 从异常栈中提取第一条业务代码定位信息。
     *
     * @param ex 异常对象
     * @return 业务栈帧（若存在）
     */
    private Optional<StackTraceElement> findBusinessFrame(Throwable ex) {
        return Arrays.stream(ex.getStackTrace())
            .filter(frame -> frame.getClassName().startsWith("com.apcapital.ecsystem"))
            .findFirst();
    }
}
