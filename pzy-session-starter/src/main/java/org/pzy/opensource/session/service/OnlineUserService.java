package org.pzy.opensource.session.service;

/**
 * 在线用户服务
 *
 * @author pan
 * @date 2020-01-27
 */
public interface OnlineUserService {

    /**
     * 根据session id删除在线用户
     *
     * @param sessionId
     */
    void deleteBySessionId(String sessionId);

    /**
     * 根据session id踢出在线用户
     *
     * @param sessionId
     */
    void kickoutOnlineUserBySessionId(String sessionId);

}
