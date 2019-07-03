package com.springboot.rpc.servcer.impl;

import com.springboot.rpc.api.UserService;
import com.springboot.rpc.dto.User;

public class UserServiceImpl implements UserService {


    @Override
    public String saveUser(User user) {
        System.out.println("request in : " + user.toString());
        return "save success";
    }

    @Override
    public User getUser(User user) {
        System.out.println("request in : " + user.toString());
        return user;
    }

}
