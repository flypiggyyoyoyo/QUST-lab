// src/main/java/com/flypiggyyoyoyo/backend/config/WebMvcConfig.java
package com.flypiggyyoyoyo.backend.config;

import com.flypiggyyoyoyo.backend.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")  // 需要认证的路径
                .excludePathPatterns(       // 排除不需要认证的路径
                        "/api/v1/user/login",
                        "/api/v1/user/register",
                        "/api/v1/public/**"
                );
    }
}