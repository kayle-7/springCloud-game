package com.springboot.feign.web.api;

import com.springboot.feign.api.FeignServer;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "springboot-feign-server", url = "http://localhost:7102")/*使用代理方式将url改为代理地址*/
public interface FeignServiceApi extends FeignServer {
}
