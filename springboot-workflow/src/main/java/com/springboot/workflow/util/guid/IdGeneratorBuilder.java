package com.springboot.workflow.util.guid;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class IdGeneratorBuilder {
    private Map<String, Object> values = new HashMap<>();
    private IdGeneratorType key;
    private final static Map<IdGeneratorType, Function<Map<String, Object>, GUIDGenerator>> map = buildMap();
    private final static Function<Map<String, Object>, GUIDGenerator> Default = DefaultIdGenerator::new;

    public static IdGeneratorBuilder builder() {
        return new IdGeneratorBuilder();
    }

    public IdGeneratorBuilder useSnowflake(long workerId) {
        this.key = IdGeneratorType.Snowflake;
        this.values.put("workerId", workerId);
        return this;
    }

    public GUIDGenerator build() {
        return key == null ? Default.apply(values) : map.getOrDefault(key, Default).apply(values);
    }

    private static Map<IdGeneratorType, Function<Map<String, Object>, GUIDGenerator>> buildMap() {
        Map<IdGeneratorType, Function<Map<String, Object>, GUIDGenerator>> map = new HashMap<>();

        map.put(IdGeneratorType.Snowflake, SnowflakeIdGenerator::new);
        //TODO other Id generator
        return map;
    }

    private enum IdGeneratorType {
        Snowflake,
        //TODO other Id generator type
    }
}