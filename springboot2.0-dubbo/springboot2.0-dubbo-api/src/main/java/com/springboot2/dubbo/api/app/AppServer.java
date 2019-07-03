package com.springboot2.dubbo.api.app;

import com.springboot2.dubbo.api.entity.User;

import java.util.List;

public interface AppServer {

    List<User> getUser();
    int insertUser(User user);

}
