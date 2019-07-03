package com.springboot.test.controller;

import com.springboot.test.entity.StudentEntity;
import com.springboot.test.server.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "Swagger控制器", tags = "学生控制器")//接口描述
@RestController
public class StudentController {

    @Autowired
    StudentService studentService;

    @PostMapping("/getStudentList")
    public List<StudentEntity> getStudentList(@ApiParam(name = "student", value = "student对象，json格式", required = true) StudentEntity student) {
        System.out.println(student.getName());
        return studentService.getStudentList();
    }

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
