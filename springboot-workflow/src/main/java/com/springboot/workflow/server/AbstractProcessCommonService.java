package com.springboot.workflow.server;

import com.springboot.workflow.constants.ActivitiConstants;
import com.springboot.workflow.entity.Operator;
import com.springboot.workflow.entity.model.ProcessDoneTask;
import com.springboot.workflow.entity.model.ProcessInstance;
import com.springboot.workflow.entity.model.ProcessTask;
import com.springboot.workflow.entity.model.ProcessTodoTask;
import com.springboot.workflow.entity.requests.ProcessDataRequest;
import com.springboot.workflow.entity.requests.ProcessInstanceRequest;
import com.springboot.workflow.entity.requests.ProcessQueryRequest;
import com.springboot.workflow.entity.requests.ProcessRequest;
import com.springboot.workflow.entity.results.ProcessInstanceResult;
import com.springboot.workflow.exception.InstanceError;
import com.springboot.workflow.exception.WorkflowException;
import com.springboot.workflow.util.ProcessUtils;
import com.springboot.workflow.util.Serializer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.Activity;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by zx
 */
@Slf4j
public abstract class AbstractProcessCommonService implements WorkflowService, WorkflowTaskService {

    @Resource
    @Setter
    private ProcessEngine processEngine;

    @Override
    public ProcessEngine getProcessEngine() {
        return this.processEngine;
    }

    @Override
    public Process getProcess(String processDefinitionId) {
        return ProcessUtils.getProcess(getProcessEngine(), processDefinitionId);
    }

    @Override
    public List<UserTask> predictionUserTask(String procDefId, String taskDefKey, Map<String, Object> map) {
        return ProcessUtils.getNextNode(this.getProcessEngine(), procDefId, taskDefKey, map);
    }

    protected Object getLocalVariables(String taskId, String variableName) {
        HistoricVariableInstance taskVariables =  getProcessEngine().getHistoryService().createHistoricVariableInstanceQuery()
                .taskId(taskId).variableName(ActivitiConstants.taskVariables).singleResult();
        String taskVariablesStr = (String)taskVariables.getValue();
        return getLocalVariables(taskId, taskVariablesStr, variableName);
    }

    protected Object getLocalVariables(String taskId, String taskVariablesStr, String variableName) {
        if(taskVariablesStr != null){
            Map<String,Object> variables = Serializer.deserialize(taskVariablesStr, Map.class);
            return variables.get(variableName);
        } else{
            val v = getProcessEngine().getHistoryService().createHistoricVariableInstanceQuery()
                    .taskId(taskId).variableName(variableName).singleResult();
            return v == null ? null : v.getValue();
        }
    }

    protected void initProcessDefinitionId(ProcessInstanceRequest request) {
        if (request.getInstanceId() > 0) {
            val instance = getInstance(request);
            if (instance == null) {
                log.info("initProcessDefinitionId:" + Serializer.serialize(request));
                throw InstanceError.processInstanceNotFound(request);
            }
            request.setDefinitionId(instance.getProcessDefinitionId());
            request.setDefinitionVersion(instance.getProcessDefinitionVersion());
        } else {
            initProcessDefinitionId((ProcessRequest) request);
        }
    }

    protected void initProcessDefinitionId(ProcessRequest request) {
        ProcessDefinitionQuery query = getProcessEngine().getRepositoryService().createProcessDefinitionQuery();
        if(StringUtils.isNotEmpty(request.getDefinitionCode())){
            query = query.processDefinitionKey(request.getDefinitionCode());
        }
        ProcessDefinition processDefinition;
        if (request.latestVersion()) {
            processDefinition = query.latestVersion().singleResult();
        } else {
            processDefinition = query.processDefinitionVersion(request.getDefinitionVersion())
                    .singleResult();
        }
        if (processDefinition == null) {
            throw new WorkflowException("processDefinition is null." + Serializer.serialize(request));
        }
        request.setDefinitionId(processDefinition.getId());
        request.setDefinitionVersion(processDefinition.getVersion());
    }

    protected HistoricProcessInstance getInstance(ProcessInstanceRequest request) {
        return getProcessEngine().getHistoryService().createHistoricProcessInstanceQuery()
                .processInstanceId(Long.toString(request.getInstanceId()))
                .singleResult();
    }


    protected void addComments(ProcessDataRequest request, TaskInfo task) {
        addComments(request, task.getId(), true);
    }

    private void addComments(ProcessDataRequest request, String taskId, boolean approvalByUser) {
        addComments(request.getConditions(), taskId, approvalByUser);
    }

