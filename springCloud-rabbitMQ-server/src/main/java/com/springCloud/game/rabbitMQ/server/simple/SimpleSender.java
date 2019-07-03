package com.springCloud.game.rabbitMQ.server.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.springCloud.game.rabbitMQ.server.config.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SimpleSender {
    private static final String QUEUE_NAME = "test_simple_queue";

    public static void main(String[] args) {
        Connection connection = null;
        Channel channel = null;
        try {
            //获取一个连接
            connection = ConnectionUtil.getConnection();
            //从连接中创建通道
            channel = connection.createChannel();
            boolean durable=false;
            boolean exclusive=false;
            boolean autoDelete=false;
            //创建队列 (声明) 因为我们要往队列里面发送消息,这时候就得知道往哪个队列中发送,就好比在哪个管子里面放水
            //如果能确定是哪一个队列 这边可以删掉
            channel.queueDeclare(QUEUE_NAME, durable, exclusive, autoDelete, null);
            String msg = "hello simple1!";
            //第一个参数是exchangeName(默认情况下代理服务器端是存在一个""名字的exchange的,
            //因此如果不创建exchange的话我们可以直接将该参数设置成"",如果创建了exchange的话
            //我们需要将该参数设置成创建的exchange的名字),第二个参数是路由键
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            System.out.println("--send msg : " + msg);
        } catch (Exception e) {
            e.printStackTrace();
            ConnectionUtil.close(connection, channel);
        }
    }
}
