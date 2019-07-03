package com.springboot.workflow.entity.model.supports;

import com.springboot.workflow.util.Serializer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Process {
    private long id;
    private String applicationId;
    private String definitionCode;
    private int definitionVersion = 0;

    public String toString()
    {
        return Serializer.serialize(this);
    }

}