    private void addComments(Map<String, Object> conditions, String taskId, boolean approvalByUser) {
        val comments = new HashMap<String, Object>();

        if(approvalByUser){
            comments.put(ActivitiConstants.approvalName, true); //该待办由人工触发完成    _@approval  true
        }

        Object value = conditions.get(ActivitiConstants.actionName);//"_@action"    同意
        if (value != null) {
            comments.put(ActivitiConstants.actionName, value);
        }
        value = conditions.get(ActivitiConstants.opinionName);//"_@opinion" 同意审批
        if (value != null) {
            comments.put(ActivitiConstants.opinionName, value);
        }
        value = conditions.get(ActivitiConstants.remarkName);//"_@remark"   agree
        if (value != null) {
            comments.put(ActivitiConstants.remarkName, value);
        }
        value = conditions.get(ActivitiConstants.operation);//"_@operation" 审批、驳回、移交......
        if (value != null) {
            comments.put(ActivitiConstants.operation, value);
        }

//        value = ccnditions.get(ActivitiConstants.transferInfo);
//        if (value != null) {
//            String transferHistory = getTransferInfo(taskId);
//            if(transferHistory != null){
//                value = transferHistory.concat(ActivitiConstants.LINE_SEPARATOR).concat(value.toString());
//            }
//            comments.put(ActivitiConstants.transferInfo, value);
//        }

        try {
            getProcessEngine().getTaskService().setVariableLocal(taskId, ActivitiConstants.taskVariables, Serializer.serialize(comments));//_@taskVariables
        } catch (Exception e){
            log.warn("setVariableLocal error", e);
        }

    }

    private String getTransferInfo(String taskId){
        val type =  getProcessEngine().getHistoryService().createHistoricVariableInstanceQuery()
                .taskId(taskId).variableName(ActivitiConstants.transferInfo).singleResult();

        return type == null ? null:type.getValue().toString();
    }



    @Override
    public boolean isMultiInstanceNodes(String definitionId, String taskDefinitionKey) {
        val nodes = getMultiInstanceNodes(definitionId);
        return nodes.containsKey(taskDefinitionKey);
    }

    private static final Map<String, Map<String, SingleInstanceNode>> singleInstanceNodes = new ConcurrentHashMap<>();
    protected static final Map<String, Map<String, MultiInstanceNode>> multiInstanceNodes = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, MixInstanceNode>> mixInstanceNodes = new ConcurrentHashMap<>();

    protected Map<String, SingleInstanceNode> getSingleInstanceNodes(String definitionId) {
        if (!singleInstanceNodes.containsKey(definitionId)) {
            cacheSingleInstanceNodes(definitionId);
        }
        return singleInstanceNodes.get(definitionId);
    }

    private synchronized void cacheSingleInstanceNodes(String definitionId) {
        if (!singleInstanceNodes.containsKey(definitionId)) {
            val nodes = new HashMap<String, SingleInstanceNode>();
            val process = getProcess(definitionId);
            val flowNodes = process.getFlowElements();
            for (val flowNode : flowNodes) {
                if (flowNode instanceof UserTask) {
                    val userTask = (UserTask) flowNode;
                    val assignee = userTask.getAssignee();
                    if (StringUtils.isNotBlank(assignee)) {
                        nodes.put(userTask.getId(), new SingleInstanceNode(assignee));
                    }
                }
            }
            singleInstanceNodes.put(definitionId, nodes);
        }
    }

    protected Map<String, MultiInstanceNode> getMultiInstanceNodes(String definitionId) {
        if (!multiInstanceNodes.containsKey(definitionId)) {
            cacheMultiInstanceNodes(definitionId);
        }
        return multiInstanceNodes.get(definitionId);
    }

    private synchronized void cacheMultiInstanceNodes(String definitionId) {
        if (!multiInstanceNodes.containsKey(definitionId)) {
            val nodes = new HashMap<String, MultiInstanceNode>();
            val process = getProcess(definitionId);
            val flowNodes = process.getFlowElements();
            for (val flowNode : flowNodes) {
                if (flowNode instanceof Activity) {
                    val activity = (Activity) flowNode;
                    val loopCharacteristics = activity.getLoopCharacteristics();
                    if (loopCharacteristics != null) {
                        val node = new MultiInstanceNode();
                        node.setSequential(loopCharacteristics.isSequential());
                        node.setCompletionCondition(loopCharacteristics.getCompletionCondition());
                        node.setListVariable(loopCharacteristics.getInputDataItem());
                        node.setElementVariable(loopCharacteristics.getElementVariable());
                        nodes.put(activity.getId(), node);
                    }
                }
            }
            multiInstanceNodes.put(definitionId, nodes);
        }
    }

