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

import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * 有序集合. 使用时需要在spring中进行bean的初始化<br>
 *
 * @author 潘志勇
 * @date 2019-02-09
 */
public class RedisSortedSetUtil {

    private static RedisTemplate<String, Object> REDIS_TEMPLATE_VALUE_JSON_STRING;

    public static RedisSortedSetUtil INSTANCE;

    private RedisSortedSetUtil() {
    }

    public static RedisSortedSetUtil newInstance(RedisTemplate<String, Object> redisTemplate) {
        if (null == REDIS_TEMPLATE_VALUE_JSON_STRING) {
            synchronized (RedisSortedSetUtil.class) {
                if (null == REDIS_TEMPLATE_VALUE_JSON_STRING) {
                    REDIS_TEMPLATE_VALUE_JSON_STRING = redisTemplate;
                    INSTANCE = new RedisSortedSetUtil();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 给有序集合添加一个指定分数的成员 如果成员存在则覆盖
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    public static boolean put(String key, Object value, double score) {
        return Optional.ofNullable(REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().add(key, value, score)).orElse(false);
    }

    /**
     * 通过TypedTuple的方式，往有序集合中添加成员集合。<br>
     * TypedTuple 默认实现为 DefaultTypedTuple
     *
     * @param key
     * @param tuples
     * @return
     */
    public static long put(String key, Set<ZSetOperations.TypedTuple<Object>> tuples) {
        return Optional.ofNullable(REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().add(key, tuples)).orElse(0L);
    }

    /**
     * 移除有序集合中指定的多个成员. 如果成员不存在则忽略
     *
     * @param key
     * @param values
     * @return
     */
    public static long remove(String key, Object... values) {
        return Optional.ofNullable(REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().remove(key, values)).orElse(0L);
    }

    /**
     * 给有序集合中的指定成员的分数增加 delta
     *
     * @param key   如果key不存在,则会自动新增
     * @param value 如果val不存在,则会自动新增
     * @param delta 增加的分数
     * @return 返回最终的分数
     */
    public static Double incrementScore(String key, Object value, double delta) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().incrementScore(key, value, delta);
    }

    /**
     * 计算并返回成员在有序集合中从低到高的排名（第一名为0）
     *
     * @param key
     * @param val
     * @return 如果key不存在或val不存在, 都返回null
     */
    public static Long rank(String key, Object val) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().rank(key, val);
    }

    /**
     * 计算并返回成员在有序集合中从高到低的排名（第一名为0）
     *
     * @param key
     * @param val
     * @return
     */
    public static Long reverseRank(String key, Object val) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().reverseRank(key, val);
    }

    /**
     * 获取指定范围内的成员集合。（0 -1）返回所有。 如果为正数，则按正常顺序取，如果为负数则反序取。
     *
     * @param key
     * @param start 元素开始位置. 第一个元素的位置为0
     * @param end   元素结束位置
     * @return
     */
    public static Set<Object> range(String key, long start, long end) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().range(key, start, end);
    }

