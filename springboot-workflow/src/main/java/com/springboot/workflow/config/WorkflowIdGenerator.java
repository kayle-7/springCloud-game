package com.springboot.workflow.config;

import com.springboot.workflow.util.guid.GUIDGenerator;
import com.springboot.workflow.util.guid.IdGeneratorBuilder;
import lombok.Setter;
import org.activiti.engine.impl.cfg.IdGenerator;

/**
 * Created by zx
 */
public class WorkflowIdGenerator implements IdGenerator {

    @Setter
    private GUIDGenerator idGenerator;

    @Override
    public String getNextId() {
        return Long.toString(getIdGenerator().nextId());
    }

    private GUIDGenerator getIdGenerator() {
        if (idGenerator == null) {
            idGenerator = IdGeneratorBuilder.builder().useSnowflake(Thread.currentThread().getId()).build();
        }
        return idGenerator;
    }
}