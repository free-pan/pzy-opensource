/*
 * Copyright (c) [2019] [潘志勇]
 *    [pzy-opensource] is licensed under the Mulan PSL v1.
 *    You can use this software according to the terms and conditions of the Mulan PSL v1.
 *    You may obtain a copy of Mulan PSL v1 at:
 *       http://license.coscl.org.cn/MulanPSL
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *    PURPOSE.
 *    See the Mulan PSL v1 for more details.
 */

package org.pzy.opensource.redis.support.util;

import org.pzy.opensource.domain.GlobalConstant;
import org.pzy.opensource.spring.util.ResourceFileLoadUtil;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁(适用情况:同类操作之间锁可以共享,不同类操作锁需要互斥)<br>
 * 如:<br>
 * 新增操作之间可以并发进行, 但如果正在进行新增,则不允许进行删除操作
 *
 * @author pan
 * @date 2019-03-31
 */
public class RedisDistributedLockAdvanceUtil {

    /**
     * 释放redis锁的lua脚本
     */
    private static String RELEASE_LOCK_SCRIPT = null;
    private static String TRY_LOCK_SCRIPT = null;

    static {
        String scriptFile = "classpath:script/ReleaseLock.lua";
        try {
            RELEASE_LOCK_SCRIPT = ResourceFileLoadUtil.loadAsString(scriptFile, GlobalConstant.DEFAULT_CHARSET);
        } catch (IOException e) {
            throw new RuntimeException(String.format("lua脚本文件[%s]读取异常!", scriptFile));
        }
        scriptFile = "classpath:script/TryAdvanceLock.lua";
        try {
            TRY_LOCK_SCRIPT = ResourceFileLoadUtil.loadAsString(scriptFile, GlobalConstant.DEFAULT_CHARSET);
        } catch (IOException e) {
            throw new RuntimeException(String.format("lua脚本文件[%s]读取异常!", scriptFile));
        }
    }

    private static RedisTemplate REDIS_TEMPLATE_VALUE_JSON_STRING;

    private RedisDistributedLockAdvanceUtil() {
    }

    public static RedisDistributedLockAdvanceUtil INSTANCE;

    public static RedisDistributedLockAdvanceUtil newInstance(RedisTemplate<String, Object> redisTemplate) {
        if (null == REDIS_TEMPLATE_VALUE_JSON_STRING) {
            synchronized (RedisDistributedLockAdvanceUtil.class) {
                if (null == REDIS_TEMPLATE_VALUE_JSON_STRING) {
                    REDIS_TEMPLATE_VALUE_JSON_STRING = redisTemplate;
                    INSTANCE = new RedisDistributedLockAdvanceUtil();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * 尝试获取锁
     *
     * @param lockKey         锁的key
     * @param clientId        客户端标识
     * @param expireSecond    锁定的时间(单位:秒)[必须大于0]
     * @param maxTryCount     当未获取到锁时的最大重试次数[必须大于0]
     * @param sleepMilliscond 重试间隔的休眠时间(毫秒)[至少100毫秒](最长休眠:sleepMilliscond毫秒,最少休眠:sleepMilliscond-90毫秒)
     * @param operateFlag     操作标识
     * @return
     * @throws InterruptedException 当休眠被中断时,抛出该异常
     */
    public static boolean tryLock(String lockKey, String clientId, Long expireSecond, Integer maxTryCount, Long sleepMilliscond, String operateFlag) throws InterruptedException {
        clientId = operateFlag + ":" + clientId;
        operateFlag = operateFlag + ":";
        boolean hasLock = RedisUtil.executeScript(TRY_LOCK_SCRIPT, Arrays.asList(lockKey), Boolean.class, clientId, expireSecond, operateFlag);
        if (!hasLock) {
            int tryCount = 0;
            Random random = new Random();
            // 重试
            long maxSleepMilliscond = sleepMilliscond;
            long minSleepMilliscond = sleepMilliscond - 90;
            while (tryCount < maxTryCount) {
                tryCount++;
                // 每次重试时,都休眠一小段时间
                int bound = (int) ((maxSleepMilliscond - minSleepMilliscond) + 1);
                long realSleepTime = random.nextInt(bound) + minSleepMilliscond;
                TimeUnit.MILLISECONDS.sleep(realSleepTime);
                hasLock = RedisUtil.executeScript(TRY_LOCK_SCRIPT, Arrays.asList(lockKey), Boolean.class, clientId, expireSecond, operateFlag);
                if (hasLock) {
                    break;
                }
            }
        }
        return hasLock;
    }

    /**
     * 尝试获取锁(如果未获取到锁,会进行3次重试,每次重试时休眠最长300毫秒,最短210毫秒)
     *
     * @param lockKey      锁的key
     * @param clientId     客户端标识
     * @param expireSecond 锁定的时间(单位:秒)[必须大于0]
     * @param operateFlag  操作标识
     * @return
     * @throws InterruptedException 当休眠被中断时,抛出该异常
     */
    public static boolean tryLock(String lockKey, String clientId, Long expireSecond, String operateFlag) throws InterruptedException {
        return tryLock(lockKey, clientId, expireSecond, 3, 300L, operateFlag);
    }

    /**
     * 释放锁
     *
     * @param lockKey  锁的key
     * @param clientId 客户端标识
     */
    public static void releaseLock(String lockKey, String clientId, String operateFlag) {
        clientId = operateFlag + ":" + clientId;
        RedisUtil.executeScript(RELEASE_LOCK_SCRIPT, Arrays.asList(lockKey), Long.class, clientId);
    }
}
