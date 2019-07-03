package com.springCloud.game.stream.server.message;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

//创建发送消息通道
public interface SendMessageInterface {

    //创建发送消息通道
    @Output("my_stream_channel")
    SubscribableChannel sendMsg();
}
