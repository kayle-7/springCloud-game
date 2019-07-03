package com.springboot2.dubbo.web.exception;

import com.springboot2.dubbo.web.model.ApiResult;

public class WebException extends RuntimeException{

    private int code = 400;
    private String message;
    private Throwable e;
    private ApiResult result;

    public WebException() {
    }

    public WebException(String message) {
        super(message);
        this.message = message;
    }

    public WebException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public WebException(ApiResult result) {
        super(result.getMessage());
        this.code = result.getCode();
        this.message = result.getMessage();
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getE() {
        return e;
    }

    public void setE(Throwable e) {
        this.e = e;
    }

    public ApiResult getResult() {
        return result;
    }

    public void setResult(ApiResult result) {
        this.result = result;
    }
}
