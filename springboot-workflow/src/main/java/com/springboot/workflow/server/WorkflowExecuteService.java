package com.springboot.workflow.server;

import com.springboot.workflow.entity.requests.*;
import com.springboot.workflow.entity.results.ProcessInstanceResult;
import com.springboot.workflow.exception.WorkflowException;

public interface WorkflowExecuteService extends WorkflowService {

    ProcessInstanceResult start(ProcessStartRequest processStartRequest)
            throws WorkflowException;

    ProcessInstanceResult submit(ProcessSubmitRequest processSubmitRequest)
            throws WorkflowException;

    ProcessInstanceResult transfer(ProcessTransferRequest processTransferRequest)
            throws WorkflowException;

    ProcessInstanceResult rollback(ProcessRollbackRequest paramProcessRollbackRequest)
            throws WorkflowException;

    ProcessInstanceResult withdraw(ProcessRollbackRequest paramProcessRollbackRequest)
            throws WorkflowException;

    ProcessInstanceResult runTo(ProcessRunToRequest processRunToRequest)
            throws WorkflowException;

    ProcessInstanceResult update(ProcessDataRequest paramProcessDataRequest)
            throws WorkflowException;

    void stop(ProcessInstanceRequest paramProcessInstanceRequest, String paramString)
            throws WorkflowException;

}
