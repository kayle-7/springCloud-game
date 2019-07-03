package com.springboot2.dubbo.api.exception;

/**
 * Created by zx
 */
public class InvalidDataException extends GlobalException {
    public InvalidDataException() {
    }

    public InvalidDataException(String message) {
        super(400, message);
    }
}
