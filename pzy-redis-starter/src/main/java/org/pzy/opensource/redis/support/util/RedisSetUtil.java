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

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Redis的Set集合操作[无序集合]. 使用时需要在spring中进行bean的初始化<br>
 *
 * @author 潘志勇
 * @date 2019-02-09
 */
public class RedisSetUtil {

    private static RedisTemplate<String, Object> REDIS_TEMPLATE_VALUE_JSON_STRING;

    private static RedisSetUtil INSTANCE;

    private RedisSetUtil() {
    }

    public static RedisSetUtil newInstance(RedisTemplate<String, Object> redisTemplate) {
        if (null == REDIS_TEMPLATE_VALUE_JSON_STRING) {
            synchronized (RedisSetUtil.class) {
                if (null == REDIS_TEMPLATE_VALUE_JSON_STRING) {
                    REDIS_TEMPLATE_VALUE_JSON_STRING = redisTemplate;
                    INSTANCE = new RedisSetUtil();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * 给集合key添加多个值，重复的元素不会再次添加, 并返回添加的元素个数
     *
     * @param key
     * @param value
     * @return 添加的元素个数
     */
    public static long put(String key, Object... value) {
        return Optional.ofNullable(REDIS_TEMPLATE_VALUE_JSON_STRING.opsForSet().add(key, value)).orElseGet(() -> 0L);
    }

    /**
     * 随机删除集合中的一个值，并返回
     *
     * @param key
     * @return 被移除的元素
     */
    public static Optional<Object> pop(String key) {
        return Optional.ofNullable(REDIS_TEMPLATE_VALUE_JSON_STRING.opsForSet().pop(key));
    }

    /**
     * 移除集合中多个value值, 并返回移除的元素个数
     *
     * @param key
     * @param value
     * @return 移除的元素个数
     */
    public static long remove(String key, Object... value) {
        return Optional.ofNullable(REDIS_TEMPLATE_VALUE_JSON_STRING.opsForSet().remove(key, value)).orElseGet(() -> 0L);
    }

    /**
     * 把源集合中的一个元素移动到目标集合。成功返回true
     *
     * @param sourceKey 待移除元素的集合的key名
     * @param value     待移除的元素
     * @param destKey   待插入元素的集合的key名
     * @return 成功返回true
     */
    public static boolean move(String sourceKey, Object value, String destKey) {
        return Optional.ofNullable(REDIS_TEMPLATE_VALUE_JSON_STRING.opsForSet().move(sourceKey, value, destKey)).orElseGet(() -> false);
    }

    /**
     * 返回集合的长度
     *
     * @param key
     * @return
     */
    public static long size(String key) {
        return Optional.ofNullable(REDIS_TEMPLATE_VALUE_JSON_STRING.opsForSet().size(key)).orElseGet(() -> 0L);
    }

    /**
     * 检查集合中是否包含某个元素
     *
     * @param key 不能为null
     * @param val 待检索的元素
     * @return 包含则返回true
     */
    public static boolean isMember(String key, Object val) {
        return Optional.ofNullable(REDIS_TEMPLATE_VALUE_JSON_STRING.opsForSet().isMember(key, val)).orElseGet(() -> false);
    }

    /**
     * 求指定集合与另一个集合的交集
     *
     * @param key      集合的key名
     * @param otherKey 另一个集合的key名
     * @return
     */
    public static Set<Object> intersect(String key, String otherKey) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForSet().intersect(key, otherKey);
    }

    /**
     * 求指定集合与另外多个个集合交集
     *
     * @param key       集合的key名
     * @param otherKeys 其它集合的key名
     * @return
     * @see <a href="http://redis.io/commands/sinter">Redis Documentation: SINTER</a>
     */
    public static Set<Object> intersect(String key, Collection<String> otherKeys) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForSet().intersect(key, otherKeys);
    }

    /**
     * 求指定集合与另一个集合的交集，并且存储到目标集合中
     *
     * @param key      集合的key名
     * @param otherKey 另一个集合的key名
     * @param destKey  待插入元素的集合key名
     * @return 返回目标集合的长度
     */
    public static long intersectAndStore(String key, String otherKey, String destKey) {
        return Optional.ofNullable(REDIS_TEMPLATE_VALUE_JSON_STRING.opsForSet().intersectAndStore(key, otherKey, destKey)).orElseGet(() -> 0L);
    }

    /**
     * 求指定集合与另一个集合的并集 并返回并集
     *
     * @param key
     * @param otherKey
     * @return 并集
     */
    public static Set<Object> union(String key, String otherKey) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForSet().union(key, otherKey);
    }

    /**
     * 求指定集合与另外多个集合的并集 并返回并集
     *
     * @param key
     * @param otherKeys
     * @return
     */
    public static Set<Object> union(String key, Collection<String> otherKeys) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForSet().union(key, otherKeys);
    }

