package com.springCloud.game.order.server.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.spring4all.swagger.EnableSwagger2Doc;
import com.springCloud.game.order.server.api.MemberApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableSwagger2Doc//生成Swagger2Doc
@Api("order server api")
public class OrderController {

    @Autowired
    private MemberApi memberApi;

    @RequestMapping("/")
    public String getOrder() {
        String result = memberApi.getMember("22");
        return "this is order server! " + result;
    }

    @ApiOperation("get order swagger")
    @ApiImplicitParam(name = "orderName", value = "订单名称", required = true, type = "String")
    @PostMapping("/getOrderSwagger")
    public String getOrderSwagger(String orderName) {
        System.out.println("getOrderSwagger(), name : " + orderName);
        return "this is get order swagger! name : " + orderName;
    }

    @RequestMapping("/getOrder")
    public String getOrder(String name) {
        String result = memberApi.getMember(name);
        System.out.println("getMember currentThread : " + Thread.currentThread().getName());
        System.out.println("result : " + result);
        return result;
    }

    @RequestMapping("/getOrderTimeout")
    @HystrixCommand(defaultFallback = "getOrderHystrixAndReturn")
    public String getOrderTimeout(String name) {
        String result = memberApi.getMemberTimeout(name);
        System.out.println("result : " + result);
        return result;
    }

    @RequestMapping("/getOrderHystrix")
    @HystrixCommand(defaultFallback = "getOrderHystrixAndReturn")
    public String getOrderHystrix(String name) {
        System.out.println("getMemberHystrix currentThread : " + Thread.currentThread().getName());
        return memberApi.getMemberHystrix(name);
    }

    @RequestMapping("/getOrderHystrix_demo2")
    @HystrixCommand(defaultFallback = "getOrderHystrixAndReturn")
    public String getOrderHystrix_demo2(String name) {
        System.out.println("getMemberHystrix currentThread : " + Thread.currentThread().getName());
        return memberApi.getMemberHystrix(name);
    }

    public String getOrderHystrixAndReturn() {
        return "服务忙，请稍后重试！";
    }
}
