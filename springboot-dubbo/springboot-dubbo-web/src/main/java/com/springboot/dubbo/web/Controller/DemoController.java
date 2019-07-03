package com.springboot.dubbo.web.Controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.springboot.dubbo.api.demo.service.DemoService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Reference(version = "1.0", application = "")
    private DemoService demoServer;

    @RequestMapping("/getData/{name}")
    public String getData(@PathVariable String name) {
        String str = demoServer.getData(name);
        return str;
    }
}
