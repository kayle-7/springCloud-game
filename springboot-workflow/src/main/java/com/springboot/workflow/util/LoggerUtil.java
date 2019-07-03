package com.springboot.workflow.util;

import com.springboot.workflow.entity.ApiResult;
import org.slf4j.LoggerFactory;

/**
 * Created by zx
 */
public class LoggerUtil {

    /**
     * @Description:    设置常规操作记录
     * @param c         接口类
     * @param m         操作信息
     */
    public static void info(Class c, String m) {
        LoggerFactory.getLogger(c).info(m);
    }

    /**
     * @Description:    设置常规失败信息，包含捕捉异常和错误信息
     * @param c         接口类
     * @param m         错误信息
     */
    public static void doInfo(Class c, String m) {
        StringBuilder sb = new StringBuilder();
        LoggerFactory.getLogger(c).info(
                sb.append("\n【操作日志】")
                        .append("\n【操作信息】：").append(m)
                        .append("\n").toString());
    }

    /**
     * @Description:    设置常规操作记录
     * @param c         接口类
     * @param m         操作信息
     * @param in        输入参数
     */
    public static void doInfo(Class c, String m, String in) {
        StringBuilder sb = new StringBuilder();
        LoggerFactory.getLogger(c).info(
                sb.append("\n【操作日志】")
                .append("\n【操作信息】：").append(m)
                .append("\n【输入参数】：").append(in)
                .append("\n").toString());
    }

    /**
     * @Description:    设置常规信息
     * @param result    返回对象
     * @param c         接口类
     */
    public static void doInfo(ApiResult result, Class c) {
        StringBuilder sb = new StringBuilder();
        if (result.getCode() >= 400) {
            LoggerFactory.getLogger(c).warn(
                    sb.append("\n【操作日志】")
                            .append("\n【操作信息】：").append(result.getMessage())
                            .append("\n【返回参数】：").append(Serializer.serialize(result))
                            .append("\n").toString());
        } else {
            LoggerFactory.getLogger(c).info(
                    sb.append("\n【操作日志】")
                            .append("\n【操作信息】：").append(result.getMessage())
                            .append("\n【返回参数】：").append(Serializer.serialize(result))
                            .append("\n").toString());
        }
    }

    /**
     * @Description:    设置常规信息
     * @param result    返回对象
     * @param c         接口类
     * @param in        输入参数
     */
    public static void doInfo(ApiResult result, Class c, String in) {
        StringBuilder sb = new StringBuilder();
        LoggerFactory.getLogger(c).info(
                sb.append("\n【操作日志】")
                .append("\n【操作信息】：").append(result.getMessage())
                .append("\n【输入参数】：").append(in)
                .append("\n【返回参数】：").append(Serializer.serialize(result))
                .append("\n").toString());
    }
}

