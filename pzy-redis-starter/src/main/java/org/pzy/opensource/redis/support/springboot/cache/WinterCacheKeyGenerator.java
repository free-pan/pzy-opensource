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

package org.pzy.opensource.redis.support.springboot.cache;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * 自定义spring-cache的key生成方式
 *
 * @author pan
 * @date 2019-07-17
 */
public class WinterCacheKeyGenerator implements KeyGenerator {

    public WinterCacheKeyGenerator() {
    }

    /**
     * key生成方式
     *
     * @param target 缓存注解所在类实例
     * @param method 缓存注解所在的方法
     * @param params 缓存注解所在方法的参数
     * @return key对象
     */
    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(target.getClass().getSimpleName()).append(":");
        stringBuilder.append(method.getName());
        if (params.length == 0) {
            return stringBuilder.toString();
        }
        if (params.length == 1) {
            Object param = params[0];
            if (param != null && !param.getClass().isArray()) {
                stringBuilder.append(":");
                return stringBuilder.toString() + param;
            }
        }
        stringBuilder.append(":");
        return new WinterSimpleKey(stringBuilder.toString(), params);
    }
}
