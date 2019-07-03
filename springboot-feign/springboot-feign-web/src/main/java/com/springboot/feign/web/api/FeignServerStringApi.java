package com.springboot.feign.web.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.InputStream;

@FeignClient(name = "springboot-feign-server", url = "http://localhost:7102")/*使用代理方式将url改为代理地址*/
public interface FeignServerStringApi {

    @RequestMapping("/getFeignServerUser")
    String getFeignServerUser();

    @RequestMapping("/getFeignServerUserList")
    String getFeignServerUserList();

    @RequestMapping("/getFeignServerUserList")
    InputStream getFeignServerUserListInputStream();

}
