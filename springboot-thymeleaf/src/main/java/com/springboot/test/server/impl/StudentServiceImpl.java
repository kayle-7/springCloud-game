package com.springboot.test.server.impl;

import com.springboot.test.dao.StudentDao;
import com.springboot.test.entity.StudentEntity;
import com.springboot.test.server.StudentService;
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
