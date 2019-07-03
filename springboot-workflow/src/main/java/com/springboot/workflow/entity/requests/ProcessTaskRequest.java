package com.springboot.workflow.entity.requests;

import com.springboot.workflow.entity.model.ProcessTask;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessTaskRequest extends ProcessQueryRequest {

    private ProcessTask.Status status;
    private boolean includeComments;
    private String BusinessKey;

}
