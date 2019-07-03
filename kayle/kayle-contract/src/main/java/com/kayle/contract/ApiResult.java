package com.kayle.contract;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

/**
 * Created by zx
 */
@Data
public class ApiResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success = true;
    private int code = 200;
    private String message = "";
    private Object data;
    private String stackTrace;

    public ApiResult() {

    }

    public ApiResult(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public ApiResult(boolean success, int code, String message, String stackTrace) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.stackTrace = stackTrace;
    }

    public ApiResult(String message) {
        this.message = message;
    }

    public ApiResult(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public ApiResult(boolean success, int code, String message, Object data, String stackTrace) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.stackTrace = stackTrace;
    }

    public static ApiResult success() {
        return new ApiResult();
    }

    public static ApiResult success(String message) {
        return new ApiResult(message);
    }

    public static ApiResult success(String message, Object data) {
        return new ApiResult(message, data);
    }

    public static ApiResult fail(String message) {
        return new ApiResult(false, 400, message);
    }

    public static ApiResult fail(int code, String message) {
        return new ApiResult(false, code, message);
    }

    public static ApiResult fail(int code, String message, String stackTrace) {
        return new ApiResult(false, code, message, stackTrace);
    }

    public static ResponseEntity<ApiResult> successInfo(String message) {
        return new ResponseEntity<>(success(message), HttpStatus.OK);
    }

    public static ResponseEntity<ApiResult> successInfo(String message, Object data) {
        return new ResponseEntity<>(success(message, data), HttpStatus.OK);
    }

    public static ResponseEntity<ApiResult> failInfo(String message) {
        return new ResponseEntity<>(fail(message), HttpStatus.OK);
    }

    public static ResponseEntity<ApiResult> failInfo(int code, String message) {
        return new ResponseEntity<>(fail(code, message), HttpStatus.OK);
    }

    public static ResponseEntity<ApiResult> failInfo(int code, String message, String stackTrace) {
        return new ResponseEntity<>(fail(code, message, stackTrace), HttpStatus.OK);
    }

}
