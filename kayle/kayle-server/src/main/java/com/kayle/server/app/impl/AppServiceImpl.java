package com.kayle.server.app.impl;

import com.kayle.contract.ApiResult;
import com.kayle.contract.app.AppService;
import com.kayle.contract.app.pojo.AppEntity;
import com.kayle.server.app.dao.AppDao;
import com.kayle.server.util.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : zx
 * create at:  2019-03-23  13:59
 * @description:
 */
@RestController
public class AppServiceImpl implements AppService {
    @Autowired
    AppDao appDao;

    @Override
    @RequestMapping("/getApp")
    public ApiResult getApp(@RequestBody AppEntity appEntity) {
        AppEntity app = appDao.getApp(appEntity);
        return ApiResult.success("success", app);
    }
}
