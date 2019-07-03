package com.springboot.workflow.util;

import com.springboot.workflow.entity.Operator;
import com.springboot.workflow.exception.WorkflowException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ValidationUtil {

    public static void checkBlank(String data, String message) {
        if (StringUtils.isBlank(data)) {
            throw new WorkflowException(message);
        }
    }

    public static void checkStringMin(String data, long min, String message) {
        if (StringUtils.isEmpty(data) || data.length() < min) {
            throw new WorkflowException(message);
        }
    }

    public static void checkStringMax(String data, long max, String message) {
        if (StringUtils.isEmpty(data) || data.length() > max) {
            throw new WorkflowException(message);
        }
    }

    public static void checkStringSize(String data, long min, long max, String message) {
        if (StringUtils.isEmpty(data) || data.length() < min || data.length() > max) {
            throw new WorkflowException(message);
        }
    }

    public static void checkNumberMin(long data, long min, String message) {
        if (data < min) {
            throw new WorkflowException(message);
        }
    }

    public static void checkNumberMax(long data, long max, String message) {
        if (data > max) {
            throw new WorkflowException(message);
        }
    }

    public static void checkNumberSize(long data, long min, long max, String message) {
        if (data < min || data > max) {
            throw new WorkflowException(message);
        }
    }


    public static void checkOperator(Operator operator, String message) {
        if (operator == null) {
            throw new WorkflowException(message + "operator is null");
        }
        checkBlank(operator.getId(), message + "operator id is blank.");
    }

    public static void checkOwner(Map<String, List<Operator>> owners, String message) {
        if (CollectionUtils.isEmpty(owners)) {
            return;
        }
        owners.forEach((k, v) -> {
            checkBlank(k, message + "owner's activity is blank.");
            if (!CollectionUtils.isEmpty(v)) {
                v.forEach(x -> checkBlank(x.getId(), message + "operator id is blank."));
            }

        });
    }

    public static boolean isHttpOrHttps(String url) {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url is null");
        }
        String value = url.toLowerCase().trim();
        return (value.startsWith("http://")) || (value.startsWith("https://"));
    }

    public static String trim(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }

    public static void validateModel(Object obj) throws WorkflowException {
        StringBuilder buffer = new StringBuilder(64);

        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();

        Set constraintViolations = validator
                .validate(obj);

        for (Object constraintViolation : constraintViolations) {
            String message = ((ConstraintViolation) constraintViolation).getMessage();
            buffer.append(message).append("\n");
        }
        if (!StringUtils.isBlank(buffer.toString())) {
            throw new WorkflowException(907, buffer.toString());
        }
    }
}