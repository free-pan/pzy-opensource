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

package org.pzy.opensource.redis.support.springboot.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * key使用StringRedisSerializer作为序列化方式, value使用GenericJackson2JsonRedisSerializer作为序列化方式的,RedisTemplate实现
 *
 * @author pan
 * @date 2019-03-23
 */
public class StringKeyObjectJsonValueRedisTemplate extends RedisTemplate<String, Object> {

    private static StringRedisSerializer STRING_REDIS_SERIALIZER = new StringRedisSerializer();
    private static GenericJackson2JsonRedisSerializer GENERIC_JACKSON_2JSON_REDIS_SERIALIZER = new GenericJackson2JsonRedisSerializer();

    public StringKeyObjectJsonValueRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        super.setConnectionFactory(redisConnectionFactory);
        super.setKeySerializer(STRING_REDIS_SERIALIZER);
        super.setHashKeySerializer(STRING_REDIS_SERIALIZER);
        super.setValueSerializer(GENERIC_JACKSON_2JSON_REDIS_SERIALIZER);
    }
}
