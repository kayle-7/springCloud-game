package com.springboot.rpc.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private int age;
    private String sex;

}
