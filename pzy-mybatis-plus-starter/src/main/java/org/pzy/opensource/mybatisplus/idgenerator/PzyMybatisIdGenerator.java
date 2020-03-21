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

package org.pzy.opensource.mybatisplus.idgenerator;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * 表的id生成器
 *
 * @author pan
 * @date 2019-12-11
 */
public class PzyMybatisIdGenerator implements IdentifierGenerator {

    // 将表的id生成器缓存起来
    private static Map<String, Snowflake> WINTER_SNOWFLAKE_MAP = new HashMap<>();

    @Override
    public Number nextId(Object entity) {
        String className = entity.getClass().getName();
        Snowflake snowflake = getSnowflake(className);
        return snowflake.nextId();
    }

    /**
     * 根据类全名获取id生成器
     *
     * @param className 类全名
     * @return id生成器
     */
    private Snowflake getSnowflake(String className) {
        Snowflake snowflake = WINTER_SNOWFLAKE_MAP.get(className);
        if (null == snowflake) {
            // id生成器不存在,则创建一个,并缓存起来
            snowflake = IdUtil.createSnowflake(1, 1);
            WINTER_SNOWFLAKE_MAP.put(className, snowflake);
        }
        return snowflake;
    }
}
