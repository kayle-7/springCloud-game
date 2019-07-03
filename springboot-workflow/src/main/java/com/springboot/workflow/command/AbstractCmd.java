package com.springboot.workflow.command;

import com.springboot.workflow.command.common.AbstractContextCommand;
import com.springboot.workflow.command.common.ProcessCmd;
import com.springboot.workflow.command.common.TaskCmd;
import com.springboot.workflow.command.common.VariableCmd;
import com.springboot.workflow.constants.ActivitiConstants;
import com.springboot.workflow.entity.Operator;
import com.springboot.workflow.entity.model.ProcessDoneTask;
import com.springboot.workflow.entity.requests.ProcessInstanceRequest;
import com.springboot.workflow.exception.InstanceError;
import com.springboot.workflow.exception.WorkflowException;
import com.springboot.workflow.util.MapUtils;
import com.springboot.workflow.util.Serializer;
import lombok.val;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;
import org.activiti.engine.impl.variable.BooleanType;
import org.activiti.engine.impl.variable.StringType;
import org.activiti.engine.impl.variable.VariableType;
import org.activiti.engine.task.TaskInfo;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zx
 */
public abstract class AbstractCmd<T> extends AbstractContextCommand<T> implements ProcessCmd, TaskCmd, VariableCmd {

    @Override
    public FlowElement getTargetFlowElement(String processDefinitionId, String activity, ProcessInstanceRequest request) throws WorkflowException {
        val process = ProcessDefinitionUtil.getProcess(processDefinitionId);
        val targetFlowElement = process.getFlowElement(activity);
        if (targetFlowElement == null) {
            throw InstanceError.activityNotFound(request, activity);
        }
        return targetFlowElement;
    }

    @Override
    public HistoricTaskInstanceEntity doCloseTask(TaskEntity task, String operation, String closedReason, Operator operator) {
        task.setExecutionId(null);
        HistoricTaskInstanceEntity hisTask = Context.getCommandContext().getHistoricTaskInstanceEntityManager().findById(task.getId());
        //1.标记历史任务表中记录
        hisTask.markEnded(ActivitiConstants.closeStatus); //标记任务完成(已删除)
        hisTask.setDeleteReason(closedReason);
        Context.getCommandContext().getHistoricTaskInstanceEntityManager().update(hisTask, true);
        //2.删除任务表中记录
        Context.getCommandContext().getTaskEntityManager().update(task);
        Context.getCommandContext().getVariableInstanceEntityManager().deleteVariableInstanceByTask(task);
        Context.getCommandContext().getTaskEntityManager().delete(task, true);
        return  hisTask;
    }

    @Override
    public void saveHistoricVariable(TaskInfo task, String name, Object value, VariableType variableType) {
        saveHistoricVariable(task.getProcessInstanceId(),task.getExecutionId(), task.getId(),
                    name, value, variableType);
    }

    @Override
    public void saveHistoricVariable(String instanceId,String executionId, String taskId, String name, Object value, VariableType variableType) {
        if (value != null) {
            HistoricVariableInstanceEntity hisVariable = createHisVariable(instanceId,executionId, taskId,
                    name, value, variableType);
            Context.getCommandContext().getHistoricVariableInstanceEntityManager().update(hisVariable, false);
        }
    }

    public HistoricVariableInstanceEntity createHisVariable(String instanceId,String executionId, String taskId, String name, Object value, VariableType variableType) {
        HistoricVariableInstanceEntity hisVariable = Context.getCommandContext().getHistoricVariableInstanceEntityManager().create();
        hisVariable.setProcessInstanceId(instanceId);
        hisVariable.setExecutionId(executionId);
        hisVariable.setTaskId(taskId);
        hisVariable.setName(name);
        hisVariable.setCreateTime(new Date());
        hisVariable.setLastUpdatedTime(new Date());
        hisVariable.setVariableType(variableType);
        variableType.setValue(value, hisVariable);
        return hisVariable;
    }

    @Override
    public void saveVariable(String executionId, String name, Object value) {
        Context.getCommandContext().getProcessEngineConfiguration().getRuntimeService().setVariableLocal(executionId,name,value);
    }

    @Override
    public void saveVariables(String executionId, Map<String, ? extends Object> variables) {
        Context.getCommandContext().getProcessEngineConfiguration().getRuntimeService().setVariablesLocal(executionId,variables);
    }



    private String[] activeCommentNames() {
        return ActivitiConstants.ACTIVE_TASK_COMMENTS;
    }

    @Override
    public void activeTaskComments(String taskId, Map<String,Object> comments) {
        val saveComments = new HashMap<String, Object>();
        saveComments.put(ActivitiConstants.approvalName, true);
        for(int i = 0; i < activeCommentNames().length; i++) {
            MapUtils.copyValue(comments, saveComments, activeCommentNames()[i]);
        }
        getTaskService().setVariableLocal(taskId, ActivitiConstants.taskVariables, Serializer.serialize(saveComments));
    }


    private String[] closeCommentNames() {
        return ActivitiConstants.CLOSE_TASK_COMMENTS;
    }

    @Override
    public void historicTaskComments(TaskInfo task, List<ProcessDoneTask> preActivityDoneTasks, boolean approval) {
        Map<String, Object> comments = getConditions();
        val saveComments = new HashMap<String, Object>();

        //该待办由人工触发完成
        if (approval){
            comments.put(ActivitiConstants.approvalName, approval);
        }

        //记录跳转节点
        if (CollectionUtils.isNotEmpty(preActivityDoneTasks)) {
            saveComments.put(ActivitiConstants.preActivity, getActivity(preActivityDoneTasks));
            saveComments.put(ActivitiConstants.preActivityName, getActivityName(preActivityDoneTasks));
        }

        for (int i = 0; i < closeCommentNames().length; i++) {
            MapUtils.copyValue(comments, saveComments, closeCommentNames()[i]);
        }

        if (saveComments.size() > 0) {
            HistoricVariableInstanceEntity hisVariable = createHisVariable(task.getProcessInstanceId(),
                    task.getExecutionId(), task.getId(),
                    ActivitiConstants.taskVariables, Serializer.serialize(saveComments), getStringType());
            Context.getCommandContext().getHistoricVariableInstanceEntityManager().insert(hisVariable, false);
        }

    }

    private void setVariables(Map<String,Object> variables,String name,Object value){
        if (value == null) {
            Map conditions = getConditions();
            if(conditions != null){
                value = conditions.get(name);
            }
        }
        if (value != null) {
            variables.put(name,value);
        }
    }


    protected String getActivity (List<ProcessDoneTask> doneTasks) {
        return String.join(ActivitiConstants.preActivitySeparator, doneTasks.stream().map(ProcessDoneTask::getActivity).distinct().collect(Collectors.toList()));
    }

    protected String getActivityName (List<ProcessDoneTask> doneTasks) {
        return String.join(ActivitiConstants.preActivitySeparator,doneTasks.stream().map(ProcessDoneTask::getName).distinct().collect(Collectors.toList()));
    }

    @Override
    public StringType getStringType() {
        return new StringType(ActivitiConstants.variablesStringMaxLength);
    }

    @Override
    public BooleanType getBooleanType() {
        return new BooleanType();
    }

    @Override
    public List<TaskEntity> getActiveTasks(String instanceId) {
        return Context.getCommandContext().getTaskEntityManager().findTasksByProcessInstanceId(instanceId);
    }

    public boolean isEmpty(Collection collection){
        return CollectionUtils.isEmpty(collection);
    }

    public boolean notEmpty(Collection collection){
        return !CollectionUtils.isEmpty(collection);
    }

}
