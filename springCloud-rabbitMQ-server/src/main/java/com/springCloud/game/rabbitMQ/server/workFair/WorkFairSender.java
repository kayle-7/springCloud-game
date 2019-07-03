package com.springCloud.game.rabbitMQ.server.workFair;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.springCloud.game.rabbitMQ.server.config.ConnectionUtil;

public class WorkFairSender {

    private static String QUEUE_NAME = "test_work_queue";
    public static void main(String[] args) {
        Connection connection = null;
        Channel channel = null;
        try {
            connection = ConnectionUtil.getConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //每个消费者发送确认信号之前，消息队列不发送下一个消息过来，一次只处理一个消息，限制发给同一个消费者不得超过1条消息
            channel.basicQos(1);

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
