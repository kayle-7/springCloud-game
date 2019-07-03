package com.springboot.workflow.web.server;

import com.springboot.workflow.web.pojo.StudentEntity;

import java.util.List;

/**
 * @author : zx
 * create at:  2019-03-16  18:51
 * @description:
 */
public interface UserService {

    List<StudentEntity> getStudentList(StudentEntity studentEntity);

    int insertStudent(StudentEntity studentEntity);
}
