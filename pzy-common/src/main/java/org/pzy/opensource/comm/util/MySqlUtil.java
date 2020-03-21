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


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author pan
 * @date 2019-12-11
 */
public class MySqlUtil {

    /**
     * key为特殊字符,value为特殊字符对应的转义字符
     */
    private static final Map<String, String> ESCAPE_CHARACTER_MAP = new HashMap<>();

    static {
        ESCAPE_CHARACTER_MAP.put("%", "/%");
        ESCAPE_CHARACTER_MAP.put("\\?", "/?");
        ESCAPE_CHARACTER_MAP.put("_", "/_");
    }

    private MySqlUtil() {
    }

    /**
     * 将查询关键词中的特殊字符(%, _, ?)进行转义
     *
     * @param key
     * @return
     */
    public static String escape(String key) {
        if (null == key || key.trim().length() == 0) {
            return key;
        }
        String tmp = key.trim();
        Set<Map.Entry<String, String>> entrySet = ESCAPE_CHARACTER_MAP.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            tmp = tmp.replaceAll(entry.getKey(), entry.getValue());
        }
        return tmp;
    }
}
