package com.apcapital.ecsystem.util;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * RedisLockUtil 是预存账户电商系统中的类。
 *
 * <p>为下单、库存扣减等并发敏感场景提供分布式锁能力。
 */
@Component
public class RedisLockUtil {

    private static final String LOCK_PREFIX = "lock:";
    private static final Map<String, ReentrantLock> FALLBACK_LOCKS = new ConcurrentHashMap<>();

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 创建实例并注入所需依赖。
     *
     * <p>该构造方法用于完成组件初始化，确保运行期可直接使用。
     *
     * @param stringRedisTemplate Redis 字符串模板
     */
    public RedisLockUtil(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 尝试获取锁。
     *
     * <p>优先使用 Redis 原子加锁；当 Redis 不可用时退化为进程内锁，
     * 保障本地开发和降级场景仍可运行。
     *
     * @param key 业务锁键
     * @param ttl 锁超时时间
     * @return 是否成功加锁
     */
    public boolean tryLock(String key, Duration ttl) {
        String redisKey = LOCK_PREFIX + key;
        try {
            Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(redisKey, "1", ttl);
            return Boolean.TRUE.equals(success);
        } catch (Exception ex) {
            ReentrantLock lock = FALLBACK_LOCKS.computeIfAbsent(redisKey, k -> new ReentrantLock());
            try {
                return lock.tryLock(50, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
    }

    /**
     * 释放锁。
     *
     * <p>优先删除 Redis 锁；若 Redis 异常则尝试释放本地退化锁。
     *
     * @param key 业务锁键
     */
    public void unlock(String key) {
        String redisKey = LOCK_PREFIX + key;
        try {
            stringRedisTemplate.delete(redisKey);
            return;
        } catch (Exception ignored) {
            // Redis 不可用时，降级到本地锁释放逻辑
        }

        ReentrantLock lock = FALLBACK_LOCKS.get(redisKey);
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}
