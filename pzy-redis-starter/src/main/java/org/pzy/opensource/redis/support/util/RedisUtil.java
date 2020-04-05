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
import org.pzy.opensource.redis.support.springboot.redis.StringKeyObjectByteArrayValueRedisTemplate;
import org.pzy.opensource.spring.util.ResourceFileLoadUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 封装redis的常用操作. 使用时需要在spring中进行bean的初始化<br>
 * <br>
 * 关于key的设计<br>
 * <strong>key的存活时间</strong><br>
 * 无论什么时候，只要有可能就利用key超时的优势。一个很好的例子就是储存一些诸如临时认证key之类的东西。当你去查找一个授权key时——以OAUTH为例——通常会得到一个超时时间。
 * 这样在设置key的时候，设成同样的超时时间，Redis就会自动为你清除。<br>
 * <strong>关系型数据库的redis</strong><br>
 * 1: 把表名转换为key前缀 如, tag:<br>
 * 2: 第2段放置用于区分区key的字段--对应mysql中的主键的列名,如userid<br>
 * 3: 第3段放置主键值,如2,3,4...., a , b ,c<br>
 * 4: 第4段,写要存储的列名<br>
 * 例：user:userid:9:username<br>
 *
 * @author 潘志勇
 * @date 2019-02-03
 */
public class RedisUtil {

    private static RedisTemplate<String, Object> REDIS_TEMPLATE_VALUE_JSON_STRING;

    private static StringKeyObjectByteArrayValueRedisTemplate STRING_KEY_OBJECT_BYTE_ARRAY_VALUE_REDIS_TEMPLATE;

    /**
     * 按前缀删除redis数据的lua脚本
     */
    private static String DEL_KEY_PREFIX = null;

    static {
        String scriptFile = "classpath:script/DelKeyByPrefix.lua";
        try {
            DEL_KEY_PREFIX = ResourceFileLoadUtil.loadAsString(scriptFile, GlobalConstant.DEFAULT_CHARSET);
        } catch (IOException e) {
            throw new RuntimeException(String.format("lua脚本文件[%s]读取异常!", scriptFile));
        }
    }

    private RedisUtil() {
    }

    public static RedisUtil INSTANCE;

