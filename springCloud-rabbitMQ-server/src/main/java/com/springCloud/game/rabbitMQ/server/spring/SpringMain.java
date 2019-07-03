package com.springCloud.game.rabbitMQ.server.spring;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringMain {

    public static void main(final String... args) throws InterruptedException {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("classpath*:context.xml");

        //RabbitMQ模板
        RabbitTemplate template = context.getBean(RabbitTemplate.class);
        //发消息
        template.convertAndSend("Hello, spring rabbitMQ");
        Thread.sleep(1000);
        context.destroy();
    }
}
