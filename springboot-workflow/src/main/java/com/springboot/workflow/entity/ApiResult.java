package com.springboot.workflow.entity;

import com.springboot.workflow.util.LoggerUtil;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

/**
 * Created by zx
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApiResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success = false;

    private int code = 400;

    private String message = "";

    private Object data;


    private static final ApiResult succeed = new ApiResult();

    public static ApiResult success() {
        return succeed;
    }

    public static ApiResult success(String message) {
        return ApiResult.builder().success(true).code(200).message(message).build();
    }

    public static ApiResult success(Object data, String message) {
        ApiResult result = ApiResult.success(message);
        result.setCode(200);
        result.setData(data);
        return result;
    }

    public static ResponseEntity<ApiResult> successInfo(Class cla, String message) {
        ApiResult result = success(message);
        LoggerUtil.doInfo(result, cla);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public static ResponseEntity<ApiResult> successInfo(Class cla, Object data, String message) {
        ApiResult result = ApiResult.success(message);
        result.setCode(200);
        result.setData(data);
        LoggerUtil.doInfo(result, cla);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public static ResponseEntity<ApiResult> failInfo(Class cla, String message, String in) {
        LoggerUtil.doInfo(cla, message, in);
        return new ResponseEntity<>(ApiResult.builder().success(false).code(400).message(message).build(), HttpStatus.OK);
    }

    public static ApiResult fail(String message) {
        return ApiResult.builder().success(false).code(400).message(message).build();
    }

    public static ApiResult fail(int code, String message) {
        return ApiResult.builder().success(false).code(code).message(message).build();
    }

    public static ApiResult fail(int code, String message, Object data) {
        ApiResult result = ApiResult.fail(code, message);
        result.setData(data);
        return result;
    }
}
