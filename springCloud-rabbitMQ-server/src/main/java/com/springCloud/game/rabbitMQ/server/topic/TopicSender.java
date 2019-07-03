package com.springCloud.game.rabbitMQ.server.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.springCloud.game.rabbitMQ.server.config.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TopicSender {

    private static String EXCHANGE_NAME = "test_exchange_topic";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        try {
            // 声明exchange 交换机 转发器
            // fanout   不处理路由键
            // direct   处理路由键
            // topic    将路由键和某模式进行匹配。
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            channel.basicQos(1);
            String msg = "RabbitMQ exchange topic routing!";
            System.out.println("RabbitMQ send exchange direct msg : " + msg);
            //此时队列需要绑定要一个模式上。符号“#”匹配一个或多个词，符号“*”匹配一个词
            String routingKey = "good.add";
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, msg.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionUtil.close(connection, channel);
        }
    }
}
