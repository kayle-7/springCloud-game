package com.springboot.feign.web.controller;

import com.springboot.feign.web.api.FeignServerStringApi;
import com.springboot.feign.web.api.FeignServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
public class FeignWebController {

    @Value("${server.port}")
    String port;

    @Autowired
    FeignServiceApi feignServiceApi;

    @Autowired
    FeignServerStringApi feignServerStringApi;

    @RequestMapping("/getFeignWeb")
    public String getFeignWeb() {
        String result = feignServiceApi.getFeignServer();
        System.out.println("feign web to feign server, result : " + result);
        return result;
    }

    @RequestMapping("/getString")
    public String getString() {
        String result1 = feignServerStringApi.getFeignServerUser();
        String result2 = feignServerStringApi.getFeignServerUserList();
        InputStream result3 = feignServerStringApi.getFeignServerUserListInputStream();
        return result1 + result2;
    }

}