    /**
     * 求指定集合与另一个集合的并集，并保存到目标集合
     *
     * @param key
     * @param otherKey
     * @param destKey
     * @return
     */
    public static long unionAndStore(String key, String otherKey, String destKey) {
        return Optional.ofNullable(REDIS_TEMPLATE_VALUE_JSON_STRING.opsForSet().unionAndStore(key, otherKey, destKey)).orElseGet(() -> 0L);
    }

    /**
     * 求指定集合与另外多个集合的并集，并保存到目标集合
     *
     * @param key
     * @param otherKeys
     * @param destKey
     * @return
     */
    public static long unionAndStore(String key, Collection<String> otherKeys, String destKey) {
        return Optional.ofNullable(REDIS_TEMPLATE_VALUE_JSON_STRING.opsForSet().unionAndStore(key, otherKeys, destKey)).orElseGet(() -> 0L);
    }

    /**
     * 求指定集合与另一个集合的差集
     *
     * @param key
     * @param otherKey
     * @return 差集
     */
    public static Set<Object> difference(String key, String otherKey) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForSet().difference(key, otherKey);
    }

    /**
     * 求指定集合与另外多个集合的差集
     *
     * @param key
     * @param otherKeys
     * @return
     */
    public static Set<Object> difference(String key, Collection<String> otherKeys) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForSet().difference(key, otherKeys);
    }

    /**
     * 求指定集合与另一个集合的差集，并保存到目标集合
     *
     * @param key
     * @param otherKey
     * @param destKey  待插入元素的集合的key名
     * @return 插入元素的个数
     */
    public static long differenceAndStore(String key, String otherKey, String destKey) {
        return Optional.ofNullable(REDIS_TEMPLATE_VALUE_JSON_STRING.opsForSet().differenceAndStore(key, otherKey, destKey)).orElseGet(() -> 0L);
    }

    /**
     * 求指定集合与另外多个集合的差集，并保存到目标集合
     *
     * @param key
     * @param otherKeys
     * @param destKey
     * @return
     */
    public static long differenceAndStore(String key, Collection<String> otherKeys, String destKey) {
        return Optional.ofNullable(REDIS_TEMPLATE_VALUE_JSON_STRING.opsForSet().differenceAndStore(key, otherKeys, destKey)).orElseGet(() -> 0L);
    }

    /**
     * 获取集合中的所有元素
     *
     * @param key
     * @return
     */
    public static Set<Object> members(String key) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForSet().members(key);
    }

    /**
     * 随机获取集合中的一个元素
     *
     * @param key
     * @return
     */
    public static Optional<Object> randomMember(String key) {
        return Optional.ofNullable(REDIS_TEMPLATE_VALUE_JSON_STRING.opsForSet().randomMember(key));
    }

    /**
     * 随机返回集合中指定数量的元素。随机的元素不会重复
     *
     * @param key
     * @param count 待返回元素数量
     * @return
     */
    public static Set<Object> distinctRandomMembers(String key, long count) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForSet().distinctRandomMembers(key, count);
    }

    /**
     * 随机返回集合中指定数量的元素。随机的元素可能重复
     *
     * @param key
     * @param count 待返回元素数量
     * @return 如果key不存在会返回一个空集合
     */
    public static List<Object> randomMembers(String key, long count) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForSet().randomMembers(key, count);
    }

    /**
     * 获取集合的游标。通过游标可以遍历整个集合<br>
     * ScanOptions 这个类中使用了构造者 工厂方法 单例。 <br>
     * 通过它可以配置返回的元素个数 count  与正则匹配元素 match. <br>
     * 不过count设置后不代表一定返回的就是count个。这个只是参考意义<br>
     *
     * @param key
     * @param options
     * @return
     */
    public static Cursor<Object> scan(String key, ScanOptions options) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForSet().scan(key, options);
    }

}
