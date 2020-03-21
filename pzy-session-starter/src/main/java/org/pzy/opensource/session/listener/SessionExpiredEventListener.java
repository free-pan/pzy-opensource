package org.pzy.opensource.session.listener;

import lombok.extern.slf4j.Slf4j;
import org.pzy.opensource.session.service.OnlineUserService;
import org.springframework.context.event.EventListener;
import org.springframework.session.Session;
import org.springframework.session.events.SessionDeletedEvent;
import org.springframework.session.events.SessionDestroyedEvent;
import org.springframework.session.events.SessionExpiredEvent;

/**
 * 监听spring的session事件
 *
 * @author pan
 * @date 2020-01-27
 */
@Slf4j
public class SessionExpiredEventListener {

    private OnlineUserService onlineUserService;

    public SessionExpiredEventListener(OnlineUserService onlineUserService) {
        this.onlineUserService = onlineUserService;
    }

    /**
     * session过期事件监听
     *
     * @param event
     */
    @EventListener(SessionExpiredEvent.class)
    public void onSessionExpiredEvent(SessionExpiredEvent event) {
        Session session = event.getSession();
        this.clearRelationData(session);
    }

    /**
     * session销毁事件监听
     *
     * @param event
     */
    @EventListener(SessionDeletedEvent.class)
    public void onSessionDeletedEvent(SessionDeletedEvent event) {
        Session session = event.getSession();
        this.clearRelationData(session);
    }

    /**
     * session销毁事件监听
     *
     * @param event
     */
    @EventListener(SessionDestroyedEvent.class)
    public void onSessionDestroyedEvent(SessionDestroyedEvent event) {
        Session session = event.getSession();
        this.clearRelationData(session);
    }

    private void clearRelationData(Session session) {
        StringBuilder stringBuilder = new StringBuilder("监听到session销毁");
        if (null != session) {
            String sessionId = session.getId();
            stringBuilder.append("session id为:[" + sessionId + "]");
            onlineUserService.deleteBySessionId(sessionId);
        }
        log.debug(stringBuilder.toString());
    }
}
