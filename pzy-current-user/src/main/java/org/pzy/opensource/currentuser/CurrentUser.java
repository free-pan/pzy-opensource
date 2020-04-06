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

package org.pzy.opensource.currentuser;

import java.util.List;
import java.util.Optional;

/**
 * 当前用户
 *
 * @author pan
 * @date 2019-12-11
 */
public class CurrentUser {

    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户账号
     */
    private String account;
    /**
     * 用户昵称
     */
    private String nikeName;
    /**
     * 用户真实姓名
     */
    private String realName;
    /**
     * 用户角色列表
     */
    private List<String> roleList;
    /**
     * 用户拥有的权限标识列表
     */
    private List<String> permissionFlagList;
    /**
     * 用户可访问的uri列表
     */
    private List<String> permissionUriList;

    private CurrentUser() {
    }

    public static final CurrentUser DEFAULT_USER = new CurrentUser();

    /**
     * @param id                 用户id
     * @param account            用户账号
     * @param nikeName           用户昵称
     * @param realName           用户真实姓名
     * @param roleList           用户角色列表
     * @param permissionFlagList 用户权限标识列表
     * @param permissionUriList  用户可访问uri列表
     */
    public void init(Long id, String account, String nikeName, String realName, List<String> roleList, List<String> permissionFlagList, List<String> permissionUriList) {
        this.id = id;
        this.account = account;
        this.nikeName = nikeName;
        this.realName = realName;
        this.roleList = roleList;
        this.permissionFlagList = permissionFlagList;
        this.permissionUriList = permissionUriList;
    }

    /**
     * 是否包含指定功能权限
     *
     * @param permissionFlag 权限标识
     * @return true包含
     */
    public boolean hasPermission(String permissionFlag) {
        if (this.permissionFlagList == null || this.permissionFlagList.isEmpty()) {
            return false;
        } else {
            return this.permissionFlagList.contains(permissionFlag.toUpperCase());
        }
    }

    /**
     * 是否包含指定角色
     *
     * @param role 角色标识
     * @return true包含
     */
    public boolean hasRole(String role) {
        if (null == this.roleList || this.roleList.isEmpty()) {
            return false;
        } else {
            return this.roleList.contains(role.toUpperCase());
        }
    }

    /**
     * 获取当前用户id
     *
     * @return
     */
    public Optional<Long> getUserId() {
        return Optional.ofNullable(this.id);
    }

    /**
     * 获取当前用户真实姓名
     *
     * @return
     */
    public Optional<String> getRealName() {
        return Optional.ofNullable(this.realName);
    }

    /**
     * 获取当前用户昵称
     *
     * @return
     */
    public Optional<String> getNikeName() {
        return Optional.ofNullable(this.nikeName);
    }

}
