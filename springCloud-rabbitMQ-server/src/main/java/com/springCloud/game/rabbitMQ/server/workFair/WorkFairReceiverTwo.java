package com.springCloud.game.rabbitMQ.server.workFair;

import com.rabbitmq.client.*;
import com.springCloud.game.rabbitMQ.server.config.ConnectionUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class WorkFairReceiverTwo {

    private static String QUEUE_NAME = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        try {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicQos(1);//保证一次只分发一个

            Consumer consumer = new DefaultConsumer(channel) {
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body, StandardCharsets.UTF_8);
                    System.out.println("[receive two] msg : " + msg);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //手动回执
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            };
            boolean autoAck = false; //手动确认消息
            channel.basicConsume(QUEUE_NAME, autoAck, consumer);
        } catch (Exception e) {
            e.printStackTrace();
            ConnectionUtil.close(connection, channel);
        }
    }
}