package com.springboot2.dubbo.api.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author : zx
 * create at:  2019-03-22  01:17
 * @description:
 */
@Data
public class Token {

    private long id;
    private long tokenId;
    private long userId;
    private String token;
    private Date buildTime;

}
