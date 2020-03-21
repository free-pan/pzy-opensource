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

package org.pzy.opensource.springboot.swagger.factory;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.core.Ordered;
import org.springframework.util.CollectionUtils;
import springfox.documentation.builders.AlternateTypeBuilder;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.schema.AlternateTypeRules;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * swagger类型解析器实例工厂
 *
 * @author 潘志勇
 * @date 2019-01-14
 */
public class AlternateTypeRuleConventionFactory {

    private AlternateTypeRuleConventionFactory() {
    }

    /**
     * 创建AlternateTypeRuleConvention实例,用于给对应的类,使用特殊的swagger解析方式<br>
     * cleanClassList中存储的是类全名<br>
     * mappingClass中存储的是key为无swagger注解的类的类全名,value为有swagger注解的类的类全名
     *
     * @param resolver       类型解析器
     * @param cleanClassList 需要swagger忽略的类集合
     * @param mappingClass   需要被swagger以另外方式映射的类
     * @return
     */
    public static AlternateTypeRuleConvention newInstance(TypeResolver resolver, List<String> cleanClassList, Map<String, String> mappingClass) {
        return new AlternateTypeRuleConvention() {
            @Override
            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }

            @Override
            public List<AlternateTypeRule> rules() {
                List<AlternateTypeRule> list = new ArrayList<>();
                if (!CollectionUtils.isEmpty(cleanClassList)) {
                    for (String singleClass : cleanClassList) {
                        try {
                            list.add(new AlternateTypeRule(resolver.resolve(Class.forName(singleClass)),
                                    resolver.resolve(cleanProperty(singleClass))));
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(String.format("类[%s]不存在", singleClass), e);
                        }
                    }
                }

                if (!CollectionUtils.isEmpty(mappingClass)) {
                    Set<Map.Entry<String, String>> entrySet = mappingClass.entrySet();
                    for (Map.Entry<String, String> entry : entrySet) {
                        try {
                            list.add(AlternateTypeRules.newRule(resolver.resolve(Class.forName(entry.getKey())), resolver.resolve(Class.forName(entry.getValue()))));
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException("类不存在!", e);
                        }
                    }
                }
                return list;
            }
        };
    }

    /**
     * 清除类的所有属性
     *
     * @param classFullName
     * @return
     * @throws ClassNotFoundException
     */
    private static Type cleanProperty(String classFullName) throws ClassNotFoundException {
        Class cls = Class.forName(classFullName);
        return new AlternateTypeBuilder().fullyQualifiedClassName(String.format("%s.generated.%s",
                cls.getPackage().getName(), cls.getSimpleName()))
                .withProperties(new ArrayList<>()).build();
    }
}
