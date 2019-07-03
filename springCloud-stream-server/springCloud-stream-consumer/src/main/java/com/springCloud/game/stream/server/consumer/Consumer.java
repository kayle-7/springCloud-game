package com.springCloud.game.stream.server.consumer;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @StreamListener("my_stream_channel")
    private void redMsg(String msg) {
        System.out.println("消费者获取到生产者投递的消息" + msg);
    }
}
