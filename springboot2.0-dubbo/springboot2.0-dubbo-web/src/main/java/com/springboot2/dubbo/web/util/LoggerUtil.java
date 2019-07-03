package com.springboot2.dubbo.web.util;

import com.springboot2.dubbo.web.model.ApiResult;
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
//        StaffInfo staffInfo = SessionInfoUtil.getStaffInfo();
        StringBuilder sb = new StringBuilder();
        LoggerFactory.getLogger(c).info(
                sb.append("\n【操作日志】")
                        .append("\n【用   户】：用户id").append(0).append("，用户名：").append("sys")//.append(staffInfo == null ? "" : staffInfo.getStaffId()).append("，用户名：").append(staffInfo == null ? "" : staffInfo.getFullName())
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
//        StaffInfo staffInfo = SessionInfoUtil.getStaffInfo();
        StringBuilder sb = new StringBuilder();
        LoggerFactory.getLogger(c).info(
                sb.append("\n【操作日志】")
                .append("\n【用   户】：用户id：").append(0).append("，用户名：").append("sys")//.append(staffInfo == null ? "" : staffInfo.getStaffId()).append("，用户名：").append(staffInfo == null ? "" : staffInfo.getFullName())
                .append("\n【操作信息】：").append(m)
                .append("\n【输入参数】：").append(in)
                .append("\n").toString());
    }

    /**
     * @Description:    设置常规信息
     * @param result    返回对象
     * @param c         接口类
     */
    public static void doInfo(ApiResult result, Class c, String in) {
//        StaffInfo staffInfo = SessionInfoUtil.getStaffInfo();
        StringBuilder sb = new StringBuilder();
        if (result.getCode() >= 400) {
            LoggerFactory.getLogger(c).warn(
                    sb.append("\n【操作日志】")
                            .append("\n【用   户】：用户id：").append(0).append("，用户名：").append("sys")//.append(staffInfo == null ? "" : staffInfo.getStaffId()).append("，用户名：").append(staffInfo == null ? "" : staffInfo.getFullName())
                            .append("\n【操作信息】：").append(result.getMessage())
                            .append("\n【输入参数】：").append(in)
                            .append("\n【返回参数】：").append(Serializer.serialize(result))
                            .append("\n").toString());
        } else {
            LoggerFactory.getLogger(c).info(
                    sb.append("\n【操作日志】")
                            .append("\n【用   户】：用户id：").append(0).append("，用户名：").append("sys")//.append(staffInfo == null ? "" : staffInfo.getStaffId()).append("，用户名：").append(staffInfo == null ? "" : staffInfo.getFullName())
                            .append("\n【操作信息】：").append(result.getMessage())
                            .append("\n【输入参数】：").append(in)
                            .append("\n【返回参数】：").append(Serializer.serialize(result))
                            .append("\n").toString());
        }
    }

    /**
     * @Description: 设置常规失败信息，包含捕捉异常、错误信息、输入参数、输出参数
     * @param c 接口类
     * @param m 错误信息
     */
    public static ApiResult doError(Class c, String m, String in) {
        ApiResult result = ApiResult.fail(m);
//        StaffInfo staffInfo = SessionInfoUtil.getStaffInfo();
        StringBuilder sb = new StringBuilder();
        LoggerFactory.getLogger(c).warn(
                sb.append("\n【异常日志】")
                        .append("\n【用   户】：用户id").append(0).append("，用户名：").append("sys")//.append(staffInfo == null ? "" : staffInfo.getStaffId()).append("，用户名：").append(staffInfo == null ? "" : staffInfo.getFullName())
                        .append("\n【错误信息】：").append(m)
                        .append("\n【输入参数】：").append(in)
                        .append("\n【返回参数】：").append(Serializer.serialize(result))
                        .append("\n").toString());
        return result;
    }

    /**
     * @Description:    设置操作异常信息，包含捕捉异常
     * @param c         接口类
     * @param e         异常
     * @param m         错误信息
     * @param in        输入参数
     */
    public static ApiResult doError(Class c, Throwable e, String m, String in) {
        ApiResult result = ApiResult.fail(m);
//        StaffInfo staffInfo = SessionInfoUtil.getStaffInfo();
        StringBuilder sb = new StringBuilder();
        LoggerFactory.getLogger(c).warn(
                sb.append("\n【异常日志】")
                .append("\n【用   户】：用户id：").append(0).append("，用户名：").append("sys")//.append(staffInfo == null ? "" : staffInfo.getStaffId()).append("，用户名：").append(staffInfo == null ? "" : staffInfo.getFullName())
                .append("\n【错误信息】：").append(m)
                .append("\n【异常信息】：").append(e.toString())
                .append("\n【输入参数】：").append(in)
                .append("\n【返回参数】：").append(Serializer.serialize(result))
                .append("\n").toString(), e);
        return result;
    }
}

