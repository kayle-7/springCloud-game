package com.springboot.workflow.command.common;

import com.springboot.workflow.entity.Operator;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

import java.util.List;

/**
 * Created by zx
 */
public interface TaskCmd extends BaseCmd {
    HistoricTaskInstanceEntity doCloseTask(TaskEntity paramTaskEntity, String paramString1, String paramString2, Operator paramOperator);

    List<TaskEntity> getActiveTasks(String paramString);
}