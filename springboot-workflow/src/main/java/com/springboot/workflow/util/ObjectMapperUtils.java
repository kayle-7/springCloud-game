package com.springboot.workflow.util;

import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class ObjectMapperUtils {
    private static final Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    public static <T, S> T convert(S source, T target) { if (source == null) {
        return null;
    }
        Assert.notNull(target, "target instance required");
        mapper.map(source, target);
        return target; }

    public static <T, S> T convert(S source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }

        Assert.notNull(targetClass, "targetClass required");
        return mapper.map(source, targetClass);
    }
    public static <T, S> List<T> convert(List<S> source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }

        List<T> targetList = new ArrayList();
        for (S s : source) {
            T target = mapper.map(s, targetClass);
            targetList.add(target);
        }
        return targetList;
    }
}