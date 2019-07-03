package com.springboot.feign.server.service;

import com.springboot.feign.api.FeignServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeignServiceImpl implements FeignServer {

    @Value("${server.port}")
    String port;

    @Override
    @RequestMapping("/getFeignServer")
    public String getFeignServer() {
        String str = "this feign server, port : " + port;
        System.out.println(str);
        return str;
    }
}
