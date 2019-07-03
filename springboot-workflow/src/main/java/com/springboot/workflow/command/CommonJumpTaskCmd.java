package com.springboot.workflow.command;

import com.springboot.workflow.constants.ActivitiConstants;
import com.springboot.workflow.entity.Operator;
import com.springboot.workflow.entity.model.ProcessDoneTask;
import com.springboot.workflow.entity.requests.ProcessRunToRequest;
import com.springboot.workflow.exception.InstanceError;
import com.springboot.workflow.util.Serializer;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntityImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zx
 */
@Slf4j
public class CommonJumpTaskCmd extends AbstractCmd<List<ProcessDoneTask>> {

    private final ProcessRunToRequest request;

    private final String operation;

    public CommonJumpTaskCmd(ProcessRunToRequest request, String operation) {
        this.request = request;
        this.operation = operation;
    }

    @Override
    public List<ProcessDoneTask> doExecute(CommandContext commandContext) {
        val doneTasks = new ArrayList<ProcessDoneTask>();

        val tasks = getActiveTasks(Long.toString(request.getInstanceId()));
        if (CollectionUtils.isEmpty(tasks)) {
            //没有待办，无需跳转
            return doneTasks;
        }

        val activities = getActivities(tasks);
        if (activities.contains(request.getTargetActivity())) {
            if (activities.size() == 1) {
                //case a
                return doneTasks;
            } else {
                //case b
                log.info("doExecute,activity Just Only One:" + Serializer.serialize(request));
                throw InstanceError.activityJustOnlyOne(request);
            }
        } else {
            //case c
            markRunToActivity();
            deleteActiveTask(tasks, commandContext, doneTasks);

            activeTargetTask(commandContext);
            return doneTasks;
        }
    }

    private void markRunToActivity(){
        String operation = (String)getConditions().get(ActivitiConstants.operation);
        if(StringUtils.equals(operation, ActivitiConstants.operationRollback)){
            // rollback 在自动审核的时候需要过滤其中打回节点
            getConditions().put(ActivitiConstants.rollback, runToInfo());
        } else if (StringUtils.equals(operation, ActivitiConstants.operationWithdraw)){
            getConditions().put(ActivitiConstants.withdraw, runToInfo());
        } else {
            getConditions().put(ActivitiConstants.runTo, runToInfo());
        }

    }

    private String runToInfo(){
        return String.format(ActivitiConstants.joinStr, request.getTargetActivity(), getTargetFlowElement().getName());
    }

    private TaskEntity getCurrentOperatorTask(List<TaskEntity> tasks) {
        if (request.getTaskId() == 0) {
            return null;
        }
        for (TaskEntity task : tasks) {
            if (StringUtils.equals(task.getId(), Long.toString(request.getTaskId()))) {
                return task;
            }
        }
        return null;
    }

    private FlowElement getTargetFlowElement(){
        return getTargetFlowElement(request.getDefinitionId(), request.getTargetActivity(), request);
    }

    private HistoricTaskInstanceEntity recordOperationWithHistoricTask(CommandContext commandContext, List<ProcessDoneTask> doneTasks) {
        String taskId = commandContext.getProcessEngineConfiguration().getIdGenerator().getNextId();
        String executionId = commandContext.getProcessEngineConfiguration().getIdGenerator().getNextId();
        FlowElement flowElement = getTargetFlowElement();

        HistoricTaskInstanceEntity hisTask = new HistoricTaskInstanceEntityImpl();
        hisTask.setId(taskId);
        hisTask.setAssignee(request.getOperator().toString());
        hisTask.setTaskDefinitionKey(request.getTargetActivity());

        hisTask.setName(flowElement == null ? "":flowElement.getName());
        hisTask.setProcessInstanceId(Long.toString(request.getInstanceId()));
        hisTask.setProcessDefinitionId(request.getDefinitionId());
        Date date = new Date();
        hisTask.setStartTime(date);
        hisTask.setClaimTime(date);
        hisTask.markEnded(ActivitiConstants.completeStatus);
//        hisTask.setTenantId(request.getApplicationId());
        hisTask.setPriority(ActivitiConstants.taskDefaultPriority);
        hisTask.setExecutionId(executionId);
        commandContext.getHistoricTaskInstanceEntityManager().insert(hisTask, false);

        historicTaskComments(hisTask, doneTasks, true);
        return hisTask;
    }

//    private HistoricVariableInstanceEntityImpl getHisVariable(String executionId, String taskId, String name, Object value, VariableType variableType) {
//        HistoricVariableInstanceEntityImpl hisVariable = new HistoricVariableInstanceEntityImpl();
//        hisVariable.setProcessInstanceId(Long.toString(request.getInstanceId()));
//        hisVariable.setExecutionId(executionId);
//        hisVariable.setTaskId(taskId);
//        hisVariable.setName(name);
//        Date date = new Date();
//        hisVariable.setCreateTime(date);
//        hisVariable.setLastUpdatedTime(date);
//        hisVariable.setVariableType(variableType);
//        variableType.setValue(value, hisVariable);
//        return hisVariable;
//    }

