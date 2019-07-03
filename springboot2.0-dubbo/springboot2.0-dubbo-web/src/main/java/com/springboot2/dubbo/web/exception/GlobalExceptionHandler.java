package com.springboot2.dubbo.web.exception;

import com.springboot2.dubbo.web.model.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zx
 */
@Slf4j
@Aspect
@ControllerAdvice(basePackages = "com.springboot.workflow.web.controller")
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(WebException.class)
    public ResponseEntity<ApiResult> webExceptionHandler(WebException ex) {
        if (ex.getResult() != null) {
            return new ResponseEntity<>(ex.getResult(), HttpStatus.OK);
        } else if (StringUtils.isBlank(ex.getMessage())) {
            return ApiResult.failInfo("操作异常");
        } else {
            return ApiResult.failInfo(ex.getMessage());
        }
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult> exceptionHandler(Exception ex) {
        WebException we = ValidationUtil.getExceptionMessage(ex);
        ValidationUtil.checkResolveException(we);
        return ApiResult.failInfo(we.getCode(), we.getMessage());
    }

}