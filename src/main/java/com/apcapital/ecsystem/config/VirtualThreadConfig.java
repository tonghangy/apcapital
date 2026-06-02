package com.apcapital.ecsystem.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * VirtualThreadConfig 是预存账户电商系统中的类。
 *
 * <p>该类型属于配置层，用于装配框架组件。
 */
@Configuration
public class VirtualThreadConfig {

    /**
     * 执行 `virtualThreadExecutor` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    @Bean(destroyMethod = "close")
    public ExecutorService virtualThreadExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
