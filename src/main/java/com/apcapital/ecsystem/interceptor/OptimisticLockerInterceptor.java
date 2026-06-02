package com.apcapital.ecsystem.interceptor;

import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OptimisticLockerInterceptor 是预存账户电商系统中的类。
 *
 * <p>该类型属于拦截器层，用于扩展框架能力。
 */
@Configuration
public class OptimisticLockerInterceptor {

    /**
     * 执行 `optimisticLockerInnerInterceptor` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    @Bean
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }
}
