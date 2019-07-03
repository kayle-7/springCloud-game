package com.springboot.test.dao;

import com.springboot.test.entity.StudentEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentDao {

    @Insert("INSERT INTO student (`name`, sex, age) VALUES ('aa', '男', 25)")
    Integer insert();

    @Select("select id, name, sex, age from student")
    List<StudentEntity> getStudentList();

}
