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
 * @author pan
 * @date 2019-12-11
 */
public class CurrentUserInfo {

    /**
     * 用户id
     */
    private static Long ID;
    /**
     * 用户账号
     */
    private static String ACCOUNT;
    /**
     * 用户昵称
     */
    private static String NIKE_NAME;
    /**
     * 用户真实姓名
     */
    private static String REAL_NAME;
    /**
     * 用户角色列表
     */
    private static List<String> ROLE_LIST;
    /**
     * 用户拥有的权限标识列表
     */
    private static List<String> PERMISSION_FLAG_LIST;
    /**
     * 用户可访问的uri列表
     */
    private static List<String> PERMISSION_URI_LIST;

    /**
     * @param id                 用户id
     * @param account            用户账号
     * @param nikeName           用户昵称
     * @param realName           用户真实姓名
     * @param roleList           用户角色列表
     * @param permissionFlagList 用户权限标识列表
     * @param permissionUriList  用户可访问uri列表
     */
    public static void init(Long id, String account, String nikeName, String realName, List<String> roleList, List<String> permissionFlagList, List<String> permissionUriList) {
        ID = id;
        ACCOUNT = account;
        NIKE_NAME = nikeName;
        REAL_NAME = realName;
        ROLE_LIST = roleList;
        PERMISSION_FLAG_LIST = permissionFlagList;
        PERMISSION_URI_LIST = permissionUriList;
    }

    /**
     * 是否包含指定功能权限
     *
     * @param permissionFlag 权限标识
     * @return true包含
     */
    public static boolean hasPermission(String permissionFlag) {
        if (PERMISSION_FLAG_LIST == null || PERMISSION_FLAG_LIST.isEmpty()) {
            return false;
        } else {
            return PERMISSION_FLAG_LIST.contains(permissionFlag.toUpperCase());
        }
    }

    /**
     * 是否包含指定角色
     *
     * @param role 角色标识
     * @return true包含
     */
    public static boolean hasRole(String role) {
        if (null == ROLE_LIST || ROLE_LIST.isEmpty()) {
            return false;
        } else {
            return ROLE_LIST.contains(role.toUpperCase());
        }
    }

    /**
     * 获取当前用户id
     *
     * @return
     */
    public static Optional<Long> getUserId() {
        return Optional.ofNullable(ID);
    }

    /**
     * 获取当前用户真实姓名
     *
     * @return
     */
    public static Optional<String> getRealName() {
        return Optional.ofNullable(REAL_NAME);
    }

    /**
     * 获取当前用户昵称
     *
     * @return
     */
    public static Optional<String> getNikeName() {
        return Optional.ofNullable(NIKE_NAME);
    }

}
