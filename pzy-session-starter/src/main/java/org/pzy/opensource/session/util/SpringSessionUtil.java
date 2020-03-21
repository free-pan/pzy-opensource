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

package org.pzy.opensource.session.util;

import lombok.extern.slf4j.Slf4j;
import org.pzy.opensource.domain.GlobalConstant;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Map;

/**
 * spring-session操作帮助类,主要用于实现向session中设置强制踢出标识
 *
 * @author pan
 * @date 2020-01-26
 */
@Slf4j
public class SpringSessionUtil implements ApplicationContextAware {

    public static SpringSessionUtil INSTANCE = new SpringSessionUtil();

    private SpringSessionUtil() {
    }

    /**
     * 使用一种便捷的方法来查找特定用户的所有会话.
     * 通过确保FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME使用用户名填充名称为的session属性来完成此操作。由于Spring Session不知道所使用的身份验证机制，因此您有责任确保填充属性
     */
    private static FindByIndexNameSessionRepository FINDBYINDEXNAMESESSIONREPOSITORY;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        FINDBYINDEXNAMESESSIONREPOSITORY = applicationContext.getBean(FindByIndexNameSessionRepository.class);
    }

    /**
     * 根据用户唯一标识查找当前用户的所有session
     *
     * @param userPrincipalName 登录用户的系统唯一标识(使用用户id最稳妥)
     * @return key为session id
     */
    public static Map<String, Session> findUserSessionByPrincipalName(String userPrincipalName) {
        return FINDBYINDEXNAMESESSIONREPOSITORY.findByPrincipalName(userPrincipalName);
    }

    /**
     * 判断当前账户的session数量是否超过系统允许的最大session数量
     *
     * @param maxSessionCount   系统允许的最大session数量
     * @param userPrincipalName 登录用户的系统唯一标识(使用用户id最稳妥)
     * @return true表示已经超过最大登录限制, false表示未超过
     */
    public static boolean isOverMaxSessionCount(int maxSessionCount, String userPrincipalName) {
        Map<String, Session> sessionMap = findUserSessionByPrincipalName(userPrincipalName);
        return !CollectionUtils.isEmpty(sessionMap) && sessionMap.size() >= maxSessionCount;
    }

    /**
     * 通过session id查找session
     *
     * @param sessionId
     * @return
     */
    public Session findByIdSessionId(String sessionId) {
        return FINDBYINDEXNAMESESSIONREPOSITORY.findById(sessionId);
    }

    /**
     * 通过session id删除session
     *
     * @param sessionId
     */
    public void deleteByIdSessionId(String sessionId) {
        FINDBYINDEXNAMESESSIONREPOSITORY.deleteById(sessionId);
    }

    /**
     * 强制踢出用户(用于同一个用户的session数量,超过系统最大限制时). 调用此方法, 需要先加上分布式锁
     *
     * @param userPrincipalName 登录用户的系统唯一标识(使用用户id最稳妥)
     */
    public static void forceLogoutFirst(String userPrincipalName) {
        Map<String, Session> sessionMap = findUserSessionByPrincipalName(userPrincipalName);
        if (!CollectionUtils.isEmpty(sessionMap)) {
            Collection<Session> sessionColl = sessionMap.values();
            Session session = findMinCreationTimeSession(sessionColl);
            if (null != session) {
                // 在session中设置强制踢出标识
                session.setAttribute(GlobalConstant.MAX_SESSION_FORCE_LOGOUT_KEY, true);
                // 将对session的更新保存到spring-session中
                FINDBYINDEXNAMESESSIONREPOSITORY.save(session);
                log.debug("执行 MAX_SESSION_FORCE_LOGOUT");
            }
        }
    }

    /**
     * 强制踢出用户(用于管理员强制踢出用户的情况)
     *
     * @param request
     */
    public static void forceLogout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (null != session) {
            session.setAttribute(GlobalConstant.SESSION_FORCE_LOGOUT_KEY, true);
            log.debug("执行 SESSION_FORCE_LOGOUT_KEY");
        }
    }

    /**
     * 保存当前登录用户的系统唯一标识(保存用户id最稳妥)
     *
     * @param httpServletRequest
     * @param userPrincipalName  登录用户的系统唯一标识(使用用户id最稳妥)
     */
    public static void saveLoginUserPrincipalName(HttpServletRequest httpServletRequest, String userPrincipalName) {
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, userPrincipalName);
    }

    /**
     * 查找创建时间最小的Session
     *
     * @param sessionColl
     * @return
     */
    private static Session findMinCreationTimeSession(Collection<Session> sessionColl) {
        Session minCreationTimeSession = null;
        for (Session session : sessionColl) {
            if (minCreationTimeSession == null) {
                minCreationTimeSession = session;
            } else if (session.getCreationTime().compareTo(minCreationTimeSession.getCreationTime()) < 0) {
                // session的创建时间小于minCreationTimeSession的时间,则当前session设置为minCreationTimeSession
                minCreationTimeSession = session;
            }
        }
        return minCreationTimeSession;
    }
}
