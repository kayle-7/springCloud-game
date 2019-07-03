package com.springboot2.dubbo.api.exception;

/**
 * Created by zx
 */
public class ConflictDataException extends GlobalException {
    public ConflictDataException() {
    }

    public ConflictDataException(String message) {
        super(409, message);
    }
}
