package com.springboot.workflow.server.impl;

import com.springboot.workflow.entity.requests.ProcessQueryRequest;
import com.springboot.workflow.entity.results.ProcessInstanceResult;
import com.springboot.workflow.entity.results.ProcessMapper;
import com.springboot.workflow.entity.results.ProcessTaskResult;
import com.springboot.workflow.exception.WorkflowException;
import com.springboot.workflow.server.AbstractProcessCommonService;
import com.springboot.workflow.server.WorkflowQueryService;
import com.springboot.workflow.util.ValidationUtil;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zx
 */
@Slf4j
@Component
public class WorkflowQueryServiceImpl extends AbstractProcessCommonService implements WorkflowQueryService {

    @Setter
    @Resource
    private ProcessEngine processEngine;

    public ProcessEngine getProcessEngine() {
        return this.processEngine;
    }

    @Override
    public List<ProcessTaskResult> todo(@NonNull final ProcessQueryRequest request) {
        validateTodo(request);

        val query = getTaskQuery(request);
        val tasks = query.orderByTaskCreateTime().desc().list();
        val entities = new ArrayList<ProcessTaskResult>();
        for (val task : tasks) {
            val entity = ProcessMapper.toTodoEntity(task);
            entities.add(entity);
        }
        return entities;
    }

    @Override
    public List<ProcessTaskResult> history(@NonNull final ProcessQueryRequest request) {
        validateHistory(request);
        val historyService = getProcessEngine().getHistoryService();
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery().includeTaskLocalVariables();
        if (request.getInstanceId() > 0) {
            query = query.processInstanceId(Long.toString(request.getInstanceId()));
        } else {
            initProcessDefinitionId(request);
            query = query.processDefinitionId(request.getDefinitionId());
        }
        val tasks = query.finished().orderByHistoricTaskInstanceEndTime().asc().list();
        val entities = new ArrayList<ProcessTaskResult>();
        for (val task : tasks) {
            val entity = ProcessMapper.toHistoryEntity(task);
            entities.add(entity);
        }
        return entities;
    }

    @Override
    public ProcessInstanceResult owner(ProcessQueryRequest processQueryRequest) {
        return null;
    }

    @Override
    public ProcessInstanceResult definition(ProcessQueryRequest processQueryRequest) {
        return null;
    }

    @Override
    public Process getProcess(String paramString) {
        return null;
    }

    @Override
    public List<UserTask> predictionUserTask(String paramString1, String paramString2, Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public boolean isMultiInstanceNodes(String paramString1, String paramString2) {
        return false;
    }

    private static void validateHistory(ProcessQueryRequest request) throws WorkflowException {
        ValidationUtil.checkNumberMin(request.getInstanceId(), 1L, "process instance id < 1L.");
    }

    private static void validateTodo(ProcessQueryRequest request) throws WorkflowException {
        ValidationUtil.checkNumberMin(request.getInstanceId(), 1L, "process instance id < 1L.");
    }
}
