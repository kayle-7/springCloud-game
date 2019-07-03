package com.springboot.workflow.entity.model;

import com.springboot.workflow.entity.Operator;
import com.springboot.workflow.util.Serializer;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProcessTodoTask {

    private long taskId;
    public String activity;
    private String name;
    private String status;
    private Operator owner;
    private int priority;
    private Date taskCreateTime;

    public String toString()
    {
        return Serializer.serialize(this);
    }

}