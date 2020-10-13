package org.pzy.opensource.redis.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.redisson.api.RLock;

/**
 * Redisson锁信息
 *
 * @author pan
 * @since 2020-10-13
 */
@Data
@Accessors(chain = true)
public class RedissonLockInfoBO {
    private RLock lock;
    private Boolean hasLock;

    public RedissonLockInfoBO() {
        this.hasLock = false;
    }

    /**
     * 释放锁. 只有当hasLock为true时,才会真正执行释放动作
     */
    public void unlock() {
        if (hasLock) {
            lock.unlock();
        }
    }
}
