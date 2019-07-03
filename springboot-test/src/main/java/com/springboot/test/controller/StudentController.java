package com.springboot.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {

    @RequestMapping("/swaggerIndex")
    public String swaggerIndex() {
        return "this is swagger index";
    }

    @RequestMapping("/getSwagger")
    public String getSwagger(String name) {
        System.out.println("getSwagger(), name : " + name);
        return "this is get swagger! name : " + name;
    }

}
