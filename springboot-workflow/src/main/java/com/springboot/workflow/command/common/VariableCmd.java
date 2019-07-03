package com.springboot.workflow.command.common;

import com.springboot.workflow.entity.model.ProcessDoneTask;
import org.activiti.engine.impl.variable.BooleanType;
import org.activiti.engine.impl.variable.StringType;
import org.activiti.engine.impl.variable.VariableType;
import org.activiti.engine.task.TaskInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by zx
 */
public interface VariableCmd extends BaseCmd {
    void saveHistoricVariable(TaskInfo paramTaskInfo, String paramString, Object paramObject, VariableType paramVariableType);

    void saveHistoricVariable(String paramString1, String paramString2, String paramString3, String paramString4, Object paramObject, VariableType paramVariableType);

    void saveVariable(String paramString1, String paramString2, Object paramObject);

    void saveVariables(String paramString, Map<String, ? extends Object> paramMap);

    void historicTaskComments(TaskInfo paramTaskInfo, List<ProcessDoneTask> paramList, boolean paramBoolean);

    void activeTaskComments(String paramString, Map<String, Object> paramMap);

    Map<String, Object> getConditions();

    StringType getStringType();

    BooleanType getBooleanType();
}
