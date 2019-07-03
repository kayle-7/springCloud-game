package com.springboot.workflow.web.server.impl;

import com.springboot.workflow.web.dao.UserDao;
import com.springboot.workflow.web.pojo.StudentEntity;
import com.springboot.workflow.web.server.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : zx
 * create at:  2019-03-16  18:51
 * @description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public List<StudentEntity> getStudentList(StudentEntity studentEntity) {
        return userDao.getStudentList(studentEntity);
    }

    @Override
    public int insertStudent(StudentEntity studentEntity) {
        return userDao.insert(studentEntity);
    }

}
