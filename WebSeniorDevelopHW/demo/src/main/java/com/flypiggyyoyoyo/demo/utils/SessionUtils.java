package com.flypiggyyoyoyo.demo.utils;

import javax.servlet.http.HttpSession;

public class SessionUtils {

    // 用户ID
    public static final String SESSION_USER_ID = "loginUser";
    // 用户名
    public static final String SESSION_USER_NAME = "userName";
    // 用户角色
    public static final String SESSION_USER_ROLE = "userRole";

    /**
     * 获取当前登录用户ID
     */
    public static Long getUserId(HttpSession session) {
        return (Long) session.getAttribute(SESSION_USER_ID);
    }

    /**
     * 获取当前登录用户名
     */
    public static String getUserName(HttpSession session) {
        return (String) session.getAttribute(SESSION_USER_NAME);
    }

    /**
     * 获取当前登录用户角色
     */
    public static Integer getRole(HttpSession session) {
        return (Integer) session.getAttribute(SESSION_USER_ROLE);
    }

    /**
     * 判断用户是否已登录
     */
    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(SESSION_USER_ID) != null;
    }
}