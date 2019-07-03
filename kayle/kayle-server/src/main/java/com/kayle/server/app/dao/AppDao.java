package com.kayle.server.app.dao;

import com.kayle.contract.app.pojo.AppEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AppDao {
    AppEntity getApp(@Param("app") AppEntity appEntity);
    int insertApp(AppEntity appEntity);
}
