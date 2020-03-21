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

package org.pzy.opensource.redis.support.springboot.annotation;

import java.lang.annotation.*;

/**
 * @author pan
 * @date 2019-06-28
 */
@Documented
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LockBuilder {
    /**
     * 如果condition为非空值, 则需要验证 condition 的执行结果是否为true, 只有执行结果为true会加锁. condition使用spring el引擎解析
     *
     * @return
     */
    String condition() default "";

    /**
     * key值是否使用spring el方式解析. 默认:true
     *
     * @return
     */
    boolean keyEl() default true;

    /**
     * 锁的前缀.
     *
     * @return
     */
    String prefix() default "";

    /**
     * 默认为"类全名.方法名#参数个数", 支持spring el表单时. 如果是spring el表达式只能返回字符串类型或Collection的子孙类类集合<br>
     * 如果返回的是java.util.Collection实例, 则会同时获取同类型的多把锁<br>
     * 如: org.pzy.HelloService.fun#3
     *
     * @return
     */
    String key() default "";
}
