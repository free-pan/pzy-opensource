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

import org.apache.commons.lang3.StringUtils;
import org.pzy.opensource.redis.domain.bo.IncrIdBO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * 使用redis进行自增/自减操作
 *
 * @author 潘志勇
 * @date 2019-03-21
 */
public class RedisIncrAndDecrUtil {

    private static RedisTemplate<String, Object> REDIS_TEMPLATE_VALUE_JSON_STRING;

    private RedisIncrAndDecrUtil() {
    }

    public static RedisIncrAndDecrUtil INSTANCE;

    public static RedisIncrAndDecrUtil newInstance(RedisTemplate<String, Object> redisTemplate) {
        if (null == REDIS_TEMPLATE_VALUE_JSON_STRING) {
            synchronized (RedisIncrAndDecrUtil.class) {
                if (null == REDIS_TEMPLATE_VALUE_JSON_STRING) {
                    REDIS_TEMPLATE_VALUE_JSON_STRING = redisTemplate;
                    INSTANCE = new RedisIncrAndDecrUtil();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 自增操作
     *
     * @param key
     * @return
     */
    public static Long incr(String key) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForValue().increment(key);
    }

    /**
     * 自增操作
     *
     * @param key
     * @param step 自增步长
     * @return
     */
    public static Long incr(String key, Integer step) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForValue().increment(key, step);
    }

    /**
     * 自减操作
     *
     * @param key
     * @return
     */
    public static Long decr(String key) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForValue().decrement(key);
    }

    /**
     * 自减操作
     *
     * @param key
     * @param step 自减步长
     * @return
     */
    public static Long decr(String key, Integer step) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForValue().decrement(key, step);
    }

    /**
     * 根据key获取自增id,prefix和datePattern不能同时为空
     *
     * @param prefix      前缀
     * @param datePattern 日期格式字符串[可以为空]
     * @param expireTime  过期时间,小于等于0表示永不过期[单位:秒]
     * @return
     */
    public static IncrIdBO getId(String prefix, String datePattern, long expireTime) {
        if (StringUtils.isBlank(prefix) && StringUtils.isBlank(datePattern)) {
            throw new RuntimeException("prefix和datePattern不能同时为空!");
        }
        String dateStr = null;
        if (StringUtils.isNotBlank(datePattern)) {
            dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern(datePattern));
        } else {
            dateStr = StringUtils.EMPTY;
        }
        String key;
        if (StringUtils.isBlank(prefix)) {
            key = dateStr;
        } else if (StringUtils.isBlank(dateStr)) {
            key = prefix.trim();
        } else {
            key = prefix.trim() + ":" + dateStr;
        }

        IncrIdBO incrIdBO = new IncrIdBO();
        incrIdBO.setKey(key);
        incrIdBO.setId(getIncr(key, expireTime));
        return incrIdBO;


    }

    /**
     * 根据key和expireTime获取自增值<br/><br/>
     * 如何防止,redis重启之后,自增值由重新开始计算?<br/>
     * 解决方案: <a href='https://blog.csdn.net/u010648555/article/details/73442336'>开启AOF备份</a>
     *
     * @param key
     * @param expireTime 小于等于0表示永久有效. 单位:秒
     * @return
     */
    private static Long getIncr(String key, long expireTime) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, REDIS_TEMPLATE_VALUE_JSON_STRING.getConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();
        if (expireTime > 0) {
            //初始设置过期时间
            entityIdCounter.expire(expireTime, TimeUnit.SECONDS);
        }
        return increment;
    }
}
