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

package org.pzy.opensource.security.shiro.filter;

import org.pzy.opensource.domain.GlobalConstant;

/**
 * 会话进行最大并发控制(超过最大会话,则会强制踢出第一个session)
 *
 * @author pan
 * @date 2020-01-26
 */
public class CrossKickoutSessionControlFilter extends AbstractCrossShiroForceLogoutFilterTemplate {

    /**
     * 构造函数
     *
     * @param forceLogoutRedirectUrl 用户被强制踢出之后转入的url
     */
    public CrossKickoutSessionControlFilter(String forceLogoutRedirectUrl) {
        super("crossKickout", GlobalConstant.MAX_SESSION_FORCE_LOGOUT_KEY, forceLogoutRedirectUrl);
    }

}
