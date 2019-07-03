package com.springboot.workflow.entity.model;

import com.springboot.workflow.entity.model.supports.Process;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessInstance extends Process {

    private Status status;
    private String businessKey;

    public static enum Status {
        Running,
        Terminated,
        none;
    }

}