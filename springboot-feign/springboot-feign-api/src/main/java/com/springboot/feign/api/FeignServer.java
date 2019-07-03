package com.springboot.feign.api;

import org.springframework.web.bind.annotation.RequestMapping;

public interface FeignServer {

    @RequestMapping("/getFeignServer")
    String getFeignServer();

}
