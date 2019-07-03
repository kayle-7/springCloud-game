package com.springboot.auth.config;

import com.springboot.auth.filter.GlobalFilter;
import com.springboot.auth.filter.TokenAuthorFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : zx
 * create at:  2019-03-22  01:55
 * @description:
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean globalFilterRegistration() {
        // 新建过滤器注册类
        FilterRegistrationBean registration = new FilterRegistrationBean();
        // 添加自定义 过滤器
        registration.setFilter(globalFilter());
        // 设置过滤器的URL模式
        registration.addUrlPatterns("/*");
        //设置过滤器顺序   order的数值越小，优先级越高    先进后出
        registration.setOrder(2);
        return registration;
    }

    @Bean
    public FilterRegistrationBean tokenAuthorFilterRegistration() {
        // 新建过滤器注册类
        FilterRegistrationBean registration = new FilterRegistrationBean();
        // 添加自定义 过滤器
        registration.setFilter(tokenAuthorFilter());
        // 设置过滤器的URL模式
        registration.addUrlPatterns("/*");
        //设置过滤器顺序   order的数值越小，优先级越高    先进后出
        registration.setOrder(2);
        return registration;
    }

    @Bean
    public GlobalFilter globalFilter() {
        return new GlobalFilter();
    }

    @Bean
    public TokenAuthorFilter tokenAuthorFilter() {
        return new TokenAuthorFilter();
    }

}
