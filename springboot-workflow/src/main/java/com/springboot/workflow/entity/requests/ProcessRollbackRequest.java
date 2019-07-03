package com.springboot.workflow.entity.requests;

import com.springboot.workflow.entity.Operator;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProcessRollbackRequest extends ProcessRunToRequest {

    @NotNull(message="process handler is null.")
    Operator operator;
    Operator opsOperator;

}
