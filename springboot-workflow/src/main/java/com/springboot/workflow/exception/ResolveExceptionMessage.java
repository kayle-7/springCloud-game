package com.springboot.workflow.exception;

import lombok.Data;
import lombok.ToString;

/**
 * Created by zx
 */
@Data
@ToString
public class ResolveExceptionMessage {
    private Throwable e;
    private String message;
}
