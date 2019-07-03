package com.springboot.workflow.server;

import com.springboot.workflow.entity.requests.ProcessQueryRequest;
import com.springboot.workflow.entity.results.ProcessInstanceResult;
import com.springboot.workflow.entity.results.ProcessTaskResult;

import java.util.List;

public interface WorkflowQueryService {

    List<ProcessTaskResult> todo(ProcessQueryRequest processTaskRequest);

    List<ProcessTaskResult> history(ProcessQueryRequest processQueryRequest);

    ProcessInstanceResult owner(ProcessQueryRequest processQueryRequest);

    ProcessInstanceResult definition(ProcessQueryRequest processQueryRequest);

//    Map<String, List<Operator>> getOwners(ProcessInstanceRequest paramProcessInstanceRequest, String paramString);

//    Operator getCreator(ProcessInstanceRequest paramProcessInstanceRequest);

//    List<ProcessInstanceViewModel> getInstances(ProcessSearchRequest paramProcessSearchRequest);
//
//    List<ProcessHistoryViewModel> getHistories(ProcessInstanceRequest paramProcessInstanceRequest);
//
//    List<ProcessTaskViewModel> getTasks(ProcessTaskRequest paramProcessTaskRequest);

//    long getTaskCount(ProcessTaskRequest paramProcessTaskRequest);
//
//    Map<String, String> getConfigs(ProcessInstanceRequest paramProcessInstanceRequest, String paramString);
//
//    Map<String, String> getTemplates(ProcessInstanceRequest paramProcessInstanceRequest, String paramString);
//
//    ProcessResult predictionUserTask(ProcessPredictionRequest paramProcessPredictionRequest);
//
//    List<ProcessActivity> getActivities(ProcessRequest paramProcessRequest, boolean paramBoolean);
//
//    Map<String, String> getActivityMap(ProcessRequest paramProcessRequest);

}