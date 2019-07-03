package com.springboot2.dubbo.server;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class Springboot2DubboServer {

    public static void main(String[] args) {
        SpringApplication.run(Springboot2DubboServer.class, args);
    }
}
