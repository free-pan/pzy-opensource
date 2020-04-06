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

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.pzy.opensource.security.domain.bo.PermissionInfoBO;
import org.pzy.opensource.security.domain.bo.SimpleShiroUserBO;
import org.pzy.opensource.security.service.ShiroWinterUserService;
import org.pzy.opensource.security.shiro.matcher.WinterCredentialsMatcher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 实现认证和授权. 该抽象类实现了鉴权方法以及将加密算法修改为:org.pzy.opensource.security.shiro.matcher.WinterCredentialsMatcher
 *
 * @author 潘志勇
 * @date 2019-02-07
 */
@Getter
@Slf4j
public abstract class AbstractWinterRealm extends AuthorizingRealm {

    private ShiroWinterUserService shiroWinterUserService;

    private static WinterCredentialsMatcher WINTER_CREDENTIALS_MATCHER = new WinterCredentialsMatcher();

    public AbstractWinterRealm(ShiroWinterUserService shiroWinterUserService) {
        super();
        this.shiroWinterUserService = shiroWinterUserService;
        // 设置密码加密算法
        super.setCredentialsMatcher(WINTER_CREDENTIALS_MATCHER);
    }

    /**
     * 鉴权时调用(判断用户是否拥有指定角色或指定权限时调用)
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.debug("使用 {} 进行鉴权...", this.getName());
        SimpleShiroUserBO shiroUserBO = (SimpleShiroUserBO) principals.getPrimaryPrincipal();
        // 通过唯一标识获取角色集合
        List<String> roleList = shiroWinterUserService.loadRoleByUsername(shiroUserBO);
        // 通过唯一标识获取权限集合
        List<PermissionInfoBO> permissionInfoList = shiroWinterUserService.loadPermissionByUsername(shiroUserBO);
        Set<String> permissionSet = convert(permissionInfoList);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(new HashSet<>(roleList));
        simpleAuthorizationInfo.setStringPermissions(permissionSet);
        return simpleAuthorizationInfo;
    }

    private Set<String> convert(List<PermissionInfoBO> permissionInfoList) {
        Set<String> permissionSet = new HashSet<>(permissionInfoList.size());
        for (PermissionInfoBO permissionInfoBO : permissionInfoList) {
            permissionSet.add(permissionInfoBO.getFlag());
        }
        return permissionSet;
    }
}
