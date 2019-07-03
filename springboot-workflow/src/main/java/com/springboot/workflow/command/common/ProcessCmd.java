package com.springboot.workflow.command.common;

import com.springboot.workflow.entity.requests.ProcessInstanceRequest;
import com.springboot.workflow.exception.WorkflowException;
import org.activiti.bpmn.model.FlowElement;

/**
 * Created by zx
 */
public interface ProcessCmd extends BaseCmd {
    FlowElement getTargetFlowElement(String paramString1, String paramString2, ProcessInstanceRequest paramProcessInstanceRequest)
            throws WorkflowException;
}