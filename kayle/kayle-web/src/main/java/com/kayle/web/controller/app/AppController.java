package com.kayle.web.controller.app;

import com.kayle.contract.ApiResult;
import com.kayle.contract.app.pojo.AppEntity;
import com.kayle.web.controller.app.contract.AppApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : zx
 * create at:  2019-03-23  13:26
 * @description:
 */
@RestController
public class AppController {

    @Autowired
    AppApi appApi;

    @RequestMapping("/getApp")
    public ResponseEntity<ApiResult> getApp(@RequestBody AppEntity app) {
        ApiResult result = appApi.getApp(app);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
