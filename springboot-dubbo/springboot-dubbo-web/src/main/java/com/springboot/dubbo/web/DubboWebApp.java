package com.springboot.dubbo.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.springboot.dubbo.web")
public class DubboWebApp {

    public static void main(String[] args) {
        SpringApplication.run(DubboWebApp.class, args);
    }

}
