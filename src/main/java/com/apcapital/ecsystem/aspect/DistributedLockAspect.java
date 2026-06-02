package com.apcapital.ecsystem.aspect;

import com.apcapital.ecsystem.exception.BusinessException;
import com.apcapital.ecsystem.exception.ErrorCode;
import com.apcapital.ecsystem.util.RedisLockUtil;
import java.time.Duration;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * DistributedLockAspect 是预存账户电商系统中的类。
 *
 * <p>该类型属于 AOP 层，用于处理横切关注点。
 */
@Aspect
@Component
public class DistributedLockAspect {

    private final RedisLockUtil redisLockUtil;

    /**
     * 创建实例并注入所需依赖。
     *
     * <p>该构造方法用于完成组件初始化，确保运行期可直接使用。
     * @param redisLockUtil 本方法的输入参数
     */
    public DistributedLockAspect(RedisLockUtil redisLockUtil) {
        this.redisLockUtil = redisLockUtil;
    }

    /**
     * 执行 `around` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param joinPoint 本方法的输入参数
     * @param distributedLock 本方法的输入参数
     * @return 本方法的返回结果
     */
    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String lockKey = distributedLock.key();
        boolean locked = redisLockUtil.tryLock(lockKey, Duration.ofSeconds(distributedLock.ttlSeconds()));
        if (!locked) {
            throw new BusinessException(ErrorCode.LOCK_ACQUIRE_FAILED);
        }
        try {
            return joinPoint.proceed();
        } finally {
            redisLockUtil.unlock(lockKey);
        }
    }
}
