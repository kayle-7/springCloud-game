package com.springboot2.dubbo.server.app.dao;

import com.springboot2.dubbo.api.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {

    List<User> getUser();

    int insertUser(User user);

}
