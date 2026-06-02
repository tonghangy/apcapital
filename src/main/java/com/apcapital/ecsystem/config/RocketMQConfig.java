package com.apcapital.ecsystem.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * RocketMQConfig 是预存账户电商系统中的类。
 *
 * <p>该类型属于配置层，用于装配框架组件。
 */
@Configuration
@ConfigurationProperties(prefix = "app.rocketmq")
public class RocketMQConfig {

    private boolean enabled = false;
    private String orderCompletedTopic = "order-completed-topic";

    /**
     * 执行 `isEnabled` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 通过 `setEnabled` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param enabled 本方法的输入参数
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * 返回 `getOrderCompletedTopic` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public String getOrderCompletedTopic() {
        return orderCompletedTopic;
    }

    /**
     * 通过 `setOrderCompletedTopic` 操作更新状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param orderCompletedTopic 本方法的输入参数
     */
    public void setOrderCompletedTopic(String orderCompletedTopic) {
        this.orderCompletedTopic = orderCompletedTopic;
    }
}
