package com.springCloud.game.stream.server.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface RedMessageInterface {

    @Input("my_stream_channel")
    SubscribableChannel redMsg();
}
