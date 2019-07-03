package com.springboot2.dubbo.server.app.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.springboot2.dubbo.api.app.AppServer;
import com.springboot2.dubbo.api.entity.User;
import com.springboot2.dubbo.server.app.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author : zx
 * create at:  2019-03-22  18:10
 * @description:
 */
@Service
public class AppServiceImpl implements AppServer {

    @Autowired
    private UserDao userDao;


    @Override
    public List<User> getUser() {
        return userDao.getUser();
    }

    @Override
    public int insertUser(User user) {
        return userDao.insertUser(user);
    }
}
