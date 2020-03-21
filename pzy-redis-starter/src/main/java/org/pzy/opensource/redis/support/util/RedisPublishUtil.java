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

/**
 * 用于向redis中发布消息. 使用时需要在spring中进行bean的初始化<br>
 *
 * @author 潘志勇
 * @date 2019-03-19
 */
public class RedisPublishUtil {

    private static RedisTemplate<String, Object> REDIS_TEMPLATE_VALUE_JSON_STRING;

    public static RedisPublishUtil INSTANCE;

    private RedisPublishUtil() {
    }

    public static RedisPublishUtil newInstance(RedisTemplate<String, Object> redisTemplate) {
        if (null == REDIS_TEMPLATE_VALUE_JSON_STRING) {
            synchronized (RedisSetUtil.class) {
                if (null == REDIS_TEMPLATE_VALUE_JSON_STRING) {
                    REDIS_TEMPLATE_VALUE_JSON_STRING = redisTemplate;
                    INSTANCE = new RedisPublishUtil();
                }
            }
        }
        return INSTANCE;
    }

    public static void publish(String channel, Object message) {
        REDIS_TEMPLATE_VALUE_JSON_STRING.convertAndSend(channel, message);
    }
}
