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

package org.pzy.opensource.security.service;

import org.pzy.opensource.security.domain.bo.PermissionInfoBO;
import org.pzy.opensource.security.domain.bo.ShiroUserBO;
import org.pzy.opensource.security.domain.bo.SimpleShiroUserBO;

import java.util.List;

/**
 * 用于实现根据登录信息查找用户信息,以及根据ShiroUserBO对象查找查找权限信息
 *
 * @author 潘志勇
 * @date 2019-2-06
 */
public interface ShiroWinterUserService {

    /**
     * 根据用户登录时使用的唯一标识(用户唯一标识)获取用户信息
     *
     * @param username 用户登录时使用的唯一标识(如:账号,密码等)
     * @return 如果未找到匹配的数据则返回null
     */
    ShiroUserBO loadUserInfoByUsername(String username);

    /**
     * 获取用户拥有的角色
     *
     * @param shiroUser 用户信息
     * @return 如未找到匹配的数据则返回空集合
     */
    List<String> loadRoleByUsername(SimpleShiroUserBO shiroUser);

    /**
     * 获取用户拥有的权限
     *
     * @param shiroUser 用户信息
     * @return 如未找到匹配的数据则返回空集合
     */
    List<PermissionInfoBO> loadPermissionByUsername(SimpleShiroUserBO shiroUser);
}
