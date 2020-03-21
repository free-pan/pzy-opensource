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

package org.pzy.opensource.security.shiro.realm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.pzy.opensource.security.domain.bo.ShiroUserBO;
import org.pzy.opensource.security.service.ShiroWinterUserService;
import org.pzy.opensource.security.shiro.exception.ExpiredAccountException;

/**
 * 实现认证和授权. 该模板类实现了认证方法
 *
 * @author 潘志勇
 * @date 2019-02-06
 */
@Slf4j
public class WinterRealmTemplate extends AbstractWinterRealm {

    private static final String REALM_NAME = "winter-realm";

    /**
     * 构造方法
     *
     * @param shiroWinterUserService 该实例用于实现根据登录信息查找用户信息,以及根据ShiroUserBO对象查找查找权限信息
     * @param tokenClass             该Realm适用于哪类Token
     */
    public WinterRealmTemplate(ShiroWinterUserService shiroWinterUserService, Class<? extends AuthenticationToken> tokenClass) {
        super(shiroWinterUserService);
        super.setName(REALM_NAME);
        super.setAuthenticationTokenClass(tokenClass);
    }

    /**
     * 用于认证(登录时会被调用)
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.debug("使用 {} 进行身份认证...", this.getClass().getName());
        String username = (String) token.getPrincipal();
        ShiroUserBO shiroUserBO = super.getShiroWinterUserService().loadUserInfoByUsername(username);
        if (null == shiroUserBO) {
            throw new UnknownAccountException("账号不存在(用户名或密码错误)!");
        }
        if (!shiroUserBO.getAccountNonExpired()) {
            throw new ExpiredAccountException("账号已过期!");
        }
        if (!shiroUserBO.getAccountNonLocked()) {
            throw new LockedAccountException("账号已被锁定!");
        }
        if (!shiroUserBO.getCredentialsNonExpired()) {
            throw new ExpiredCredentialsException("密码已过期!");
        }
        if (!shiroUserBO.getEnabled()) {
            throw new DisabledAccountException("账号被删除或禁用");
        }
        return new SimpleAuthenticationInfo(shiroUserBO, shiroUserBO.getPassword(), REALM_NAME);
    }
}
