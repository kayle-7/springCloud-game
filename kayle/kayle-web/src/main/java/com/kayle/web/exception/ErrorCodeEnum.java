package com.kayle.web.exception;

/**
 * Created by zx
 */
public enum ErrorCodeEnum {
    VERSION_LIMIT_ERROR_CODE(403,"配置项版本错误"),
    VALIDATE_ERROR_CODE(400, "验证失败"),
    SQL_UPDATE_ERROR_CODE(401, "数据库更新失败"),
    COMMON_ERROR_CODE(402, "服务失败");

    private int code;
    private String message;

    ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
