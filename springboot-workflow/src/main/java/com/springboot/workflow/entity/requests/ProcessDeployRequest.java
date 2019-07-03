package com.springboot.workflow.entity.requests;

import com.springboot.workflow.entity.Operator;
import com.springboot.workflow.util.Serializer;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
public class ProcessDeployRequest {

    @NotNull(message = "process definition content is null")
    @Size(min = 1, message = "process definition content must be >= {min}.")
    String content; //部署的内容

    int contentType; //流程定义内容的类型，默认为xml

    @Size(max = 50, message = "process deploy operator size must be <= {max}.")
    String operator; //操作人
    String name; //部署的名称

    @Size(max = 40, message = "process definition code size must be <= {max}.")
    String key; //部署的key

    public String getOperator() {
        if (StringUtils.isEmpty(operator)) {
            return Operator.getSystem().getName();
        }
        return operator;
    }

    @Override
    public String toString() {
        return Serializer.serialize(this);
    }
}
