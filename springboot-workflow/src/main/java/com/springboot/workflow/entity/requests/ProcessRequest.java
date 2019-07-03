package com.springboot.workflow.entity.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.workflow.entity.Operator;
import com.springboot.workflow.util.Serializer;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class ProcessRequest {

    @Pattern(regexp = "^[\\w-]*$", message = "process definition code must be letter(A-Z/a-z), digital(0-9), underline(_) and joiner(-).")
    String definitionCode; //流程唯一标识

    String definitionId; //流程ID

    @Min(value = 0, message = "process definition version < {min}.")
    int definitionVersion; //流程版本

    Operator operator; //操作人

    @JsonIgnore
    public boolean latestVersion() {
        return getDefinitionVersion() == 0;
    }

    public Operator getOperator() {
        if (operator == null) {
            return Operator.getSystem();
        }
        return operator;
    }

    @Override
    public String toString() {
        return Serializer.serialize(this);
    }

}
