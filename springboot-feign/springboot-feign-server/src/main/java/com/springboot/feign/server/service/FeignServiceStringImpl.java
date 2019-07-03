package com.springboot.feign.server.service;

import com.springboot.feign.api.FeignServerString;
import com.springboot.feign.api.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class FeignServiceStringImpl implements FeignServerString {

    @Value("${server.port}")
    String port;

    @Override
    @RequestMapping("/getFeignServerUser")
    public User getFeignServerUser() {
        System.out.println("this feign server, port : " + port);
        User user = new User(1, 12, "name");
        return user;
    }

    @Override
    @RequestMapping("/getFeignServerUserList")
    public List<User> getFeignServerUserList() {
        System.out.println("this feign server, port : " + port);
        List<User> users = Arrays.asList(new User(1, 10, "张三"), new User(2, 11, "李四"));
        return users;
    }
}
