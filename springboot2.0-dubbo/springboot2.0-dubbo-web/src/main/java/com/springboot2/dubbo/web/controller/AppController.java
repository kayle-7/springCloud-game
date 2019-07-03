package com.springboot2.dubbo.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.springboot2.dubbo.api.app.AppServer;
import com.springboot2.dubbo.api.entity.User;
import com.springboot2.dubbo.api.server.DubboService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : zx
 * create at:  2019-03-22  19:50
 * @description:
 */
@RestController
public class AppController {

    @Reference
    AppServer appServer;

    @Reference
    DubboService dubboService;

    @RequestMapping("/getUser")
    public List<User> getUser() {
        return appServer.getUser();
    }

    @RequestMapping("/insertUser")
    public String insertUser() {
        User user = new User();
        user.setUserId(1);
        user.setUserName("nimo");
        user.setPassword("nimo");
        int i = appServer.insertUser(user);
        return "success";
    }

}
