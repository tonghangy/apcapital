package com.apcapital.ecsystem.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlusConfig 是预存账户电商系统中的类。
 *
 * <p>该类型属于配置层，用于装配框架组件。
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 执行 `mybatisPlusInterceptor` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param optimisticLockerInnerInterceptor 本方法的输入参数
     * @return 本方法的返回结果
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor);
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }
}
