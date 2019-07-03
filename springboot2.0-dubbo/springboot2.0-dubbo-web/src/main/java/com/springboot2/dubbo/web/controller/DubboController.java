package com.springboot2.dubbo.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.springboot2.dubbo.api.server.DubboService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DubboController {

    @Reference
    DubboService dubboService;

    @RequestMapping("/getData")
    public String getData(String data) {
        System.out.println("This is springboot2.0 dubbo web; data : " + data);
        String result = dubboService.getData(data);
        return result;
    }
}
