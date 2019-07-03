package com.springCloud.game.rabbitMQ.server.workFair;

import com.rabbitmq.client.*;
import com.springCloud.game.rabbitMQ.server.config.ConnectionUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class WorkFairReceiverOne {

    private static String QUEUE_NAME = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        final Channel channel = connection.createChannel();
        try {

            /*
            * ###消息持久化
            * 如果 RabbitMQ 服务器停止，把"队列"和"消息"设为持久化。
            * boolean durable = true;
            * channel.queueDeclare("test_queue_work", durable, false, false, null);
            * 注意：RabbitMQ 不允许使用不同的参数设定重新定义已经存在的队列
            * */
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicQos(1);//保证一次只分发一个

            final Consumer consumer = new DefaultConsumer(channel) {
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body, StandardCharsets.UTF_8);
                    System.out.println("[receive one] msg : " + msg);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //手动回执
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            };
            /*
            * ###消息应答
            * 消息应答是默认打开的。也就是 boolean autoAck =false;
            *
            * boolean autoAck = true;(自动确认模式)
            * 一旦 RabbitMQ 将消息分发给了费者，就会从内存中删除。将消息分发给了费者，就会从内存中删除。
            * boolean autoAck = false; (手动 确认模式 )
            * 我们不想丢失任何务，如果有一个消费者挂掉了那么我们应该将分发给它的任务交给另一个消费者去处理。
            * */
            boolean autoAck = false; //手动确认消息
            channel.basicConsume(QUEUE_NAME, autoAck, consumer);
        } catch (Exception e) {
            e.printStackTrace();
            ConnectionUtil.close(connection, channel);
        }
    }
}
