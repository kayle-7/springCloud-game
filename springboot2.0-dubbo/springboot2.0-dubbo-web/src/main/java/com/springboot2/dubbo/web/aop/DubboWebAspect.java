package com.springboot2.dubbo.web.aop;

import com.springboot2.dubbo.web.exception.ValidationUtil;
import com.springboot2.dubbo.web.exception.WebException;
import com.springboot2.dubbo.web.model.ApiResult;
import com.springboot2.dubbo.web.util.Serializer;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by zx
 */
@Slf4j
@Component
@Aspect
public class DubboWebAspect {

    @Pointcut("within(com.springboot2.dubbo.web.controller..*)")
    public void electronicSeal(){}

    @Pointcut("within(com.springboot2.dubbo.web..*)")
    public void electronicSealWeb(){}

    @Before("electronicSealWeb()")
    public void electronicSealWebBefore(JoinPoint joinPoint) {
        log.info(new StringBuilder()
                .append("\n【操作日志】\n【请求方法】:").append(joinPoint.getSignature())
                .append("\n【输入参数】:").append(Serializer.serialize(joinPoint.getArgs()))
                .toString());
    }

    @AfterReturning(returning = "obj", pointcut = "electronicSealWeb()")
    public void electronicSealWebReturning(JoinPoint joinPoint, Object obj){
        log.info(new StringBuilder()
                .append("\n【操作日志】\n【请求方法】:").append(joinPoint.getSignature())
                .append("\n【输入参数】:").append(Serializer.serialize(joinPoint.getArgs()))
                .append("\n【返回参数】:").append(Serializer.serialize(obj))
                .toString());
    }

    @AfterThrowing(throwing="ex", pointcut = "electronicSeal()")
    public void electronicSealWebAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        ApiResult result;
        try {
            WebException we = ValidationUtil.getExceptionMessage(ex);
            ValidationUtil.checkResolveException(we);
            result = ApiResult.fail(we.getCode(), we.getMessage());
            log.warn(new StringBuffer()
                    .append("\n【异常日志】\n【请求方法】:").append(joinPoint.getSignature())
                    .append("\n【异常信息】:").append(we.getMessage())
                    .append("\n【输入参数】:").append(Serializer.serialize(joinPoint.getArgs()))
                    .append("\n【返回参数】:").append(Serializer.serialize(result)).toString(), ex);
        } catch (Exception e) {
            LoggerFactory.getLogger(this.getClass())
                    .warn("electronicSealWebAfterThrowing() joinPoint : " + Serializer.serialize(joinPoint) + "; exception : " + e.toString(), ex);
            throw new WebException(ApiResult.fail("操作异常"));
        }
        throw new WebException(result);
    }

}