    private void addDeletedTaskComments(ProcessRunToRequest request, Task task) {
        val comments = new HashMap<String, Object>();
        comments.put(ActivitiConstants.operation, operation);
        Object value = request.getConditions().get(ActivitiConstants.actionName);//action   todo 后续考虑取默认值
        comments.put(ActivitiConstants.opinionName,
                String.format(ActivitiConstants.taskCloseDefaultOpinion, request.getOperator().getName(), value == null ? operation : value, getTargetFlowElement().getName()));
        getTaskService().setVariableLocal(task.getId(), ActivitiConstants.taskVariables, Serializer.serialize(comments));
    }



//    private void preSaveHistoricVariable(HistoricTaskInstanceEntity task, String name, Object value, VariableType variableType) {
//        if (value == null) {
//            value = request.getConditions().get(name);
//        }
//        saveHistoricVariable(task,name,value,variableType);
//    }

    private List<String> getActivities(List<TaskEntity> tasks) {
        return tasks.stream().map(TaskInfo::getTaskDefinitionKey).distinct().collect(Collectors.toList());
    }



    private void deleteActiveTask(List<TaskEntity> tasks, CommandContext commandContext, List<ProcessDoneTask> doneTasks) {
        TaskEntity taskEntity = null;

        for (TaskEntity task : tasks) {
            String taskStatus;
            if (task.getId().equals(Long.toString(request.getTaskId()))) {
                taskEntity = task;
//                setOpsOperator();//管理员
                activeTaskComments(task.getId(), getConditions());
                taskStatus = ActivitiConstants.completeStatus;
            } else {
                addDeletedTaskComments(request, task);
                taskStatus = ActivitiConstants.deleted;
            }
            doCloseTask(task, commandContext, taskStatus);

            doneTasks.add(new ProcessDoneTask() {{
                setTaskId(Long.parseLong(task.getId()));
                setActivity(task.getTaskDefinitionKey());
                setName(task.getName());
                setOperator(Operator.valueOf(task.getAssignee()));
                setStatus(taskStatus);
            }});
        }

        if (taskEntity == null ) {
            recordOperationWithHistoricTask(commandContext, doneTasks);
        }

    }

    private void setOpsOperator(){
        if(request.getOpsOperator() != null) {
            getConditions().put(ActivitiConstants.opinionName, request.getOpsOperator().toString());
        }

    }

    private void doCloseTask(TaskEntity task, CommandContext commandContext, String taskStatus) {
        task.setExecutionId(null);
        val hisTask = commandContext.getHistoricTaskInstanceEntityManager().findById(task.getId());
        //1.标记历史任务表中记录
        hisTask.markEnded(taskStatus); //标记任务完成(已删除)
        commandContext.getHistoricTaskInstanceEntityManager().update(hisTask, true);
        //2.删除任务表中记录
        commandContext.getTaskEntityManager().update(task);
        commandContext.getVariableInstanceEntityManager().deleteVariableInstanceByTask(task);
        commandContext.getTaskEntityManager().delete(task, true);
    }

    private void activeTargetTask(CommandContext commandContext) {
        val root = commandContext.getExecutionEntityManager().findByRootProcessInstanceId(Long.toString(request.getInstanceId()));
        if (root != null) { //清空当前执行流，并重建执行流
            commandContext.getExecutionEntityManager().deleteChildExecutions(root, null, false);
            val current = commandContext.getExecutionEntityManager().createChildExecution(root);
            current.setCurrentFlowElement(getTargetFlowElement());
            commandContext.getAgenda().planContinueProcessInCompensation(current);
        }
    }

    @Override
    public Map<String, Object> getConditions() {
        return  request.getConditions();
    }
}
