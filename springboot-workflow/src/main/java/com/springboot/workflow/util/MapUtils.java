package com.springboot.workflow.util;

import java.util.Map;

public class MapUtils {

    public static void copyValue(Map<String, Object> from, Map<String, Object> to, String name) {
        if ((from != null) && (from.containsKey(name)))
            to.put(name, from.get(name));
    }

    public static <K, V> boolean isEmpty(Map<K, V> map)
    {
        return (map == null) || (map.size() <= 0);
    }

    public static <T> boolean isEmpty(T[] array) {
        return (array == null) || (array.length <= 0);
    }
}