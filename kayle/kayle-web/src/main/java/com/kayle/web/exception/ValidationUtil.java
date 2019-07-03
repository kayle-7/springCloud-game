package com.kayle.web.exception;

import com.kayle.common.exception.GlobalException;
import com.kayle.common.exception.InvalidDataException;
import com.kayle.common.util.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {

    /**
     *  object
     */
    public static void assertObjectNull(Object obj, String message){
        if(obj == null ){
            throw new InvalidDataException(message);
        }
    }

    /*
     *  collection
     * */
    public static void assertNotEmpty(List list, String message) {
        if (CollectionUtils.isEmpty(list)) {
            throw new InvalidDataException(message);
        }
    }

    /**
     *  String
     */
    public static void assertEmpty(String data, String message){
        if(StringUtils.isEmpty(data)) {
            throw new InvalidDataException(message);
        }
    }

    public static void assertBlank(String data, String message){
        if(StringUtils.isBlank(data)) {
            throw new InvalidDataException(message);
        }
    }

    public static void assertStrLengthMin(String data, long min, String message) {
        if (StringUtils.isEmpty(data) || data.length() < min) {
            throw new InvalidDataException(message);
        }
    }

    public static void assertStrLengthMax(String data, long max, String message) {
        if (StringUtils.isEmpty(data) || data.length() > max) {
            throw new InvalidDataException(message);
        }
    }

    public static void assertStrLengthSize(String data, long min, long max, String message) {
        if (StringUtils.isEmpty(data) || data.length() < min || data.length() > max) {
            throw new InvalidDataException(message);
        }
    }

    public static void assertStringSize(String data, long min, long max, String message) {
        if(data == null){
            throw new InvalidDataException(message);
        }

        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(data);
        if(!isNum.matches()) {
            throw new InvalidDataException(message);
        }

        BigDecimal bg = new BigDecimal(String.valueOf(data));
        if(bg.compareTo(BigDecimal.ZERO) <= min || bg.compareTo(BigDecimal.ZERO) >= max) {
            throw new InvalidDataException(StringUtils.isNotBlank(message) ? message : "数据必须大于" + min + "并且数据必须小于" + max);
        }
    }

    /**
     *  number
     */
    public static void assertNumberMin(long data, long min, String message) {
        if(data <= min) {
            throw new InvalidDataException(StringUtils.isNotBlank(message) ? message : "数据必须大于" + min);
        }
    }

    public static void assertNumberMax(long data, long max, String message) {
        if(data >= max) {
            throw new InvalidDataException(StringUtils.isNotBlank(message) ? message : "数据必须小于" + max);
        }
    }

    public static void assertNumberSize(long data, long min, long max, String message) {
        if(data <= min || data >= max) {
            throw new InvalidDataException(StringUtils.isNotBlank(message) ? message : "数据必须大于" + min + "并且数据必须小于" + max);
        }
    }

    /*
    *  http
    * */
    public static boolean assertHttpOrHttps(String url) {
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("url is blank");
        }
        String value = url.toLowerCase().trim();
        return (value.startsWith("http://")) || (value.startsWith("https://"));
    }

    /*
    *  trim
    * */
    public static String trim(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }

    /*
    *  validate
    * */
    public static void validateModel(Object obj) throws InvalidDataException {
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
            throw new InvalidDataException(buffer.toString());
        }
    }

    /**
     *  date
     */
    public static void assertDate(String date, String pattern, String message) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);/*"yyyy/MM/dd"*/
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(date);
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            throw new InvalidDataException(StringUtils.isNotBlank(message) ? message : "日期格式错误");
        }
    }

    public static void assertDateCompareNow(String date, String pattern, String message, int now) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);/*"yyyy/MM/dd"*/
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            if (now == format.parse(date).compareTo(DateUtils.parse(new Date(), pattern))) {
                return;
            }
            throw new InvalidDataException(StringUtils.isNotBlank(message) ? message : "日期格式错误");
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            throw new InvalidDataException(StringUtils.isNotBlank(message) ? message : "日期格式错误");
        }
    }

    public static void assertDateCompareNow(String date, String pattern, String message, boolean now) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);/*"yyyy/MM/dd"*/
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            int i = format.parse(date).compareTo(DateUtils.parse(new Date(), pattern));
            if (now) {
                if (i >= 0) {
                    return;
                }
                throw new InvalidDataException(StringUtils.isNotBlank(message) ? message : "日期格式错误");
            } else {
                if (i <= 0) {
                    return;
                }
                throw new InvalidDataException(StringUtils.isNotBlank(message) ? message : "日期格式错误");
            }
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            throw new InvalidDataException(StringUtils.isNotBlank(message) ? message : "日期格式错误");
        }
    }

    /**
     *  ajax
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        if (StringUtils.isNotBlank(requestedWith) && "XMLHTTPREQUEST".equals(requestedWith.toUpperCase())) {
            return true;
        } else {
            String accept = request.getHeader("Accept");
            if (StringUtils.isNotBlank(accept)) {
                String[] accepts = accept.split(",");

                for (String item : accepts) {
                    if ("application/json".equals(item.trim().toLowerCase())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     *  exception
     */
    public static void checkResolveException(WebException we) {
        Throwable t = we.getE();
        if (t instanceof GlobalException) {
            we.setCode(((GlobalException)t).getCode());
            we.setMessage(StringUtils.isBlank(we.getMessage()) ? "操作异常" : we.getMessage());
        } else {
            we.setCode(500);
            we.setMessage("操作异常");
        }
    }

    /**
     * 获取异常信息
     */
    public static WebException getExceptionMessage(Throwable ex) {
        Throwable e = ex;
        StringBuilder sb = new StringBuilder();
        while( e != null) {
            if (ex.getMessage() != null ) {
                sb.append(ex.getMessage());
            }
            if (ex.getCause() != null) {
                ex = ex.getCause();
            }
            e = e.getCause();
        }
        WebException we = new WebException();
        we.setE(ex);
        we.setMessage(sb.toString());
        return we;
    }
}