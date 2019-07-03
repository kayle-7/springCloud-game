package com.springboot.workflow.entity.model;

import com.springboot.workflow.util.Serializer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessActivity {
    private String id;
    private String name;

    public String toString()
    {
        return Serializer.serialize(this);
    }

    public ProcessActivity(String id, String name) { this.id = id; this.name = name; }
}