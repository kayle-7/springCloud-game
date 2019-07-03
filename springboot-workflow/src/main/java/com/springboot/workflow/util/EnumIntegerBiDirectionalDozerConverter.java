package com.springboot.workflow.util;

import org.dozer.CustomConverter;
import org.dozer.MappingException;

import java.lang.reflect.Method;

public class EnumIntegerBiDirectionalDozerConverter implements CustomConverter {
    public Object convert(Object destination, Object source, Class<?> destinationClass, Class<?> sourceClass) {
        if (source == null) {
            return null;
        }
        if ((source instanceof Enum))
            return getInteger(destinationClass, source);
        if ((source instanceof Integer)) {
            return getEnum(destinationClass, source);
        }

        throw new MappingException("Converter " + getClass().getSimpleName() + " was used incorrectly. Arguments were: " +
                destinationClass.getClass().getName() + " and " +
                source);
    }

    private Object getInteger(Class<?> destinationClass, Object source) {
        try {
            Enum em = (Enum)source;
            Class clazz = em.getDeclaringClass();
            Method getCode = clazz.getMethod("getValue", new Class[0]);
            Object code = getCode.invoke(source, new Object[0]);
            return (Integer)code;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object getEnum(Class<?> destinationClass, Object source) {
        try {
            Method m = destinationClass.getDeclaredMethod("findByValue", new Class[] { Integer.TYPE });
            return m.invoke(destinationClass.getClass(), new Object[] { (Integer)source });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}