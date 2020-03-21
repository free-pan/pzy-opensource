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

import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis操作map结构
 *
 * @author 潘志勇
 * @date 2019-03-19
 */
public class RedisHashUtil {

    private static RedisTemplate REDIS_TEMPLATE_VALUE_JSON_STRING;

    private RedisHashUtil() {
    }

    public static RedisHashUtil INSTANCE;

    public static RedisHashUtil newInstance(RedisTemplate<String, Object> redisTemplate) {
        if (null == REDIS_TEMPLATE_VALUE_JSON_STRING) {
            synchronized (RedisHashUtil.class) {
                if (null == REDIS_TEMPLATE_VALUE_JSON_STRING) {
                    REDIS_TEMPLATE_VALUE_JSON_STRING = redisTemplate;
                    INSTANCE = new RedisHashUtil();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 将单个值放入redis的map结构中
     *
     * @param redisKey redis的key
     * @param key      map结构中的key
     * @param val      map结构中的值
     */
    public static void put(Object redisKey, Object key, Object val) {
        REDIS_TEMPLATE_VALUE_JSON_STRING.opsForHash().put(redisKey, key, val);
    }

    /**
     * 将单个值放入redis的map结构中(只有当map的key不存在时,才会执行插入)
     *
     * @param redisKey redis的key
     * @param key      map结构中的key
     * @param val      map结构中的值
     */
    public static void putIfAbsent(Object redisKey, Object key, Object val) {
        REDIS_TEMPLATE_VALUE_JSON_STRING.opsForHash().putIfAbsent(redisKey, key, val);
    }


    /**
     * 将map存入redis中
     *
     * @param redisKey
     * @param map
     */
    public static void putAll(Object redisKey, Map<?, ?> map) {
        REDIS_TEMPLATE_VALUE_JSON_STRING.opsForHash().putAll(redisKey, map);
    }

    /**
     * 将map存入redis中
     *
     * @param redisKey redis的key
     * @param mapKey   map中使用的key
     */
    public static long deleteByMapKey(Object redisKey, Object mapKey) {
        Long delCount = REDIS_TEMPLATE_VALUE_JSON_STRING.opsForHash().delete(redisKey, mapKey);
        return delCount == null ? 0 : delCount;
    }

    /**
     * 判断map中是否存在对应的key
     *
     * @param redisKey redis的key
     * @param mapKey   map中使用的key
     */
    public static boolean hasMapKey(Object redisKey, Object mapKey) {
        Boolean hasKey = REDIS_TEMPLATE_VALUE_JSON_STRING.opsForHash().hasKey(redisKey, mapKey);
        return null == hasKey ? false : hasKey;
    }

    /**
     * 通过map的key获取对应的值
     *
     * @param redisKey redis的key
     * @param mapKey   map中使用的key
     */
    public static Object getByMapKey(Object redisKey, Object mapKey) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForHash().get(redisKey, mapKey);
    }

    /**
     * 通过map的key集合获取对应的值集合
     *
     * @param redisKey redis的key
     * @param mapKeys  map中使用的key
     */
    public static List getByMapKey(Object redisKey, List mapKeys) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForHash().multiGet(redisKey, mapKeys);
    }

    /**
     * 获取map的所有key集合
     *
     * @param redisKey redis的key
     */
    public static Set getKeys(Object redisKey) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForHash().keys(redisKey);
    }

    /**
     * 获取map的所有key集合
     *
     * @param redisKey redis的key
     */
    public static List getValues(Object redisKey) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForHash().values(redisKey);
    }

    /**
     * 获取map的大小
     *
     * @param redisKey redis的key
     */
    public static Long size(Object redisKey) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForHash().size(redisKey);
    }

}
