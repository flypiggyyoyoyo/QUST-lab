package com.flypiggyyoyoyo.demo.utils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebFilter(filterName = "AuthFilter",
//        urlPatterns = "/*") // 拦截所有请求
public class AuthFilter implements Filter {
    // 公开路径列表（不需要登录的路径）
    private static final String[] PUBLIC_PATHS = {
            "/demo/login",       // 登录页面
            "/login",            // 登录处理接口
            "/api/captcha",      // 验证码接口

            "/static/",          // 静态资源根目录
            "/css/",             // CSS资源
            "/js/",              // JS资源
            "/images/",          // 图片资源
            "/fonts/" ,           // 字体资源

            // 登录后跳转的框架页面
            "/top.html",
            "/left.html",
            "/index.html",

    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());

        // 检查是否为公开路径
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        // 检查登录状态
        Object user = httpRequest.getSession().getAttribute("user");
        if (user != null) {
            chain.doFilter(request, response);
        } else {
            // 未登录，重定向到登录页面
            httpResponse.sendRedirect(contextPath + "/demo/login");
        }
    }

    // 判断是否为公开路径（支持前缀匹配和精确匹配）
    private boolean isPublicPath(String path) {
        for (String publicPath : PUBLIC_PATHS) {
            if (path.equals(publicPath) || path.startsWith(publicPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}