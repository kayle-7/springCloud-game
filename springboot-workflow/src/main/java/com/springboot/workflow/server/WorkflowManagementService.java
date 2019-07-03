package com.springboot.workflow.server;

import com.springboot.workflow.entity.requests.ProcessDeployRequest;
import com.springboot.workflow.entity.requests.ProcessRequest;
import com.springboot.workflow.exception.WorkflowException;

public interface WorkflowManagementService {

    boolean deploy(final ProcessDeployRequest request) throws WorkflowException;

    //  todo 暂不使用
    boolean delete(final ProcessRequest request, boolean cascade) throws WorkflowException;

}
