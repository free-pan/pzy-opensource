package org.pzy.opensource.redis.support.util;

import lombok.extern.slf4j.Slf4j;
import org.pzy.opensource.redis.domain.bo.RedissonLockInfoBO;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static org.pzy.opensource.redis.support.springboot.aop.WinterLockAop.LOCK_TYPE_NAME;

/**
 * Redisson分布式锁帮助类
 *
 * @author pan
 * @since 2020-10-13
 */
@Slf4j
public class RedissonLockHelper {

    private RedissonClient redissonClient;

    public RedissonLockHelper(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        if (log.isDebugEnabled()) {
            log.debug("{}已加入spring容器,可使用该组件进行编程级别的锁控制!", RedissonLockHelper.class.getName());
        }
    }

    /**
     * 最大可能获取锁(如果没有获取到会产生一定的等待和重试)
     *
     * @param key       锁使用的key
     * @param waitTime  最长等待时间.单位:秒
     * @param leaseTime 锁的有效时间.单位:秒
     * @return
     */
    public RedissonLockInfoBO getLock(String key, Long waitTime, Long leaseTime) {
        String lockKey = buildLockKey(key);
        RLock lock = redissonClient.getLock(lockKey);
        boolean hasLock = false;
        try {
            hasLock = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("锁[" + lockKey + "]获取异常!", e);
            if (hasLock) {
                lock.unlock();
            }
        }
        return new RedissonLockInfoBO().setHasLock(hasLock).setLock(lock);
    }

    /**
     * 最大可能获取锁(如果没有获取到会产生一定的等待和重试)
     *
     * @param keyColl   锁使用的key集合
     * @param waitTime  最长等待时间.单位:秒
     * @param leaseTime 锁的有效时间.单位:秒
     * @return
     */
    public RedissonLockInfoBO getLock(Collection<String> keyColl, Long waitTime, Long leaseTime) {
        RLock[] lockArr = buildLockKey(keyColl);

        boolean hasLock = false;
        RLock lock = redissonClient.getMultiLock(lockArr);
        try {
            hasLock = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("锁获取异常!", e);
            if (hasLock) {
                lock.unlock();
            }
        }
        return new RedissonLockInfoBO().setHasLock(hasLock).setLock(lock);
    }

    private RLock[] buildLockKey(Collection<String> keyColl) {
        RLock[] lockArr = new RLock[keyColl.size()];

        int i = 0;
        for (String key : keyColl) {
            lockArr[i] = redissonClient.getLock(LOCK_TYPE_NAME + key);
            ++i;
        }
        return lockArr;
    }

    /**
     * 获取锁
     *
     * @param key 锁使用的key
     * @return
     */
    public RedissonLockInfoBO getLock(String key) {
        String lockKey = buildLockKey(key);
        RLock lock = redissonClient.getLock(lockKey);
        boolean hasLock = lock.tryLock();
        return new RedissonLockInfoBO().setHasLock(hasLock).setLock(lock);
    }

    /**
     * 获取锁
     *
     * @param keyColl 锁使用的key集合
     * @return
     */
    public RedissonLockInfoBO getLock(Collection<String> keyColl) {
        RLock[] lockArr = buildLockKey(keyColl);
        RLock lock = redissonClient.getMultiLock(lockArr);
        boolean hasLock = lock.tryLock();
        return new RedissonLockInfoBO().setHasLock(hasLock).setLock(lock);
    }

    /**
     * 释放锁
     *
     * @param lockInfoBO
     */
    public void unlock(RedissonLockInfoBO lockInfoBO) {
        if (null != lockInfoBO) {
            lockInfoBO.unlock();
        }
    }

    private String buildLockKey(String key) {
        return LOCK_TYPE_NAME + key;
    }
}
