package com.apcapital.ecsystem.component;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * OrderMessageConsumer 是预存账户电商系统中的类。
 *
 * <p>该类型属于基础组件层，用于定时任务与消息处理。
 */
@Component
@ConditionalOnProperty(prefix = "app.rocketmq", name = "enabled", havingValue = "true")
@RocketMQMessageListener(consumerGroup = "ec-order-consumer-group", topic = "${app.rocketmq.order-completed-topic}")
public class OrderMessageConsumer implements RocketMQListener<String> {

    private static final Logger log = LoggerFactory.getLogger(OrderMessageConsumer.class);

    /**
     * 执行 `onMessage` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param orderNo 本方法的输入参数
     */
    @Override
    public void onMessage(String orderNo) {
        log.info("Received order completed message: {}", orderNo);
    }
}
