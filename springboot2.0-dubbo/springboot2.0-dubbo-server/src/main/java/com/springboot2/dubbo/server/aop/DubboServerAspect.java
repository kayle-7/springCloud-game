package com.springboot2.dubbo.server.aop;

import com.springboot2.dubbo.api.exception.GlobalException;
import com.springboot2.dubbo.server.util.Serializer;
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
public class DubboServerAspect {

    @Pointcut("execution(* com.springboot2.dubbo.server.app.impl.*Impl.*(..))")
    public void appPointcut(){}

    @Pointcut("execution(* com.springboot2.dubbo.server.common.impl.*Impl.*(..))")
    public void commonPointcut(){}

    @Before("appPointcut() " +
            "|| commonPointcut()")
    public void electronicSealServerBefore(JoinPoint joinPoint) {
        log.info(new StringBuilder()
                .append("\n【操作日志】\n【请求方法】:").append(joinPoint.getSignature())
                .append("\n【输入参数】:").append(Serializer.serialize(joinPoint.getArgs()))
                .toString());
        // 中英文处理、人员信息处理
        //I18nUtil.initResourceLanguage();
    }

    @AfterReturning(returning = "obj",
                    pointcut = "appPointcut() " +
                                "|| commonPointcut()")
    public void electronicSealWebReturning(JoinPoint joinPoint, Object obj){
        log.info(new StringBuilder()
                .append("\n【操作日志】\n【请求方法】:").append(joinPoint.getSignature())
                .append("\n【输入参数】:").append(Serializer.serialize(joinPoint.getArgs()))
                .append("\n【返回参数】:").append(Serializer.serialize(obj))
                .toString());
    }

    @AfterThrowing(throwing="ex",
                    pointcut = "appPointcut() " +
                            "|| commonPointcut()")
    public void electronicSealServerAfterThrowing(JoinPoint joinPoint, Throwable ex) throws GlobalException {
        GlobalException se;
        try {
            se = resolveException(ex);
            log.warn(new StringBuffer().append("\n【异常日志】\n【请求方法】:").append(joinPoint.getSignature())
                    .append("\n【异常信息】：").append(se.getMessage())
                    .append("\n【输入参数】:").append(Serializer.serialize(joinPoint.getArgs()))
                    .append("\n【返回参数】：").append("ServiceException").toString(), ex);
        } catch (Exception e) {
            LoggerFactory.getLogger(this.getClass())
                    .warn("electronicSealServerAfterThrowing() joinPoint : " + Serializer.serialize(joinPoint) + "; exception : " + e.toString(), ex);
            throw new GlobalException(400, e.toString());
        }
        throw se;
    }

    private GlobalException resolveException(Throwable e) {
        e.printStackTrace();
        if(e instanceof GlobalException){
            return (GlobalException) e;
        } else{
            return new GlobalException(500, e.getMessage());
        }
    }
}
