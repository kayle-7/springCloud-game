package com.springCloud.game.stream.server;

import com.springCloud.game.stream.server.message.RedMessageInterface;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

//消费者
@SpringBootApplication
@EnableBinding(RedMessageInterface.class)
public class StreamConsumerServer {

    public static void main(String[] args) {
        SpringApplication.run(StreamConsumerServer.class, args);
    }

    //消费者队列底层自动创建一个队列绑定my_stream_channel

}
