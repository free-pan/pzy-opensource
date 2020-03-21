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

package org.pzy.opensource.comm.util;

import java.util.Map;

/**
 * @author pan
 * @date 2019-07-08
 */
public class WinterSnowflakeUtil {

    private static Map<String, WinterSnowflake> WINTER_SNOWFLAKE_MAP;

    private static final WinterSnowflakeUtil INSTANCE = new WinterSnowflakeUtil();

    private WinterSnowflakeUtil() {
    }

    public static final WinterSnowflakeUtil newInstance(Map<String, WinterSnowflake> stringWinterSnowflakeMap) {
        WINTER_SNOWFLAKE_MAP = stringWinterSnowflakeMap;
        return INSTANCE;
    }

    /**
     * 使用指定的id生成器生成一个id
     *
     * @param idGenerator 在springboot中配置的id生成器
     * @return long类型的id
     */
    public static Long nextId(String idGenerator) {
        WinterSnowflake winterSnowflake = getWinterSnowflake(idGenerator);
        return winterSnowflake.nextId();
    }

    /**
     * 使用指定的id生成器生成一个id
     *
     * @param idGenerator 在springboot中配置的id生成器
     * @return 字符串类型的id(实际就是将Long转为了String)
     */
    public static String nextIdStr(String idGenerator) {
        WinterSnowflake winterSnowflake = getWinterSnowflake(idGenerator);
        return winterSnowflake.nextIdStr();
    }

    private static WinterSnowflake getWinterSnowflake(String idGenerator) {
        WinterSnowflake winterSnowflake = WINTER_SNOWFLAKE_MAP.get(idGenerator);
        if (null == winterSnowflake) {
            throw new RuntimeException("id生成器[" + idGenerator + "]不存在!");
        }
        return winterSnowflake;
    }

    /**
     * 反推出机器id
     *
     * @param idGenerator 在springboot中配置的id生成器
     * @param id          snowflake算法生成的id
     * @return 所属机器的id
     */
    public static long inferWorkerId(String idGenerator, long id) {
        WinterSnowflake winterSnowflake = getWinterSnowflake(idGenerator);
        return winterSnowflake.inferWorkerId(id);
    }

    /**
     * 反推出数据中心的id
     *
     * @param idGenerator 在springboot中配置的id生成器
     * @param id          snowflake算法生成的id
     * @return 所属数据中心
     */
    public static long inferDataCenterId(String idGenerator, long id) {
        WinterSnowflake winterSnowflake = getWinterSnowflake(idGenerator);
        return winterSnowflake.inferDataCenterId(id);
    }

    /**
     * 反推生成时间
     *
     * @param idGenerator 在springboot中配置的id生成器
     * @param id          snowflake算法生成的id
     * @return 生成的时间的时间戳
     */
    public static long inferGenerateDateTime(String idGenerator, long id) {
        WinterSnowflake winterSnowflake = getWinterSnowflake(idGenerator);
        return winterSnowflake.inferGenerateDateTime(id);
    }
}
