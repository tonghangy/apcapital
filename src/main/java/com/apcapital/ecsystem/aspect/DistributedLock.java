package com.apcapital.ecsystem.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * DistributedLock 是预存账户电商系统中的注解。
 *
 * <p>该类型属于 AOP 层，用于处理横切关注点。
 */
public @interface DistributedLock {

    /**
     * 业务锁键。
     *
     * <p>同一锁键在同一时刻仅允许一个线程进入目标方法，
     * 用于避免并发下单导致的余额或库存超扣。
     *
     * @return 锁键字符串
     */
    String key();

    /**
     * 锁超时时间（秒）。
     *
     * <p>用于防止异常场景下锁长期不释放。
     *
     * @return 锁过期秒数
     */
    long ttlSeconds() default 5L;
}
