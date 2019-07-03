package com.springCloud.game.rabbitMQ.server.tx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.springCloud.game.rabbitMQ.server.config.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class txSender {

    private static String QUEUE_NAME = "test_queue_tx";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        try {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String msg = "RabbitMQ tx!";
            channel.txSelect();
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            System.out.println("RabbitMQ send tx msg : " + msg);
            int i = 1/0;
            channel.txCommit();
            //此种模式还是很耗时的，降低了 Rabbitmq 的消息吞吐量
        } catch (Exception e) {
            e.printStackTrace();
            channel.txRollback();
        } finally {
            ConnectionUtil.close(connection, channel);
        }
    }
}
