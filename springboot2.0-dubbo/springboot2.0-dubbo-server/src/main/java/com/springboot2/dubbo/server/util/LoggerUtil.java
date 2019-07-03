package com.springboot2.dubbo.server.util;

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
}

