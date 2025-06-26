package com.flypiggyyoyoyo.demo.conf;

import com.flypiggyyoyoyo.demo.utils.AuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilterRegistration() {
        FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new AuthFilter());
        registration.addUrlPatterns("/*"); // 拦截所有请求
        registration.setName("authFilter");
        registration.setOrder(1); // 设置过滤器顺序
        return registration;
    }
}