package com.springboot.workflow.util;

import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by zx
 */
public class ClassUtils {
    /**
     * @description     通过属性名称获取对象属性值
     * @param fieldName 属性名
     * @param obj       dto对象
     * @return java.lang.Object
     */
    public static Object getFieldValueByObj(String fieldName, Object obj) {
        Object value = null;
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = obj.getClass().getMethod(getter);
            value = method.invoke(obj);
        } catch (Exception e) {
            LoggerFactory.getLogger(ClassUtils.class).warn("fieldName : " + fieldName + "; obj : " + Serializer.serialize(obj), e);
        }
        return value;
    }

    public static Object getFieldValueByClassName(String methodName, String className, Object... objects) {
        Object value = null;
        try {
            Method method = Class.forName(className).getMethod(methodName);
            value = method.invoke(Class.forName(className).newInstance(), objects);
        } catch (Exception e) {
            LoggerFactory.getLogger(ClassUtils.class).warn("methodName : " + methodName + "; className : " + className + "; obj : " + Serializer.serialize(objects), e);
        }
        return value;
    }

    public static String classMapperServiceName(Class t) {
        String shortName = org.apache.commons.lang3.ClassUtils.getShortCanonicalName(t);
        return shortName.substring(0, 1).toLowerCase() + shortName.substring(1);
    }

    public static String classMapperServiceName(String t) {
        String shortName = org.apache.commons.lang3.ClassUtils.getShortCanonicalName(t);
        return shortName.substring(0, 1).toLowerCase() + shortName.substring(1);
    }
}