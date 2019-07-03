package com.springCloud.game.rabbitMQ.server.simple;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.springCloud.game.rabbitMQ.server.config.ConnectionUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SimpleReceiver {

    private static final String QUEUE_NAME = "test_simple_queue";
    public static void main(String[] args) {
        Connection connection = null;
        Channel channel = null;
        try {
            //获取一个连接
            connection = ConnectionUtil.getConnection();
            channel = connection.createChannel();
            //声明队列 如果能确定是哪一个队列 这边可以删掉,不去掉 这里会忽略创建
            //channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            DefaultConsumer consumer = new DefaultConsumer(channel) {
                //获取到达的消息
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, StandardCharsets.UTF_8);
                    System.out.println(" [x] Received '" + message + "'");
                }
            };
            //监听队列
            channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (Exception e) {
            e.printStackTrace();
            ConnectionUtil.close(connection, channel);
        }
    }
}
