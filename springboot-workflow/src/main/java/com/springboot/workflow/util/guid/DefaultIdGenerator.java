package com.springboot.workflow.util.guid;

import lombok.val;

import java.util.Map;

class DefaultIdGenerator implements GUIDGenerator {
    private long id;
    private final long step;

    DefaultIdGenerator(final Map<String, Object> args) {
        val init = (Long)args.getOrDefault("init", 0L);
        val step = (Long)args.getOrDefault("step", 1L);
        this.id = init > 0 ? init : 0;
        this.step = step;
    }

    @Override
    public long nextId() {
        this.id += step;
        return this.id;
    }
}