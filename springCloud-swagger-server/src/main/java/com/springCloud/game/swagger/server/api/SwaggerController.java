package com.springCloud.game.swagger.server.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("Swagger控制器")//接口描述
@RestController
public class SwaggerController {

    @ApiOperation("swagger演示接口")
    @GetMapping("/swaggerIndex")
    public String swaggerIndex() {
        return "this is swagger index";
    }

    @ApiOperation("get swagger")
    @ApiImplicitParam(name = "name", value = "名称", required = true, type = "String")
    @PostMapping("/getSwagger")
    public String getSwagger(String name) {
        System.out.println("getSwagger(), name : " + name);
        return "this is get swagger! name : " + name;
    }
}
