package com.apcapital.ecsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * RedisConfig 是预存账户电商系统中的类。
 *
 * <p>该类型属于配置层，用于装配框架组件。
 */
@Configuration
public class RedisConfig {

    /**
     * 执行 `stringRedisTemplate` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param connectionFactory 本方法的输入参数
     * @return 本方法的返回结果
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}
