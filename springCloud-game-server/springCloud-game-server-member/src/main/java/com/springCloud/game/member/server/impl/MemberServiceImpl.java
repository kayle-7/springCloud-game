package com.springCloud.game.member.server.impl;

import com.springCloud.game.member.api.memberApi.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberServiceImpl implements MemberService {

    @Value("${server.port}")
    private String port;

    @RequestMapping("/")
    public String getMember() {
        return "this is member server "  + port;
    }

    @RequestMapping("/getMember")
    public String getMember(String name) {
        System.out.println("member name : " + name);
        return "this is member server, get name : " + name + port;
    }

    @RequestMapping("/getMemberTimeout")
    public String getMemberTimeout(String name) {
        System.out.println("member name : " + name);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "this is member server, getMemberTimeout()";
    }

    @RequestMapping("/getMemberHystrix")
    public String getMemberHystrix(String name) {
        System.out.println("member name : " + name);
        return "this is member server, get name : " + name;
    }

}
