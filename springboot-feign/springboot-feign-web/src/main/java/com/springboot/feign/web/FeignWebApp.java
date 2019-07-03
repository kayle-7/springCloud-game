package com.springboot.feign.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.springboot.feign.web.api")
@SpringBootApplication
public class FeignWebApp {

    public static void main(String[] args) {
        SpringApplication.run(FeignWebApp.class, args);
    }

}
