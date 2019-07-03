package com.springboot.workflow.web.controller;

import com.springboot.workflow.web.pojo.StudentEntity;
import com.springboot.workflow.web.server.UserService;
import com.springboot.workflow.entity.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : zx
 * create at:  2019-02-24  21:19
 * @description:
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getStudentList")
    public ResponseEntity<ApiResult> getStudentList(StudentEntity studentEntity) throws Exception{
        List<StudentEntity> list = userService.getStudentList(studentEntity);
        return ApiResult.successInfo(this.getClass(), list, "success");
    }

    @RequestMapping(value = "/inserStudent")
    public ResponseEntity<ApiResult> inserStudent(StudentEntity studentEntity) throws Exception{
        int i = userService.insertStudent(studentEntity);
        return ApiResult.successInfo(this.getClass(), i, "success");
    }

    @RequestMapping(value = "user")
    public String indexTest(Model model) throws Exception{
        /*User user = userServer.listUser("E12C8E9B70EE44D6B0A55A44ED81924B");
        model.addAttribute("user",user);
        try {
            userServer.insertUser();
        } catch (Exception e){
            e.printStackTrace();
        }*/
        return "layouts/default";
    }

}
