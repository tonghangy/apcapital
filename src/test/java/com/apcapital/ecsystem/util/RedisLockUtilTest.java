package com.apcapital.ecsystem.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * RedisLockUtilTest 是预存账户电商系统中的类。
 *
 * <p>该类型属于工具层，用于提供可复用辅助能力。
 */
@ExtendWith(MockitoExtension.class)
class RedisLockUtilTest {

    @Mock
    private StringRedisTemplate redisTemplate;
    @Mock
    private ValueOperations<String, String> valueOperations;

    private RedisLockUtil redisLockUtil;

    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        redisLockUtil = new RedisLockUtil(redisTemplate);
    }

    @Test
    void tryLock_shouldReturnTrueWhenRedisSuccess() {
        when(valueOperations.setIfAbsent(eq("lock:order-1"), eq("1"), any(Duration.class))).thenReturn(true);
        assertTrue(redisLockUtil.tryLock("order-1", Duration.ofSeconds(3)));
    }

    @Test
    void tryLock_shouldFallbackWhenRedisFails() {
        when(valueOperations.setIfAbsent(eq("lock:fallback"), eq("1"), any(Duration.class)))
            .thenThrow(new RuntimeException("redis down"));

        assertTrue(redisLockUtil.tryLock("fallback", Duration.ofSeconds(3)));
        redisLockUtil.unlock("fallback");
    }

    @Test
    void tryLock_shouldReturnFalseWhenRedisDenied() {
        when(valueOperations.setIfAbsent(eq("lock:busy"), eq("1"), any(Duration.class))).thenReturn(false);
        assertFalse(redisLockUtil.tryLock("busy", Duration.ofSeconds(3)));
    }

    @Test
    void unlock_shouldDeleteRedisKey() {
        redisLockUtil.unlock("order-2");
        verify(redisTemplate).delete("lock:order-2");
    }
}
