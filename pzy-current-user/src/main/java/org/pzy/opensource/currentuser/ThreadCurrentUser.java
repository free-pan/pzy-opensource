/**
 * Copyright (C): 恒大集团版权所有 Evergrande Group
 */
package org.pzy.opensource.currentuser;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;

/**
 * ThreadCurrentUser
 *
 * @author pan
 * @date 2020/4/6 09:29
 */
@Slf4j
public class ThreadCurrentUser {
    public static final TransmittableThreadLocal<CurrentUser> CONTEXT = new TransmittableThreadLocal<>();

    /**
     * 设置当前用户
     *
     * @param currentUser
     */
    public static void set(CurrentUser currentUser) {
        if (null != currentUser) {
            CONTEXT.set(currentUser);
        }
    }

    /**
     * 从当前线程上下文中获取用户信息
     *
     * @return
     */
    public static CurrentUser get() {
        CurrentUser currentUser = CONTEXT.get();
        if (null == currentUser) {
            if (log.isWarnEnabled()) {
                log.warn("警告:当前用户为null,返回的是默认用户信息!");
            }
            currentUser = CurrentUser.DEFAULT_USER;
        }
        return currentUser;
    }

    /**
     * 从当前线程上下文中删除用户信息
     */
    public static void remove() {
        CONTEXT.remove();
    }

    /**
     * 是否包含指定功能权限
     *
     * @param permissionFlag 权限标识
     * @return true包含
     */
    public static boolean hasPermission(String permissionFlag) {
        return ThreadCurrentUser.get().hasPermission(permissionFlag);
    }

    /**
     * 是否包含指定角色
     *
     * @param role 角色标识
     * @return true包含
     */
    public static boolean hasRole(String role) {
        return ThreadCurrentUser.get().hasRole(role);
    }

    /**
     * 获取当前用户id
     *
     * @param defaultUserId 当用户id为null时, 默认返回的userId值
     * @return 用户id
     */
    public static Long getUserId(Long defaultUserId) {
        return ThreadCurrentUser.get().getUserId().orElse(defaultUserId);
    }

    /**
     * 获取当前用户真实姓名
     *
     * @param defaultRealName 当用户真实姓名为null时, 默认返回的真实姓名
     * @return 用户真实姓名
     */
    public static String getRealName(String defaultRealName) {
        return ThreadCurrentUser.get().getRealName().orElse(defaultRealName);
    }

    /**
     * 获取当前用户昵称
     *
     * @param defaultNikeName 当用户昵称为null时, 默认返回的昵称
     * @return 用户昵称
     */
    public static String getNikeName(String defaultNikeName) {
        return ThreadCurrentUser.get().getNikeName().orElse(defaultNikeName);
    }
}
