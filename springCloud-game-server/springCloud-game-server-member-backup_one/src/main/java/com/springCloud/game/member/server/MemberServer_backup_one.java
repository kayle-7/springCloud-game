package com.springCloud.game.member.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class MemberServer_backup_one {

    public static void main(String[] args) {
        SpringApplication.run(MemberServer_backup_one.class, args);
    }

}
