// src/main/java/com/flypiggyyoyoyo/backend/interceptor/JwtInterceptor.java
package com.flypiggyyoyoyo.backend.interceptor;

import com.flypiggyyoyoyo.backend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1. 从请求头获取Token
        String token = request.getHeader("Authorization");

        // 2. 检查Token格式
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 3. 提取Token
        token = token.substring(7);

        // 4. 验证Token有效性
        if (!jwtUtils.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 5. 将用户ID存入请求属性
        Integer userId = jwtUtils.getUserIdFromToken(token);
        request.setAttribute("userId", userId);

        return true;
    }
}