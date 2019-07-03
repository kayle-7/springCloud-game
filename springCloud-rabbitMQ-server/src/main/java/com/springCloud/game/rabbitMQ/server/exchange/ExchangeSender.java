package com.springCloud.game.rabbitMQ.server.exchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.springCloud.game.rabbitMQ.server.config.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ExchangeSender {

    private static String EXCHANGE_NAME = "test_exchange_fanout";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        try {
            channel.basicQos(1);
            // 声明exchange 交换机 转发器
            // fanout   不处理路由键
            // direct   处理路由键
            // topic    将路由键和某模式进行匹配。
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");//fanout 分裂
            String msg = "RabbitMQ exchange!";
            System.out.println("RabbitMQ send exchange msg : " + msg);
            channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionUtil.close(connection, channel);
        }
    }
}
