package com.springboot.workflow.entity.results;

import com.springboot.workflow.entity.Operator;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ProcessTaskResult {

    private long taskId;
    private Operator owner;
    private String definitionCode;
    private int definitionVersion;
    private long instanceId;
    private String activity;
    private String name;
    private String status;
    private long beginTime;
    private long dueTime;
    private long endTime;
    private long claimTime;
    private int priority;
    private Map<String, Object> properties;

}
