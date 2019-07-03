package com.springCloud.game.rabbitMQ.server.config;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

public class ConnectionUtil {
    public static Connection getConnection() throws IOException, TimeoutException {
        //定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务地址
        factory.setHost("localhost");
        //端口
        factory.setPort(5672);//amqp协议 端口 类似与mysql的3306
        //设置账号信息，用户名、密码、vhost
        factory.setVirtualHost("/vhost_game");
        factory.setUsername("game");
        factory.setPassword("game");
        // 通过工程获取连接
        return factory.newConnection();
    }

    public static void close(Connection connection, Channel channel) {
        try {
            if (channel != null) {
                channel.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
