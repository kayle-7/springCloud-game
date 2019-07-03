package com.springCloud.game.rabbitMQ.server.spring;

public class MyConsumer {
    public void listen(String foo) {
        System.out.println("spring consumer : " + foo);
    }
}
