package com.springCloud.game.rabbitMQ.server.topic;

import com.rabbitmq.client.*;
import com.springCloud.game.rabbitMQ.server.config.ConnectionUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class TopicReceiverTwo {

    private static String EXCHANGE_NAME = "test_exchange_topic";
    private static String QUEUE_NAME = "test_exchange_topic_queue_two";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        try {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicQos(1);
            //此时队列需要绑定要一个模式上。符号“#”匹配一个或多个词，符号“*”匹配一个词
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "good.#");

            Consumer consumer = new DefaultConsumer(channel) {
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body, StandardCharsets.UTF_8);
                    System.out.println("[exchange receive topic] msg : " + msg);
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            };
            channel.basicConsume(QUEUE_NAME, false, consumer);
        } catch (Exception e) {
            ConnectionUtil.close(connection, channel);
        }
    }

}
