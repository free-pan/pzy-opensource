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

/**
 * redis的List数据类型操作. 使用时需要在spring中进行bean的初始化<br>
 *
 * @author 潘志勇
 * @date 2019-02-09
 */
public class RedisListUtil {

    private static RedisTemplate<String, Object> REDIS_TEMPLATE_VALUE_JSON_STRING;

    public static RedisListUtil INSTANCE;

    private RedisListUtil() {
    }

    public static RedisListUtil newInstance(RedisTemplate<String, Object> redisTemplate) {
        if (null == REDIS_TEMPLATE_VALUE_JSON_STRING) {
            synchronized (RedisSetUtil.class) {
                if (null == REDIS_TEMPLATE_VALUE_JSON_STRING) {
                    REDIS_TEMPLATE_VALUE_JSON_STRING = redisTemplate;
                    INSTANCE = new RedisListUtil();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * 从key列表左边（从头部）插入 1个值. 前提:队列的key必须是已存在的
     *
     * @param key
     * @param value
     * @return 队列总长度
     */
    public static Long leftPush(String key, Object value) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForList().leftPush(key, value);
    }


    /**
     * 从key列表左边（从头部）插入 N个值
     *
     * @param key
     * @param value
     * @return 队列总长度
     */
    public static Long leftPushAll(String key, Object... value) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForList().leftPushAll(key, value);
    }

    /**
     * 移除列表中的第一个值(移除队头元素)，并返回该值
     *
     * @param key
     * @return 如果队列为空或key代表的队列不存在, 则返回null
     */
    public static Object leftPop(String key) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForList().leftPop(key);
    }

    /**
     * 从key列表右边（从尾部）插入 1个值. 前提:队列的key必须是已存在的
     *
     * @param key
     * @param value
     * @return
     */
    public static Long rightPush(String key, Object value) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForList().rightPush(key, value);
    }

    /**
     * (只有当key存在时,才会执行插入)从key列表右边（从尾部）插入 1个值. 前提:队列的key必须是已存在的
     *
     * @param key
     * @param value
     * @return 当key存在时, 返回队列的实际长度, 当key对应的队列不存在时, 返回0
     */
    public static Long rightPushIfPresent(String key, Object value) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForList().rightPushIfPresent(key, value);
    }

    /**
     * 从key列表右边（从尾部）插入 N个值
     *
     * @param key
     * @param value
     * @return
     */
    public static Long rightPushAll(String key, Object... value) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForList().rightPushAll(key, value);
    }

    /**
     * 移除列表中的最后个值，并返回该值
     *
     * @param key
     * @return
     */
    public static Object rightPop(String key) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForList().rightPop(key);
    }

    /**
     * 栈/队列长
     *
     * @param key
     * @return
     */
    public static Long length(String key) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForList().size(key);
    }

    /**
     * 范围检索[时间复杂度为:O(N)]
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static List<Object> range(String key, int start, int end) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForList().range(key, start, end);
    }

    /**
     * 删除列表中第一个遇到的value值。count指定删除多少个
     *
     * @param key
     * @param count 删除个数
     * @param value
     */
    public static void remove(String key, long count, Object value) {
        REDIS_TEMPLATE_VALUE_JSON_STRING.opsForList().remove(key, count, value);
    }

    /**
     * 获取列表中指定索引的value
     *
     * @param key
     * @param index 元素位置
     * @return
     */
    public static Object index(String key, long index) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForList().index(key, index);
    }

    /**
     * 设置key列表中指定位置的值为value index不能大于列表长度。大于抛出异常<br>
     * 为负数则从右边开始计算<br>
     *
     * @param key
     * @param index
     * @param value
     */
    public static void set(String key, long index, Object value) {
        REDIS_TEMPLATE_VALUE_JSON_STRING.opsForList().set(key, index, value);
    }

    /**
     * 从指定列表中从右边（尾部）移除第一个值，并将这个值从左边（头部）插入目标列表, 并返回 移除（插入）的值
     *
     * @param sourceKey      待移除元素的队列
     * @param destinationKey 待插入元素的队列
     * @return 移除（插入）的值
     */
    public static Object rightPopAndLeftPush(String sourceKey, String destinationKey) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForList().rightPopAndLeftPush(sourceKey, destinationKey);
    }

}
