package com.springCloud.test.server;

import com.springCloud.test.entity.StudentEntity;

import java.util.List;

public interface StudentService {

    List<StudentEntity> getStudentList();
}
