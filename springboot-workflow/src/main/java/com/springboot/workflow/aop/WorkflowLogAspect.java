package com.springboot.workflow.aop;

import com.springboot.workflow.exception.WorkflowException;
import com.springboot.workflow.util.Serializer;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Created by zx
 */
@Slf4j
@Component
@Aspect
public class WorkflowLogAspect {

    @Pointcut("execution(* com.springboot.workflow.server.*.*(..)) && !execution(* com.springboot.workflow.server.*.getProcessEngine(..))")
    public void workflowLog(){}

    @Before("workflowLog()")
    public void workflowLogBefore(JoinPoint joinPoint) {
        log.info(new StringBuilder()
                .append("\n【操作日志】\n【请求方法】:").append(joinPoint.getSignature())
                .append("\n【输入参数】:").append(Serializer.serialize(joinPoint.getArgs()))
                .toString());
    }

    @AfterReturning(returning = "obj", pointcut = "workflowLog()")
    public void methodAfterReturning(JoinPoint joinPoint, Object obj){
        log.info(new StringBuilder()
                .append("\n【操作日志】\n【请求方法】:").append(joinPoint.getSignature())
                .append("\n【返回参数】:").append(Serializer.serialize(obj))
                .toString());
    }

    @AfterThrowing(throwing="ex", pointcut = "workflowLog()")
    public void workflowAfter(JoinPoint joinPoint, Throwable ex) {
        log.warn(new StringBuilder()
                .append("\n【异常日志】\n【请求方法】:").append(joinPoint.getSignature())
                .append("\n【输入参数】:").append(Serializer.serialize(joinPoint.getArgs()))
                .append("\n【异常信息】:").append(ex.getMessage())
                .toString(), ex);
        String errorMessage = "workflow server error! error message : ";
        if (ex instanceof WorkflowException) {
            errorMessage = errorMessage + ex.getMessage();
        } else {
            errorMessage = errorMessage + ex.toString();
        }
        throw new WorkflowException(errorMessage);
    }
}
