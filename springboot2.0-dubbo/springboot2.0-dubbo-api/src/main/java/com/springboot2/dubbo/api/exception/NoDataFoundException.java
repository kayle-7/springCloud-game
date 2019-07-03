package com.springboot2.dubbo.api.exception;

/**
 * Created by zx
 */
public class NoDataFoundException extends GlobalException {
    public NoDataFoundException() {
    }

    public NoDataFoundException(String message) {
        super(404, message);
    }
}
