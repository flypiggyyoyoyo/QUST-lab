package com.flypiggyyoyoyo.demo.listener;

import com.flypiggyyoyoyo.demo.model.OnlineUser;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@WebListener
public class OnlineUserListener implements HttpSessionListener, HttpSessionAttributeListener {
    // key: sessionId, value: OnlineUser
    private static final ConcurrentHashMap<String, OnlineUser> onlineUsers = new ConcurrentHashMap<>();

    /** 供 Controller 获取所有在线用户 **/
    public static Collection<OnlineUser> getOnlineUsers() {
        return onlineUsers.values();
    }

    /** 当 “user” 属性被添加，说明用户刚登录 **/
    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        if (!"user".equals(event.getName())) {
            return;
        }

        HttpSession session = event.getSession();

        // 1. 处理 userId（兼容 Integer/Long/Number）
        Object rawId = event.getValue();
        Long userId;
        if (rawId instanceof Number) {
            userId = ((Number) rawId).longValue();
        } else {
            System.err.printf("[Listener] attributeAdded: 无法识别的 user 类型: %s%n", rawId);
            return;
        }

        // 2. 取用户名、真实名、角色码
        Object lognameObj  = session.getAttribute("userName");
        Object realnameObj = session.getAttribute("userRealname");
        Object roleObj     = session.getAttribute("userRole");

        String userLogname  = lognameObj != null ? lognameObj.toString() : "";
        String userRealname = realnameObj != null ? realnameObj.toString() : "";
        // 直接使用数据库或登录时写入的角色码（"1","2","3"）
        String userRoleCode = roleObj != null ? roleObj.toString() : "";

        System.out.printf(
                "[Listener] attributeAdded: sessionId=%s, userId=%d, logname=%s, realname=%s, roleCode=%s%n",
                session.getId(), userId, userLogname, userRealname, userRoleCode);

        // 将角色码传给 OnlineUser，前端模板统一映射显示中文
        OnlineUser user = new OnlineUser(userId, userLogname, userRealname, userRoleCode, session);
        onlineUsers.put(session.getId(), user);
        System.out.printf("[Listener] current online count = %d%n", onlineUsers.size());
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        if ("user".equals(event.getName())) {
            System.out.printf("[Listener] attributeRemoved: sessionId=%s%n", event.getSession().getId());
            onlineUsers.remove(event.getSession().getId());
            System.out.printf("[Listener] current online count = %d%n", onlineUsers.size());
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.printf("[Listener] sessionDestroyed: sessionId=%s%n", se.getSession().getId());
        onlineUsers.remove(se.getSession().getId());
        System.out.printf("[Listener] current online count = %d%n", onlineUsers.size());
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        // 不关心替换逻辑，登录只走 attributeAdded
    }
}
