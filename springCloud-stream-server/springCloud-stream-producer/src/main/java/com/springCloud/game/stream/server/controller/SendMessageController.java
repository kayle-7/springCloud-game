package com.springCloud.game.stream.server.controller;

import com.springCloud.game.stream.server.message.SendMessageInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class SendMessageController {

//    @Autowired
//    private SendMessageInterface sendMessageInterface;
//
//    //生产者流程
//
//    //创建发送消息通道
//
//    //生产者投递消息
//    @RequestMapping("/sendMessage")
//    public String sendMessage() {
//        String msg = UUID.randomUUID().toString();
//        System.out.println("生产者发送内容msg:" + msg);
//        Message build = MessageBuilder.withPayload(msg.getBytes()).build();
//        sendMessageInterface.sendMsg().send(build);
//        return "success";
//    }

    //开启绑定（集合）
}
