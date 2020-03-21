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

import org.pzy.opensource.redis.support.springboot.aop.WinterLockAop;

import java.lang.annotation.*;

/**
 * 锁切面实现详见 {@link WinterLockAop} 切面 Order值为: 99950
 *
 * @author pzy
 * @date 2019/1/10
 * @see WinterLockAop
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WinterLock {

    /**
     * 锁构建方式
     *
     * @return
     */
    LockBuilder lockBuilder();

    /**
     * 等待获取锁的时间. 单位: 秒  默认:3秒
     *
     * @return
     */
    long waitTime() default 3;

    /**
     * 锁占用时间. 单位:秒 默认15秒
     *
     * @return
     */
    long leaseTime() default 15;

}
