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

/**
 * 长地址转短地址
 * <p>
 * 基本原理:
 * 1. 用户发号器获取一个数值id, 并将该数值id转换为16进制数
 * 2. 将该16进制数与网址保存到 no sql 数据库
 * 3. 返回该16进制数
 *
 * @author pan
 * @date 2019-04-30
 */
public class TinyUrlUtil {

    private TinyUrlUtil() {
    }

    private static final char[] array =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd',
                    'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm',
                    'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D',
                    'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M'};

    private static Map<Character, Integer> charValueMap = new HashMap<Character, Integer>();

    //初始化map
    static {
        for (int i = 0; i < array.length; i++) {
            charValueMap.put(array[i], i);
        }
    }

    /**
     * 把数字转换成相对应的进制,目前支持(2-62)进制
     *
     * @param number  数字
     * @param decimal 进制
     * @return
     */
    public static String numberConvertToDecimal(long number, int decimal) {
        StringBuilder builder = new StringBuilder();
        while (number != 0) {
            builder.append(array[(int) (number - (number / decimal) * decimal)]);
            number /= decimal;
        }
        return builder.reverse().toString();
    }

    /**
     * 把进制字符串转换成相应的数字
     *
     * @param decimalStr 进制字符串转
     * @param decimal    进制
     * @return
     */
    public static long decimalConvertToNumber(String decimalStr, int decimal) {
        long sum = 0;
        long multiple = 1;
        char[] chars = decimalStr.toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            char c = chars[i];
            sum += charValueMap.get(c) * multiple;
            multiple *= decimal;
        }
        return sum;
    }
}
