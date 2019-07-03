package com.springboot2.dubbo.server.app.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.springboot2.dubbo.api.server.DubboService;
import org.springframework.beans.factory.annotation.Value;

/**
* @author   zx
* */
@Service
public class DubboServiceImpl implements DubboService {

    @Value("${server.port}")
    String port;

    /**
    * @author zx
    * getData
    * */
    @Override
    public String getData(String data) {
        System.out.println("This is springboot2.0 dubbo; port : " + port);
        return "This is springboot2.0 dubbo; data : " + data;
    }
}
