package com.springCloud.game.rabbitMQ.server.exchange;

import com.rabbitmq.client.*;
import com.springCloud.game.rabbitMQ.server.config.ConnectionUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ExchangeReceiverTwo {
    private static String QUEUE_NAME = "test_exchange_queue_sms";
    private static String EXCHANGE_NAME = "test_exchange_fanout";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        try {
            //声明队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicQos(1);
            //绑定交换机
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
            Consumer consumer = new DefaultConsumer(channel) {
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body, StandardCharsets.UTF_8);
                    System.out.println("[exchange receive one] msg : " + msg);
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
