package com.springboot.test.server;

import com.springboot.test.entity.StudentEntity;

import java.util.List;

public interface StudentService {

    List<StudentEntity> getStudentList();
}
