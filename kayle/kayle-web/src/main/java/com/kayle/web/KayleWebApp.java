package com.kayle.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author : zx
 * create at:  2019-03-23  01:35
 * @description:
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.kayle.web.controller.*.contract")
@EnableHystrix
@ServletComponentScan
public class KayleWebApp {
    public static void main(String[] args) {
        SpringApplication.run(KayleWebApp.class, args);
    }
}