    public static RedisUtil newInstance(RedisTemplate<String, Object> redisTemplate, StringKeyObjectByteArrayValueRedisTemplate stringKeyObjectByteArrayValueRedisTemplate) {
        if (null == REDIS_TEMPLATE_VALUE_JSON_STRING) {
            synchronized (RedisUtil.class) {
                if (null == REDIS_TEMPLATE_VALUE_JSON_STRING) {
                    REDIS_TEMPLATE_VALUE_JSON_STRING = redisTemplate;
                    STRING_KEY_OBJECT_BYTE_ARRAY_VALUE_REDIS_TEMPLATE = stringKeyObjectByteArrayValueRedisTemplate;
                    INSTANCE = new RedisUtil();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 往redis中插入数据
     *
     * @param key
     * @param val
     */
    public static void put(String key, Object val) {
        REDIS_TEMPLATE_VALUE_JSON_STRING.opsForValue().set(key, val);
    }

    /**
     * 往redis中插入比特数组数据
     *
     * @param key
     * @param val value类型为byte[]或Byte[]
     */
    public static void putByteArr(String key, Object val) {
        STRING_KEY_OBJECT_BYTE_ARRAY_VALUE_REDIS_TEMPLATE.opsForValue().set(key, val);
    }

    /**
     * 往redis插入数据,并设置过期时间
     *
     * @param key
     * @param val
     * @param expireTime 过期时间. 单位:秒
     */
    public static void put(String key, Object val, long expireTime) {
        REDIS_TEMPLATE_VALUE_JSON_STRING.opsForValue().set(key, val, Duration.ofSeconds(expireTime));
    }

    /**
     * 往redis插入比特数组数据,并设置过期时间
     *
     * @param key
     * @param val        value类型为byte[]或Byte[]
     * @param expireTime 过期时间. 单位:秒
     */
    public static void putByteArr(String key, Object val, long expireTime) {
        STRING_KEY_OBJECT_BYTE_ARRAY_VALUE_REDIS_TEMPLATE.opsForValue().set(key, val, Duration.ofSeconds(expireTime));
    }

    /**
     * 如果key不存在,则往redis中插入数据,并设置失效时间
     *
     * @param key
     * @param val
     * @param expireSecond 失效时间(单位:秒)
     * @return 插入结果. true插入成功,否则表示插入失败
     */
    public static boolean setIfAbsent(String key, Object val, long expireSecond) {
        Boolean ret = REDIS_TEMPLATE_VALUE_JSON_STRING.opsForValue().setIfAbsent(key, val, expireSecond, TimeUnit.SECONDS);
        return null == ret ? false : ret;
    }

    /**
     * 执行脚本,并获取返回值
     *
     * @param script     脚本
     * @param keys       key参数
     * @param returnType 返回值的class类型
     * @param args       可变长度的args参数
     * @param <T>
     * @return 返回值
     */
    public static <T> T executeScript(String script, List keys, Class<T> returnType, Object... args) {
        RedisScript<T> luaScript = new DefaultRedisScript<>(script, returnType);
        return (T) REDIS_TEMPLATE_VALUE_JSON_STRING.execute(luaScript, keys, args);
    }

    /**
     * 如果key不存在,则往redis中插入数据
     *
     * @param key
     * @param val
     * @return 插入结果. true插入成功,否则表示插入失败
     */
    public static boolean setIfAbsent(String key, Object val) {
        Boolean ret = REDIS_TEMPLATE_VALUE_JSON_STRING.opsForValue().setIfAbsent(key, val);
        return null == ret ? false : ret;
    }

    /**
     * 删除指定key的数据
     *
     * @param key
     */
    public static void remove(String key) {
        REDIS_TEMPLATE_VALUE_JSON_STRING.delete(key);
    }

    /**
     * 删除指定key前缀的数据
     *
     * @param key key写法示例. test*, apple*
     * @return 删除的数据数量
     */
    public static Long removeByKeyPrefix(String key) {
        return RedisUtil.executeScript(DEL_KEY_PREFIX, Arrays.asList(key), Long.class);
    }

    /**
     * 判断redis中是否存在指定的key
     *
     * @param key
     * @return
     */
    public static boolean hasKey(String key) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.hasKey(key);
    }

    /**
     * 获取key对应的value
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForValue().get(key);
    }

    /**
     * 获取key对应的value(适用于value类型为byte[]或Byte[]的情况)
     *
     * @param key
     * @return
     */
    public static Object getByteArr(String key) {
        return STRING_KEY_OBJECT_BYTE_ARRAY_VALUE_REDIS_TEMPLATE.opsForValue().get(key);
    }

    /**
     * 一次获取多个key的值
     *
     * @param key
     * @return
     */
    public static List mutilGet(String... key) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.opsForValue().multiGet(Arrays.asList(key));
    }

    /**
     * 一次获取多个key的值(适用于值类型是byte数组情况)
     *
     * @param key
     * @return
     */
    public static List mutilGetByteArr(String... key) {
        return STRING_KEY_OBJECT_BYTE_ARRAY_VALUE_REDIS_TEMPLATE.opsForValue().multiGet(Arrays.asList(key));
    }

    /**
     * 根据key获取过期时间
     *
     * @param key
     * @return
     */
    public static Long getExpire(String key) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.getExpire(key);
    }

    /**
     * 根据pattern获取所有匹配的key
     *
     * @param pattern
     * @return
     */
    public static Set getKeys(String pattern) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.keys(pattern);
    }

    /**
     * 设置key的过期时间
     *
     * @param key
     * @param expireTime 过期时间. 单位:秒
     * @return 是否设置成功
     */
    public static Boolean setExpire(String key, Long expireTime) {
        return REDIS_TEMPLATE_VALUE_JSON_STRING.expire(key, expireTime, TimeUnit.SECONDS);
    }
}
