package com.springboot.workflow.entity.model;

import com.springboot.workflow.entity.Operator;
import com.springboot.workflow.entity.model.supports.Process;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessTask extends Process {
    private long instanceId;
    private String activity;
    private Status status;
    private Operator owner;
    private Operator operator;
    private int priority;

    public static enum Status {
        Pending,
        Claimed,
        Completed,
        Deleted,
        Closed;
    }

}