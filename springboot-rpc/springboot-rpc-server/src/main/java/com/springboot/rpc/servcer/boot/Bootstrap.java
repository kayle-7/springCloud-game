package com.springboot.rpc.servcer.boot;

import com.springboot.rpc.api.UserService;
import com.springboot.rpc.servcer.config.RpcProxyServer;
import com.springboot.rpc.servcer.impl.UserServiceImpl;

public class Bootstrap {

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        RpcProxyServer rpcProxyServer = new RpcProxyServer();
        rpcProxyServer.publisher(userService, 9202);
    }
}
