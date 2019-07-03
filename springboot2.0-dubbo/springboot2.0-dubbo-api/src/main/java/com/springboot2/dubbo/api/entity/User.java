package com.springboot2.dubbo.api.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : zx
 * create at:  2019-03-22  01:16
 * @description:
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private long userId;
    private String userName;
    private String password;

}
