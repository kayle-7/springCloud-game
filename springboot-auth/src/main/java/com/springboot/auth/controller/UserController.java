package com.springboot.auth.controller;

import com.springboot.auth.entity.User;
import com.springboot.auth.server.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : zx
 * create at:  2019-03-22  01:34
 * @description:
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/getTest")
    public String getTest() {

        return "this is getTest";
    }

    @RequestMapping("/getUser")
    public User getUser() {

        return null;
    }
}
