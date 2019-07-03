package com.springCloud.game.stream.server;

import com.springCloud.game.stream.server.message.SendMessageInterface;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

//生产者
@SpringBootApplication
@EnableBinding(SendMessageInterface.class)
public class StreamProducerServer {

    public static void main(String[] args) {
        SpringApplication.run(StreamProducerServer.class, args);
    }

    //默认以通道名称创建交换机，消费者启动的时候随机创建一个队列名称

}
