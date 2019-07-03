package com.springboot.workflow.server;

import com.springboot.workflow.entity.model.ProcessDoneTask;
import com.springboot.workflow.entity.model.ProcessTodoTask;

import java.util.List;

public interface WorkflowTaskService {
    List<ProcessDoneTask> getDoneTask(String paramString1, String paramString2, String paramString3, String paramString4);

    List<ProcessTodoTask> getTodoTask(String paramString1, String paramString2, String paramString3, String paramString4);
}