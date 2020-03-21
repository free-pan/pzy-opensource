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
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * key使用StringRedisSerializer作为序列化方式, value使用JdkSerializationRedisSerializer作为序列化方式的,RedisTemplate实现
 * 适用于value为byte数组的情况
 *
 * @author pan
 * @date 2019-03-23
 */
public class StringKeyObjectByteArrayValueRedisTemplate extends RedisTemplate<String, Object> {

    private static StringRedisSerializer STRING_REDIS_SERIALIZER = new StringRedisSerializer();
    private static JdkSerializationRedisSerializer VALUE_SERIALIZER = new JdkSerializationRedisSerializer();

    public StringKeyObjectByteArrayValueRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        super.setConnectionFactory(redisConnectionFactory);
        super.setKeySerializer(STRING_REDIS_SERIALIZER);
        super.setHashKeySerializer(STRING_REDIS_SERIALIZER);
        super.setValueSerializer(VALUE_SERIALIZER);
    }
}
