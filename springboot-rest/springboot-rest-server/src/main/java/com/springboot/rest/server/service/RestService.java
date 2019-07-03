package com.springboot.rest.server.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestService {

    @Value("${server.port}")
    String port;

    @RequestMapping("/getRestData")
    public String getRestData() {
        String str = "this is rest server, port : " + port;
        System.out.println(str);
        return str;
    }
}
