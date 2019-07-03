package com.springboot.workflow.web.dao;

import com.springboot.workflow.web.pojo.StudentEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : zx
 * create at:  2019-03-16  18:51
 * @description:
 */
@Mapper
public interface UserDao {

    List<StudentEntity> getStudentList(StudentEntity studentEntity);

    int insert(StudentEntity studentEntity);
}
