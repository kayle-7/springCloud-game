package com.springboot.workflow.entity.model;

import com.springboot.workflow.entity.Operator;
import com.springboot.workflow.util.Serializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessDoneTask {
    private long taskId;
    private String activity;
    private String name;
    private String status;
    private Operator operator;

    public String toString()
    {
        return Serializer.serialize(this);
    }
}