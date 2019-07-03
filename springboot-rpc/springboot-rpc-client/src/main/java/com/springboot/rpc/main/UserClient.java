package com.springboot.rpc.main;

import com.springboot.rpc.api.UserService;
import com.springboot.rpc.config.RpcProxyClient;
import com.springboot.rpc.dto.User;

public class UserClient {

    public static void main(String[] args) {
        //获得一个远程服务的代理
        RpcProxyClient rpcProxyClient = new RpcProxyClient();
        //目标服务、ip、端口号
        UserService userService = rpcProxyClient.clientProxy(UserService.class, "localhost", 9202);
        User user = new User();
        user.setName("Mic");
        String str = userService.saveUser(user);
        System.out.println(str);
    }
}
