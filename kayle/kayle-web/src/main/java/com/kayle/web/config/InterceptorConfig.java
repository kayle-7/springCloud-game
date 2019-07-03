package com.kayle.web.config;

import com.kayle.web.filter.Interceptor.SimpleInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author : zx
 * create at:  2019-03-22  02:28
 * @description:
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private SimpleInterceptor simpleInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 自定义拦截器，添加拦截路径和排除拦截路径
//        registry.addInterceptor(simpleInterceptor).addPathPatterns("/**");
    }

}