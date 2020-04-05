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

package org.pzy.opensource.domain;

/**
 * @author pan
 * @date 2019-12-07
 */
public interface GlobalConstant {

    /**
     * redirect跳转前缀
     */
    String REDIRECT = "redirect:";
    /**
     * forward跳转前缀
     */
    String FORWARD = "forward:";
    /**
     * 全局方法缓存前缀
     */
    String OPERATE_CACHE_PREFIX = "winter-operate-cache:";
    /**
     * 全局方法锁前缀
     */
    String OPERATE_LOCK_PREFIX = "winter-operate-lock:";
    /**
     * 日志切面执行优先级
     */
    int AOP_ORDER_LOG = 99800;
    /**
     * 缓存切面执行优先级
     */
    int AOP_ORDER_CACHE = 99850;
    /**
     * 方法参数验证切面执行优先级
     */
    int AOP_ORDER_METHOD_VALIDATE = 99900;
    /**
     * 锁切面执行优先级
     */
    int AOP_ORDER_LOCK = 99950;
    /**
     * 事务切面执行优先级
     */
    int AOP_ORDER_TRANSACTIONAL = 99999;

    String OPTIONS_METHOD = "OPTIONS";

    String DEFAULT_CHARSET = "UTF-8";
    /**
     * 日期模式字符串: yyyy-MM-dd
     */
    String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 日期时间字符串: yyyy-MM-dd HH:mm:ss
     */
    String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期时间字符串: yyyy-MM-dd HH:mm
     */
    String DATE_TIME_PATTERN_B = "yyyy-MM-dd HH:mm";

    /**
     * 日期时间字符串: yyyy-MM-dd HH:mm:00
     */
    String DATE_TIME_PATTERN_C = "yyyy-MM-dd HH:mm:00";

    /**
     * 日期时间字符串: yyyy-MM-dd HH:mm:59
     */
    String DATE_TIME_PATTERN_D = "yyyy-MM-dd HH:mm:59";

    /**
     * 日期时间字符串: yyyy-MM-dd 00:00:00
     */
    String DATE_TIME_PATTERN_E = "yyyy-MM-dd 00:00:00";

    /**
     * 日期时间字符串: yyyy-MM-dd 23:59:59
     */
    String DATE_TIME_PATTERN_F = "yyyy-MM-dd 23:59:59";

    /**
     * 日期时间字符串: yyyy-MM-dd 00:00
     */
    String DATE_TIME_PATTERN_G = "yyyy-MM-dd 00:00";

    /**
     * 日期时间字符串: yyyy-MM-dd 23:59
     */
    String DATE_TIME_PATTERN_H = "yyyy-MM-dd 23:59";

    /**
     * 默认分页大小
     */
    Long PAGE_SIZE = 10L;
    /**
     * 每页最大记录数
     */
    Long MAX_PAGE_SIZE = 200L;
    /**
     * 默认页号
     */
    Long PAGE_NUM = 1L;

    Long MAX_PAGE_NUM = 300L;

    /**
     * 逻辑删除标识: 已删除(不可用)
     */
    Short LOGIC_DEL_YES = 0;

    /**
     * 逻辑删除标识: 未删除(可用)
     */
    Short LOGIC_DEL_NO = 1;

    /**
     * 启用
     */
    Short ENABLE = 1;

    /**
     * 禁用
     */
    Short DISABLE = 0;

    /**
     * 已激活
     */
    Short ACTIVE = 1;

    /**
     * 未激活
     */
    Short NOT_ACTIVE = 0;

    /**
     * 0
     */
    Long ZERO = 0L;

    /**
     * 空字符串
     */
    String EMPTY_STRING = "";

    String DEFAULT_SPLIT_CHART = ";";
    /**
     * 存放于session的强制踢出标识,当session中存在这个值时,说明该session应该强制注销(适用于用于管理员进行无理由手动强制踢出的情况)
     */
    String SESSION_FORCE_LOGOUT_KEY = "SESSION_FORCE_LOGOUT";
    /**
     * 存放于session的强制踢出标识,当session中存在这个值时,说明该session应该强制注销(适用于同一个账户session达到最大数量的自动强制踢出)
     */
    String MAX_SESSION_FORCE_LOGOUT_KEY = "MAX_SESSION_FORCE_LOGOUT";

    /**
     * 默认时区: GMT是格林威治标准时间的英文缩写(Greenwich Mean Time). gmt+8 是格林威治时间+8小时. 中国所在时区就是gmt+8
     */
    String TIME_ZONE = "GMT+8";
}
