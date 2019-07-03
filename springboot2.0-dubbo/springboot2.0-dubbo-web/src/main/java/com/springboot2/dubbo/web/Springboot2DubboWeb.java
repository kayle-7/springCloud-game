package com.springboot2.dubbo.web;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class Springboot2DubboWeb {

    public static void main(String[] args) {
        SpringApplication.run(Springboot2DubboWeb.class, args);
    }

}
