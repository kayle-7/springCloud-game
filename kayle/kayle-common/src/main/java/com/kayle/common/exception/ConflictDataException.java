package com.kayle.common.exception;

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