    protected Map<String, MixInstanceNode> getMixInstanceNodes(String definitionId) {
        if (!mixInstanceNodes.containsKey(definitionId)) {
            cacheMixInstanceNodes(definitionId);
        }
        return mixInstanceNodes.get(definitionId);
    }

    private synchronized void cacheMixInstanceNodes(String definitionId) {
        if (!mixInstanceNodes.containsKey(definitionId)) {
            val nodes = new HashMap<String, MixInstanceNode>();
            val process = getProcess(definitionId);
            val flowNodes = process.getFlowElements();
            for (val flowNode : flowNodes) {
                if (flowNode instanceof UserTask) {
                    val userTask = (UserTask) flowNode;
                    val loopCharacteristics = userTask.getLoopCharacteristics();
                    if (loopCharacteristics != null) {
                        val node = new MultiInstanceNode();
                        node.setSequential(loopCharacteristics.isSequential());
                        node.setCompletionCondition(loopCharacteristics.getCompletionCondition());
                        node.setListVariable(loopCharacteristics.getInputDataItem());
                        node.setElementVariable(loopCharacteristics.getElementVariable());
                        nodes.put(userTask.getId(), node);
                    } else {
                        val assignee = userTask.getAssignee();
                        if (StringUtils.isNotBlank(assignee)) {
                            nodes.put(userTask.getId(), new SingleInstanceNode(assignee));
                        }
                    }

                }
            }
            mixInstanceNodes.put(definitionId, nodes);
        }
    }

    public ProcessTodoTask convertToProcessTodoTask(Task task){
        val processTask = new ProcessTodoTask();
        processTask.setTaskId(Long.parseLong(task.getId()));
        processTask.setPriority(task.getPriority());
        processTask.setActivity(task.getTaskDefinitionKey());
        processTask.setName(task.getName());
        processTask.setOwner(Operator.valueOf(task.getAssignee()));
        processTask.setStatus(StringUtils.isEmpty(task.getAssignee()) ?
                ProcessTask.Status.Pending.name() : ProcessTask.Status.Claimed.name());
        processTask.setTaskCreateTime(task.getCreateTime());
//        processTask.setParentTaskId(task.getParentTaskId() == null ? 0: Long.parseLong(task.getParentTaskId()));
        return processTask;
    }

    public ProcessDoneTask convertToProcessDoneTask(TaskInfo task){
        return convertToProcessDoneTask(task,null);
    }

    public ProcessDoneTask convertToProcessDoneTask(TaskInfo task , String status){
        ProcessDoneTask doneTask = new ProcessDoneTask();
        doneTask.setTaskId(Long.parseLong(task.getId()));
        doneTask.setActivity(task.getTaskDefinitionKey());
        doneTask.setName(task.getName());
        doneTask.setOperator(Operator.valueOf(task.getAssignee()));
        doneTask.setStatus(status == null ? ActivitiConstants.completeStatus:status);
        return  doneTask;
    }

    //流转到终止节点的返回处理
    public ProcessInstanceResult terminatedResult(ProcessInstanceRequest request, List<ProcessTodoTask> todoTasks, List<ProcessDoneTask> doneTasks) {
        val instance = getInstance(request);
        if (instance.getEndTime() != null) {
//            processTaskOwnerMapper.clear(request, request.getInstanceId());
            val result = request.toResult(todoTasks, doneTasks, ProcessInstance.Status.Terminated.name());
            val endActivity = instance.getEndActivityId();
            if (StringUtils.isNotEmpty(endActivity)) { //流程已结束
                result.getCurrentActivities().add(endActivity);
            }
            return result;
        } else { //无待办任务，且流程未结束
            throw InstanceError.taskMissException(request);
        }
    }

    protected List<ProcessDoneTask> getDoneTaskAfter(String instanceId, String assignee, String activity, String taskId){
        val tasks = getTaskHistoriesQuery(instanceId,assignee, activity,null)
                .taskDefinitionKey(activity).finished().includeTaskLocalVariables()
                .orderByHistoricTaskInstanceEndTime().desc().list();
        List<ProcessDoneTask> list = new ArrayList<ProcessDoneTask>();
        for(HistoricTaskInstance t : tasks){
            list.add(doneTask(t));
            if(StringUtils.equals(t.getId(),taskId)){
                break;
            }
        }
        return list;
    }

