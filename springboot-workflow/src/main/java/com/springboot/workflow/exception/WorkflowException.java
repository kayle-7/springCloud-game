package com.springboot.workflow.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by zx
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class WorkflowException extends RuntimeException {

    private int code = 400;
    private String message;
    private Object data;
    private String stackMessage;

    public WorkflowException(String message) {
        super(message);
        this.message = message;
    }

    public WorkflowException(String message, Throwable ex) {
        super(message);
        this.message = message;
    }

    public WorkflowException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public WorkflowException(int code, String message, Object data) {
        super(message);
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public WorkflowException(int code, String message, Object data, String stackMessage) {
        super(message);
        this.code = code;
        this.message = message;
        this.data = data;
        this.stackMessage = stackMessage;
    }

}
