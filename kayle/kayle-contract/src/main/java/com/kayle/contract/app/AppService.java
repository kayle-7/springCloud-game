package com.kayle.contract.app;

import com.kayle.contract.ApiResult;
import com.kayle.contract.app.pojo.AppEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

public interface AppService {

    @RequestMapping("/getApp")
    ApiResult getApp(@RequestBody AppEntity appEntity);

}
