package com.apcapital.ecsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * EcSystemApplication 是预存账户电商系统中的类。
 *
 * <p>该类型用于支撑系统核心能力。
 */
@SpringBootApplication(scanBasePackages = "com.apcapital.ecsystem")
@MapperScan("com.apcapital.ecsystem.mapper")
@EnableScheduling
@EnableFeignClients
public class EcSystemApplication {

    /**
     * 执行 `main` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param args 本方法的输入参数
     */
    public static void main(String[] args) {
        SpringApplication.run(EcSystemApplication.class, args);
    }
}
