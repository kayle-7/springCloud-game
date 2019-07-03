package com.springboot.auth.entity;

import lombok.Data;

/**
 * @author : zx
 * create at:  2019-03-22  01:16
 * @description:
 */
@Data
public class User {

    private long id;
    private long userId;
    private String userName;
    private String password;

}
