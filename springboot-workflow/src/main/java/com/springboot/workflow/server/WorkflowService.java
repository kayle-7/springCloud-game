package com.springboot.workflow.server;

import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ProcessEngine;

import java.util.List;
import java.util.Map;

public interface WorkflowService {

    ProcessEngine getProcessEngine();

    Process getProcess(String paramString);

    List<UserTask> predictionUserTask(String paramString1, String paramString2, Map<String, Object> paramMap);

    boolean isMultiInstanceNodes(String paramString1, String paramString2);

}
