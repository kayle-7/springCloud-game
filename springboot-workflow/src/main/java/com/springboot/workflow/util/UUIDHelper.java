package com.springboot.workflow.util;

import java.util.Random;
import java.util.UUID;

/*
 * create by zx
 * */
public class UUIDHelper {
    /*
    * 获取一个UUID
    * */
    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getRandomStr(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        int randomNum;
        char randomChar;
        Random random = new Random();
        // StringBuffer类型的可以append增加字符
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < length; i++) {
            // 可生成[0,n)之间的整数，获得随机位置
            randomNum = random.nextInt(base.length());
            // 获得随机位置对应的字符
            randomChar = base.charAt(randomNum);
            // 组成一个随机字符串
            str.append(randomChar);
        }
        return str.toString();
    }

    public static String getBusinessKey(String applyCode, int applyId) {
        return applyCode + "-" + UUIDHelper.getRandomStr(6) + "-" + applyId;
    }
}
