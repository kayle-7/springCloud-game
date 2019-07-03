package com.springboot.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RestWebController {

    @Value("${server.port}")
    String port;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/getData")
    public String getData() {
//        String url_application_name = "http://app-demo-data-server/getData";//需开@LoadBalanced
        String url = "http://localhost:7002/getRestData";//需关闭@LoadBalanced
        String result = restTemplate.getForObject(url, String.class);
        System.out.println("rest web to rest server, getData : " + result);
        return result;
    }

}
