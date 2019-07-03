package com.springCloud.game.rabbitMQ.server.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.springCloud.game.rabbitMQ.server.config.ConnectionUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

public class ConfirmAsynSender {
    private static String QUEUE_NAME = "test_queue_confirm";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        try {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //生产者调用 ConfirmSelect 将channel设置为confirm模式
            channel.confirmSelect();

            //未确认的消息标识
            SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<>());
            //通道加监听
            channel.addConfirmListener(new ConfirmListener() {
                //有问题handleNack
                @Override
                public void handleAck(long l, boolean b) {
                    if (b) {
                        System.out.println("---handleNack------mutiple");
                        confirmSet.headSet(l + 1).clear();
                    } else {
                        System.out.println("---handleNack------multiple false");
                        confirmSet.remove(l);
                    }
                }

                //没有问题的handleAck
                @Override
                public void handleNack(long l, boolean b) {
                    if (b) {
                        System.out.println("---handleAck------multiple");
                        confirmSet.headSet(l + 1).clear();
                    } else {
                        System.out.println("---handleAck------multiple");
                        confirmSet.remove(l);
                    }
                }
            });

            String msg = "RabbitMQ confirm!";

            while (true) {
                long seqNo = channel.getNextPublishSeqNo();
                channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
                confirmSet.add(seqNo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionUtil.close(connection, channel);
        }
    }
}