    private ProcessDoneTask doneTask(HistoricTaskInstance t){
        return new ProcessDoneTask() {
            {
                setTaskId(Long.parseLong(t.getId()));
                setActivity(t.getTaskDefinitionKey());
                setName(t.getName());
                setOperator(Operator.valueOf(t.getAssignee()));
                Object variables = t.getTaskLocalVariables().get(ActivitiConstants.taskVariables);
                if(variables == null) {
                    if(t.getTaskLocalVariables().containsKey(ActivitiConstants.autoCompleteType)){
                        setStatus(ActivitiConstants.autoCompleteStatus);
                    }else if(t.getTaskLocalVariables().containsKey(ActivitiConstants.approvalName)){
                        setStatus(ActivitiConstants.completeStatus);
                    }else {
                        setStatus(ActivitiConstants.closeStatus);
                    }
                } else {
                    Map<String,Object> map = Serializer.deserialize(variables.toString(), Map.class);
                    if(map.containsKey(ActivitiConstants.autoCompleteType)){
                        setStatus(ActivitiConstants.autoCompleteStatus);
                    }else if(map.containsKey(ActivitiConstants.approvalName)){
                        setStatus(ActivitiConstants.completeStatus);
                    }else {
                        setStatus(ActivitiConstants.closeStatus);
                    }
                }
            }
        };
    }

    @Override
    public List<ProcessDoneTask> getDoneTask(String instanceId, String assignee, String activity, String taskId) {
        return getTaskHistoriesQuery(instanceId,assignee, activity,taskId)
                .taskDefinitionKey(activity).finished().includeTaskLocalVariables()
                .orderByHistoricTaskInstanceEndTime().desc().list()
                .stream().map(this::doneTask).collect(Collectors.toList());
    }

    @Override
    public List<ProcessTodoTask> getTodoTask(String instanceId, String assignee, String activity, String taskId) {
        val tasks = getTaskQuery(instanceId, assignee).list();
        List<ProcessTodoTask> todoTasks = new ArrayList<ProcessTodoTask>();
        for(val task:tasks){
            todoTasks.add(convertToProcessTodoTask(task));
        }
        return todoTasks;
    }

    protected HistoricTaskInstanceQuery getTaskHistoriesQuery(String instanceId,String assignee, String activity,String taskId) {
        val query = getProcessEngine().getHistoryService().createHistoricTaskInstanceQuery()
                .processInstanceId(instanceId);
        if(StringUtils.isNotEmpty(assignee)){
            query.taskAssignee(assignee);
        }
        if(StringUtils.isNotEmpty(activity)){
            query.taskDefinitionKey(activity);
        }
        if(StringUtils.isNotEmpty(taskId)){
            query.taskId(taskId);
        }
        return query;
    }

    protected TaskQuery getTaskQuery(ProcessInstanceRequest request) {
        return getTaskQuery(Long.toString(request.getInstanceId()), null);
    }

    protected TaskQuery getTaskQuery(ProcessQueryRequest request) {
        return getTaskQuery(Long.toString(request.getInstanceId()), null);
    }

    protected TaskQuery getTaskQuery(ProcessInstanceRequest request, String assignee) {
        return getTaskQuery(Long.toString(request.getInstanceId()), assignee);
    }

    private TaskQuery getTaskQuery(String instanceId, String assignee) {
        TaskQuery query = getProcessEngine().getTaskService().createTaskQuery()
                .processInstanceId(instanceId)
                .active();
        if (StringUtils.isNotEmpty(assignee)) {
            query = query.taskAssignee(assignee.trim());
        }
        return query;
    }

    /**
     * The type Multi instance node.
     */
    @Setter
    @Getter
    protected static class SingleInstanceNode extends MixInstanceNode {
        String assignee;

        SingleInstanceNode(String assignee) {
            this.assignee = assignee;
        }
    }

    /**
     * The type Multi instance node.
     */
    @Setter
    @Getter
    protected static class MultiInstanceNode extends MixInstanceNode{
        /**
         * The Completion condition.
         */
        String completionCondition;
        /**
         * The List variable.
         */
        @Pattern(regexp = "^\\$\\{\\w+}$", message = "activity listVariable must match ${\\w}.")
        String listVariable;
        /**
         * The Element variable.
         */
        String elementVariable;
        /**
         * The Sequential.
         */
        boolean sequential;
    }

    @Setter
    @Getter
    public static class MixInstanceNode { }
}
