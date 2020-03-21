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

package org.pzy.opensource.security.shiro.matcher;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

/**
 * @author 潘志勇
 * @date 2019-02-06
 */
public class WinterCredentialsMatcher implements CredentialsMatcher {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        // 获取用户提交的,未经过加密的密码
        String submittedPassword = getSubmittedPassword(token);
        // 获取数据库中存储的,已经过加密的密码
        String storedCredentials = getStoredPassword(info);

        if (null == submittedPassword) {
            return false;
        }

        return encoder.matches(submittedPassword, storedCredentials);
    }

    protected String getSubmittedPassword(AuthenticationToken token) {
        Object obj = token != null ? token.getCredentials() : null;
        if (obj != null) {
            if (obj instanceof char[]) {
                return new String((char[]) obj);
            }
            return obj.toString();
        }
        return null;
    }

    protected String getStoredPassword(AuthenticationInfo storedAccountInfo) {
        Object stored = storedAccountInfo != null ? storedAccountInfo.getCredentials() : null;
        //fix for https://issues.apache.org/jira/browse/SHIRO-363
        if (stored instanceof char[]) {
            stored = new String((char[]) stored);
        }
        if (null != stored) {
            return stored.toString();
        }
        return null;
    }

}
