package com.springboot.rpc.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    //类名称
    private String className;
    //方法名
    private String methodName;
    //请求参数
    private Object[] parameters;

}
