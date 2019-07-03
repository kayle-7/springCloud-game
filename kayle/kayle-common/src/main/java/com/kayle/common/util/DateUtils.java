package com.kayle.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT_WITH_T = "yyyy-MM-dd'T'HH:mm:ss";

    //日期格式化(年-月-日)
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    //日历对象
    private static Calendar c = Calendar.getInstance();

    public static String format(Date date, String pattern){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public static String format(Object date, String pattern){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }
    public static String format( java.sql.Time date, String pattern){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public static String format(String date, String oldPattern,String newPattern){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(oldPattern);
        try {
            Date _date = simpleDateFormat.parse(date);
            return format(_date,newPattern);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @description     string格式化成date
     * @param date      date
     * @param pattern   格式
     * @return java.util.Date
     */
    public static Date parse(String date, String pattern) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.parse(date);
    }

    /**
     * @description     date格式化成date
     * @param date      date
     * @param pattern   格式
     * @return java.util.Date
     */
    public static Date parse(Date date, String pattern) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.parse(format(date, pattern));
    }

    /**
     * @description     date日期后的x年x月x日
     * @param date      date
     * @param days      天
     * @param months    月
     * @param years     年
     * @return java.util.Date
     */
    public static Date afterTime(Date date, int days, int months, int years){
        c.setTime(date);
        if(days != 0){
            c.add(Calendar.DATE, days);
        }
        if(months != 0){
            c.add(Calendar.MONTH, months);
        }
        if(years != 0){
            c.add(Calendar.YEAR, years);
        }
        String str = format.format(date);
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @description     获取days天以后的日期
     * @param days      天
     * @return java.util.Date
     */
    public static Date afterDays(Date day, int days, String pattern) throws ParseException {
        if (day == null) {
            c.setTime(new Date());
        } else {
            c.setTime(day);
        }
        c.add(Calendar.DATE, days);
        Date date = c.getTime();
        if (pattern != null && !("".equals(pattern))) {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            String str = format.format(date);
            return format.parse(str);
        }
        return date;
    }

    /*
    * 获取当前时间
    * */
    public static Timestamp getTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    public static String getDateString() {
        String date = null;
        try {
            date = format(new Date(), "yyyy-MM-dd hh:mm:ss");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @description 获取当前时间 的时间戳
     * @return long
     */
    public static long nowTime(){
        return System.currentTimeMillis();// new Date()为获取当前系统时间，也可使用当前时间戳
    }

    public static boolean checkDate(String date, String pattern, long difference) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);/*"yyyy/MM/dd"*/
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(date);
            return new Date().getTime() - format.parse(date).getTime() >= difference;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkDateCompareNow(String date, String pattern, boolean now) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);/*"yyyy/MM/dd"*/
            format.setLenient(false);
            int i = format.parse(date).compareTo(DateUtils.parse(new Date(), pattern));
            if (now) {
                return i >= 0;
            } else {
                return i <= 0;
            }
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
    }

    public static SimpleDateFormat getDateTimeFormat()
    {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    public static SimpleDateFormat getDateTimeFormatWithT() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    }

    public static String getCurrentDateTime() {
        Date now = new Date();
        return getDateTimeFormat().format(now);
    }

    public static Date getDate(String date) {
        try {
            return getDateFormat().parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDateWithT(String date) {
        try {
            return getDateTimeFormatWithT().parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateWithTStr(String date) {
        return convert2String(getDateWithT(date));
    }

    public static Date getNextDate(String date)
    {
        Date cur = getDate(date);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(cur);
        calendar.add(5, 1);
        return calendar.getTime();
    }

    public static String convert2String(Date date) {
        if (date == null) {
            return null;
        }
        return getDateTimeFormat().format(date);
    }

    public static boolean isValidDateTimeString(String dateTime) {
        try {
            getDateTimeFormat().parse(dateTime);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isValidDateString(String date) {
        try {
            getDateFormat().parse(date);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Date currentDateTime() {
        return new Date();
    }

//    public static void main(String[] args)
//    {
//        System.out.println(getDateWithTStr("2018-09-20T19:34:53.068988557+08:00"));
//    }
}
