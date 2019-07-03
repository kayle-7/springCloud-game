package com.springboot.workflow.exception;

import com.springboot.workflow.entity.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zx
 */
@Slf4j
@ControllerAdvice(basePackages = "com.springboot.workflow.test.UserController")
public class GlobalExceptionHandler {

//    @ResponseBody
//    @ExceptionHandler(WorkflowException.class)
//    public ResponseEntity<ApiResult> workflowExceptionHandler(WorkflowException ex) {
//        return new ResponseEntity<>(ApiResult.fail(ex.getMessage()), HttpStatus.OK);
//    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult> exceptionHandler(Exception ex) {
        ResolveExceptionMessage rem = getExceptionMessage(ex);
        Throwable t = rem.getE();
        int code;
        String exceptionMessage;
        if (t instanceof WorkflowException) {
            code = ((WorkflowException)t).getCode();
            exceptionMessage = (StringUtils.isBlank(rem.getMessage()) ? "操作异常" : rem.getMessage());
//        } else if (t instanceof InvalidDataException){
//            code = 400;
        } else {
            code = 500;
            exceptionMessage = "操作异常";
        }

        ApiResult result = ApiResult.fail(code, exceptionMessage);
        log.warn(new StringBuffer().append("\n【异常日志】")
                .append("\n【错误信息】：").append(exceptionMessage)
                .append("\n【异常信息】：").append(t.toString())
                .append("\n【返回参数】：").append(result.toString()).toString(), ex);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * @description     获取异常信息
     * @param ex        Throwable
     */
    private static ResolveExceptionMessage getExceptionMessage(Throwable ex) {
        Throwable e = ex;
        StringBuilder sb = new StringBuilder();
        while( e != null) {
            if (ex.getMessage() != null ) {
                sb.append(ex.getMessage());
            }
            if (ex.getCause() != null) {
                ex = ex.getCause();
            }
            e = e.getCause();
        }
        ResolveExceptionMessage resolveExceptionMessage = new ResolveExceptionMessage();
        resolveExceptionMessage.setE(ex);
        resolveExceptionMessage.setMessage(sb.toString());
        return resolveExceptionMessage;
    }
}