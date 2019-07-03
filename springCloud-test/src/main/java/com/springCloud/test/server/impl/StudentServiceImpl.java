package com.springCloud.test.server.impl;

import com.springCloud.test.dao.StudentDao;
import com.springCloud.test.entity.StudentEntity;
import com.springCloud.test.server.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentDao studentDao;

    @Transactional
    @Override
    public List<StudentEntity> getStudentList() {
        studentDao.insert();
        return studentDao.getStudentList();
    }
}
