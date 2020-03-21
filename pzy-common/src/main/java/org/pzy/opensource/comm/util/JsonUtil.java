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

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * @author pan
 * @date 2019-12-12
 */
public class JsonUtil {

    private JsonUtil() {
    }

    /**
     * 对象转json字符串
     *
     * @param obj
     * @return
     */
    public static final String toJsonString(Object obj) {
        return toJsonString(obj, false);
    }

    /**
     * 对象转json字符串
     *
     * @param obj
     * @param pretty 是否格式化
     * @return
     */
    private static final String toJsonString(Object obj, boolean pretty) {
        if (null == obj) {
            return null;
        }
        return JSONObject.toJSONString(obj, pretty);
    }

    /**
     * 对象转json字符串(对json字符串进行美化输出)
     *
     * @param obj
     * @return
     */
    public static final String toPrettyJsonString(Object obj) {
        return toJsonString(obj, true);
    }

    /**
     * json字符串转java对象
     *
     * @param jsonStr json字符串
     * @param cls     java对象的class类型
     * @param <T>
     * @return
     */
    public static final <T> T toJavaBean(String jsonStr, Class<T> cls) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        return JSONObject.parseObject(jsonStr, cls);
    }
}
