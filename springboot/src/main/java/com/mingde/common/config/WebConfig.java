package com.mingde.common.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private JWTInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/login", "/register")
                .excludePathPatterns("/files/download/**")
                .excludePathPatterns("/alipay/notify")
                .excludePathPatterns("/notice/selectAll", "/notice/selectById/**")
                .excludePathPatterns("/article/selectById/**", "/article/selectAll", "/article/selectRecommend", "/article/selectPage", "/article/updateReadCount/**")
                .excludePathPatterns("/routes/selectById/**", "/routes/selectAll", "/routes/selectPage")
                .excludePathPatterns("/tourism/selectById/**", "/tourism/selectAll", "/tourism/selectPage")
                .excludePathPatterns("/travels/selectById/**", "/travels/selectFrontPage", "/travels/updateReadCount/**")
                .excludePathPatterns("/comment/selectCount/**", "/comment/selectTree");
    }
}
