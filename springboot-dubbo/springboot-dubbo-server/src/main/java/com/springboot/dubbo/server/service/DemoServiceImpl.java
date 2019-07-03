package com.springboot.dubbo.server.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.springboot.dubbo.api.demo.service.DemoService;

@Service(version = "1.0", application = "${dubbo.application.id}", protocol = "${dubbo.protocol.id}", registry = "${dubbo.registry.id}")
public class DemoServiceImpl implements DemoService {

    @Override
    public String getData(String name) {
        System.out.println("**************************this is demoServiceImpl, name is " + name);
        return name;
    }

}
