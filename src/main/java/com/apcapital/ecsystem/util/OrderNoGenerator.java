package com.apcapital.ecsystem.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

/**
 * OrderNoGenerator 是预存账户电商系统中的类。
 *
 * <p>该类型属于工具层，用于提供可复用辅助能力。
 */
@Component
public class OrderNoGenerator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 执行 `generate` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @return 本方法的返回结果
     */
    public String generate() {
        int suffix = ThreadLocalRandom.current().nextInt(1000, 10000);
        return "ORD" + LocalDateTime.now().format(FORMATTER) + suffix;
    }
}
