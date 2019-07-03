//package com.kayle.zuul.interceptor;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//
///**
// * @author : zx
// * create at:  2019-03-24  20:47
// * @description:
// */
//@Slf4j
//@Configuration
//@EnableResourceServer
//public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
//
//
//    @Autowired
//    private CustomAuthEntryPoint customAuthEntryPoint;
//    @Autowired
//    private CustomAccessDeniedHandler customAccessDeniedHandler;
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http
//                .exceptionHandling().authenticationEntryPoint(customAuthEntryPoint)
//                .and().authorizeRequests()
//                .antMatchers("/oauth/remove_token").permitAll()
//                .anyRequest().authenticated();
//        ;
//    }
//
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        super.configure(resources);
//        resources.authenticationEntryPoint(customAuthEntryPoint).accessDeniedHandler(customAccessDeniedHandler);
//    }
//}