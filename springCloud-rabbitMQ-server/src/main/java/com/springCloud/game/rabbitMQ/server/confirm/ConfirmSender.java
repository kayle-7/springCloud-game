package com.springCloud.game.rabbitMQ.server.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.springCloud.game.rabbitMQ.server.config.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConfirmSender {
    private static String QUEUE_NAME = "test_queue_confirm";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        try {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String msg = "RabbitMQ confirm!";
            //生产者调用 ConfirmSelect 将channel设置为confirm模式
            channel.confirmSelect();
            for (int i = 0; i < 10; i++) {
                channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
                System.out.println("RabbitMQ send confirm msg : " + msg);
            }
            int i = 1/0;
            //确认    失败为验证
            if (channel.waitForConfirms()) {
                System.out.println("RabbitMQ send confirm succeed");
            } else {
                System.out.println("RabbitMQ send confirm failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionUtil.close(connection, channel);
        }
    }
}
