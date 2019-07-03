package com.springCloud.game.rabbitMQ.server.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.springCloud.game.rabbitMQ.server.config.ConnectionUtil;

public class WorkSender {

    private static String QUEUE_NAME = "test_work_queue";
    public static void main(String[] args) {
        Connection connection = null;
        Channel channel = null;
        try {
            connection = ConnectionUtil.getConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            for (int i = 0; i < 50; i ++) {
                String msg = "work msg : " + i;
                System.out.println(msg);
                channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionUtil.close(connection, channel);
        }
    }
}