    /**
     * 获取有序集合中指定范围内的成员集合(包括成员的分数)
     *
     * @param key
     * @param start 元素开始位置
     * @param end   元素结束位置
     * @return
     */
    public static Set<ZSetOperations.TypedTuple<Object>> rangeWithScores(String key, long start, long end) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().rangeWithScores(key, start, end);
    }

    /**
     * 获取有序集合中分数在指定的最小值 与最大值之间的所有成员集合  闭合区间
     *
     * @param key
     * @param min 最小分数
     * @param max 最大分数
     * @return 如果key不存在, 则返回一个空集合
     */
    public static Set<Object> rangeByScore(String key, double min, double max) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 获取有序集合中分数在指定的最小值 与最大值之间的所有成员的TypedTuple集合  闭合区间
     *
     * @param key
     * @param min 最小分数值
     * @param max 最大分数
     * @return
     */
    public static Set<ZSetOperations.TypedTuple<Object>> rangeByScoreWithScores(String key, double min, double max) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().rangeByScoreWithScores(key, min, max);
    }

    /**
     * 从有序集合中从指定位置（offset)开始，取 count个元素, 分数范围在（min)与（max)之间的成员集合
     *
     * @param key    集合key名
     * @param min    最小分数
     * @param max    最大分数
     * @param offset 起始位置
     * @param count  获取的元素数量
     * @return
     */
    public static Set<Object> rangeByScore(String key, double min, double max, long offset, long count) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().rangeByScore(key, min, max, offset, count);
    }

    /**
     * 从有序集合中从指定位置（offset)开始，取 count个元素, 分数范围在（min)与（max)之间的成员集合
     *
     * @param key    集合key名
     * @param min    最小分数
     * @param max    最大分数
     * @param offset 起始位置
     * @param count  获取的元素数量
     * @return
     */
    public static Set<ZSetOperations.TypedTuple<Object>> rangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().rangeByScoreWithScores(key, min, max, offset, count);
    }

    /**
     * 从有序集合中获取指定范围内分数从高到低的成员集合
     *
     * @param key
     * @param start 开始位置
     * @param end   结束位置
     * @return
     */
    public static Set<Object> reverseRange(String key, long start, long end) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 从有序集合中获取指定范围内从高到低的成员TypedTuple集合
     *
     * @param key
     * @param start 开始位置
     * @param end   结束位置
     * @return
     */
    public static Set<ZSetOperations.TypedTuple<Object>> reverseRangeWithScores(String key, long start, long end) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    /**
     * 从有序集合中获取分数在指定范围内从高到低的成员集合
     *
     * @param key
     * @param min 最小分数
     * @param max 最大分数
     * @return
     */
    public static Set<Object> reverseRangeByScore(String key, double min, double max) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().reverseRangeByScore(key, min, max);
    }

    /**
     * 从有序集合中获取分数在指定范围内从高到低的成员TypedTuple集合
     *
     * @param key
     * @param min 最小分数
     * @param max 最大分数
     * @return
     */
    public static Set<ZSetOperations.TypedTuple<Object>> reverseRangeByScoreWithScores(String key, double min, double max) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
    }

    /**
     * 从有序集合中从指定位置（offset)开始 获取 count个
     * 分数在指定（min, max)范围内从高到低的成员集合
     * 排序方式从高到低.
     *
     * @param key
     * @param min    最小分数
     * @param max    最大分数
     * @param offset 起始位置
     * @param count  需要的结果数
     * @return
     */
    public static Set<Object> reverseRangeByScore(String key, double min, double max, long offset, long count) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
    }

    /**
     * 从有序集合中从指定位置（offset)开始 获取 count个
     * 分数在指定（min, max)范围内从高到低的成员TypedTuple集合
     *
     * @param key
     * @param min    最小分数
     * @param max    最大分数
     * @param offset 起始位置
     * @param count  需要的结果数
     * @return
     */
    public static Set<ZSetOperations.TypedTuple<Object>> reverseRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().reverseRangeByScoreWithScores(key, min, max, offset, count);
    }

    /**
     * 统计分数在范围内的成员个数
     *
     * @param key
     * @param min 最小分数
     * @param max 最大分数
     * @return
     */
    public static Long count(String key, double min, double max) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().count(key, min, max);
    }

    /**
     * 返回有序集合的大小
     */
    public static Long size(String key) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().size(key);
    }

    /**
     * 返回有序集合的大小
     */
    public static Long zCard(String key) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().zCard(key);
    }

    /**
     * 获取有序集合中某个成员的分数
     */
    public static Double score(String key, Object val) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().score(key, val);
    }

    /**
     * 移除有序集合中从start开始到end结束的成员 闭区间
     */
    public static Long removeRange(String key, long start, long end) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().removeRange(key, start, end);
    }

    /**
     * 移除有序集合中分数在指定范围内[min,max]的成员
     *
     * @param key
     * @param min 最小分数
     * @param max 最大分数
     * @return
     */
    public static Long removeRangeByScore(String key, double min, double max) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().removeRangeByScore(key, min, max);
    }

    /**
     * 求两个有序集合的并集，并存到目标集合中。 如果存在相同的成员，则分数相加。
     *
     * @param key
     * @param otherKey
     * @param destKey
     * @return
     */
    public static Long unionAndStore(String key, String otherKey, String destKey) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().unionAndStore(key, otherKey, destKey);
    }

    /**
     * 一个有序集合与多个有序集合进行并集， 如果存在相同成员，则分数相加
     *
     * @param key
     * @param otherKeys
     * @param destKey
     * @return
     */
    public static Long unionAndStore(String key, Collection<String> otherKeys, String destKey) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 求两个有序集合中相同成员的并集（分数相加），并存到目标集合中。没有共同的成员则忽略
     *
     * @param key
     * @param otherKey
     * @param destKey
     * @return
     */
    public static Long intersectAndStore(String key, String otherKey, String destKey) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().intersectAndStore(key, otherKey, destKey);
    }

    /**
     * 一个有序集合与多个有序集合进行相同成员并集， 如果存在不相同相同成员，则忽略
     *
     * @param key
     * @param otherKeys
     * @param destKey
     * @return
     */
    public static Long intersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().intersectAndStore(key, otherKeys, destKey);
    }

    /**
     * 获取有序集合的TypedTuple游标。
     * ScanOptions  可以配置匹配正则 设置参考的count
     *
     * @param key
     * @param options
     * @return
     */
    public static Cursor<ZSetOperations.TypedTuple<Object>> scan(String key, ScanOptions options) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().scan(key, options);
    }

    /**
     * 获取有序集合中成员在指定范围内的集合（成员范围，不是分值范围）
     *
     * @param key
     * @param range
     * @return
     */
    public static Set<Object> rangeByLex(String key, RedisZSetCommands.Range range) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().rangeByLex(key, range);
    }

    /**
     * 获取指定索引开始，获取count个成员在指定范围内的集合（成员范围，不是分值范围）
     *
     * @param key
     * @param range
     * @param limit
     * @return
     */
    public static Set<Object> rangeByLex(String key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForZSet().rangeByLex(key, range, limit);
    }
}
