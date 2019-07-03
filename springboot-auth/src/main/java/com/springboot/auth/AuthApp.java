package com.springboot.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author : zx
 * create at:  2019-03-22  01:11
 * @description:
 */
@SpringBootApplication
@MapperScan("com.springboot.auth.dao")
@ServletComponentScan
public class AuthApp {

    public static void main(String[] args) {
        SpringApplication.run(AuthApp.class, args);
    }

}
