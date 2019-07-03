package com.springboot.workflow.entity.model;

import com.springboot.workflow.entity.model.supports.Process;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessDefinition extends Process {

    private String description;
    private String definitionId;

}