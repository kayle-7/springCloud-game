package com.kayle.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author : zx
 * create at:  2019-03-23  01:36
 * @description:
 */
@SpringBootApplication
@EnableEurekaClient
public class KayleServerApp {
    public static void main(String[] args) {
        SpringApplication.run(KayleServerApp.class, args);
    }
}
