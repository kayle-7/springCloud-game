package com.springboot.workflow.util.guid;

public interface GUIDGenerator {
    /**
     * 生成一个全局唯一ID
     */
    long nextId();
}