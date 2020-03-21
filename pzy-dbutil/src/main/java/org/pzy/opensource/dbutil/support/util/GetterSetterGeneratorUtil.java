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

package org.pzy.opensource.dbutil.support.util;

/**
 * 根据属性名生成getter/setter方法
 *
 * @author pzy
 * @date 2018/11/11
 */
public class GetterSetterGeneratorUtil {

    private static final String BOOL_TYPE_KEY_A = "boolean";
    private static final String BOOL_TYPE_KEY_B = "java.lang.Boolean";
    private static final String GETTER_METHOD_PREFIX_A = "is";
    private static final String GETTER_METHOD_PREFIX_B = "get";
    private static final String SETTER_METHOD_PREFIX = "set";

    private GetterSetterGeneratorUtil() {
    }

    /**
     * 根据属性名称和java类型，获取对应的getter方法名
     *
     * @param property     属性名
     * @param javaType     属性类型
     * @param removePrefix 需要移除的属性前缀[为null,则表示不需要进行前缀移除]
     * @param removeSuffix 需要移除的属性后缀[为null,则表示不需要进行后缀移除]
     * @return
     */
    public static String getGetterMethodName(String property, String javaType, String removePrefix, String removeSuffix) {
        StringBuilder sb = new StringBuilder();
        // 移除前后缀
        String newProperty = removePrefixAndSuffix(property, removePrefix, removeSuffix);
        // 蛇形命名转驼峰命名
        newProperty = Underline2CamelUtil.underline2Camel(newProperty, true);
        sb.append(newProperty);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }
        if (BOOL_TYPE_KEY_A.equals(javaType) || BOOL_TYPE_KEY_B.equals(javaType)) {
            sb.insert(0, GETTER_METHOD_PREFIX_A);
        } else {
            sb.insert(0, GETTER_METHOD_PREFIX_B);
        }
        return sb.toString();
    }

    /**
     * 移除属性名的指定前缀和后缀
     *
     * @param property     原始属性名
     * @param removePrefix 前缀.为null表示不需要移除
     * @param removeSuffix 后缀.为null表示不需要移除
     * @return 移除前后缀的新属性名
     */
    private static String removePrefixAndSuffix(String property, String removePrefix, String removeSuffix) {
        String newProperty = property;
        if (null != removePrefix && newProperty.startsWith(removePrefix)) {
            newProperty = newProperty.substring(removePrefix.length());
        }
        if (null != removeSuffix && newProperty.endsWith(removeSuffix)) {
            newProperty = newProperty.substring(0, newProperty.length() - removePrefix.length());
        }
        return newProperty;
    }


    /**
     * 根据属性名称获取对应的setter方法名称
     *
     * @param property     属性名
     * @param removePrefix 需要移除的属性前缀[为null,则表示不需要进行前缀移除]
     * @param removeSuffix 需要移除的属性后缀[为null,则表示不需要进行后缀移除]
     * @return
     */
    public static String getSetterMethodName(String property, String removePrefix, String removeSuffix) {
        StringBuilder sb = new StringBuilder();
        String newProperty = property;
        // 移除前后缀
        newProperty = removePrefixAndSuffix(newProperty, removePrefix, removeSuffix);
        // 蛇形命名转驼峰命名
        newProperty = Underline2CamelUtil.underline2Camel(newProperty, true);
        sb.append(newProperty);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }
        sb.insert(0, SETTER_METHOD_PREFIX);
        return sb.toString();
    }
}
