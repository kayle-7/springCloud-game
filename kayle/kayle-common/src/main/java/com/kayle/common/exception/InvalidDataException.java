package com.kayle.common.exception;

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
