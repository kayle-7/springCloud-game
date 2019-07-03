package com.springboot.workflow.entity.results;

import com.springboot.workflow.constants.ActivitiConstants;
import com.springboot.workflow.entity.Operator;
import com.springboot.workflow.entity.model.ProcessTask;
import com.springboot.workflow.entity.model.views.ProcessInstanceViewModel;
import com.springboot.workflow.entity.model.views.ProcessTaskViewModel;
import com.springboot.workflow.util.Serializer;
import lombok.val;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by zx
 */
public class ProcessMapper {

    public static ProcessInstanceViewModel toEntity(HistoricProcessInstance object, String applicationId) {
        val instance = new ProcessInstanceViewModel();
        if (object == null) {
            return instance;
        }
        instance.setApplicationId(applicationId);
        instance.setDefinitionCode(object.getProcessDefinitionKey());
        instance.setDefinitionVersion(object.getProcessDefinitionVersion());
        instance.setId(Long.parseLong(object.getId()));
        instance.setBeginTime(object.getStartTime());
        instance.setEndTime(object.getEndTime());
        instance.setCreator(Operator.valueOf(object.getStartUserId()));
        instance.setBeginActivity(object.getStartActivityId());
        instance.setEndActivity(object.getEndActivityId());
        instance.setBusinessKey(object.getBusinessKey());
        instance.setCondition(object.getProcessVariables());
        return instance;
    }

    public static ProcessTaskViewModel toEntity(HistoricTaskInstance object, ProcessTask.Status status) {
        val task = new ProcessTaskViewModel();
        task.setDefinitionCode(object.getProcessDefinitionId().split(":")[0]);
        task.setDefinitionVersion(Integer.parseInt(object.getProcessDefinitionId().split(":")[1]));
        task.setId(Long.parseLong(object.getId()));
        task.setBeginTime(object.getStartTime());
        task.setEndTime(object.getEndTime());
        task.setInstanceId(Long.parseLong(object.getProcessInstanceId()));
        task.setActivity(object.getTaskDefinitionKey());
        task.setName(object.getName());
        task.setOwner(Operator.valueOf(object.getAssignee())); //owner才是activiti的assignee
        task.setCreator(Operator.valueOf(object.getOwner())); //creator对应activiti的owner，通常为null
        task.setStatus(getStatus(object, status));
        task.setPriority(object.getPriority());
        task.setDueTime(object.getDueDate());
        task.setClaimTime(object.getClaimTime());
        val localVariables = object.getTaskLocalVariables();
        if(localVariables != null && localVariables.size() > 0){
            String taskVariables = (String)localVariables.get(ActivitiConstants.taskVariables);
            if(taskVariables != null){
                task.setProperties(Serializer.deserialize(taskVariables, Map.class));
            }else{
                task.setProperties(localVariables);
            }
        }
        return task;
    }

    private static String getStatus(HistoricTaskInstance task, ProcessTask.Status status) {
        if (status != null) {
            return status.name();
        }
        Map<String ,Object> variables = task.getTaskLocalVariables();
        Object taskVariables = variables.get(ActivitiConstants.taskVariables);
        if(taskVariables != null){
            Map<String ,Object> _variables = Serializer.deserialize(taskVariables.toString(),Map.class);
            if(_variables != null){
                variables = _variables;
            }
        }

        if (task.getEndTime() != null) {
            if(StringUtils.equals(task.getDeleteReason(), ActivitiConstants.deleted)){//关闭任务 deleted
                return ActivitiConstants.deleted;
            }else if(variables.containsKey(ActivitiConstants.autoCompleteType)){//自动审核  autoCompleted
                return ActivitiConstants.autoCompleteStatus;
            }else if(variables.containsKey(ActivitiConstants.approvalName)){//操作人审核 completed
                return ActivitiConstants.completeStatus;
            }else {
                return ActivitiConstants.closeStatus;//自动审核-关闭任务    closed
            }
        } else {
            if (StringUtils.isEmpty(task.getAssignee())) {//有任务-没审核人
                return ProcessTask.Status.Pending.name();
            } else {
                return ProcessTask.Status.Claimed.name();//有任务-有审核人
            }
        }
    }

    public static ProcessTaskResult toHistoryEntity(HistoricTaskInstance object) {
        val task = new ProcessTaskResult();
        task.setDefinitionCode(object.getProcessDefinitionId().split(":")[0]);
        task.setDefinitionVersion(Integer.parseInt(object.getProcessDefinitionId().split(":")[1]));
        task.setTaskId(Long.parseLong(object.getId()));
        task.setBeginTime(object.getStartTime().getTime());
        task.setEndTime(object.getEndTime().getTime());
        task.setInstanceId(Long.parseLong(object.getProcessInstanceId()));
        task.setActivity(object.getTaskDefinitionKey());
        task.setName(object.getName());
        task.setOwner(Operator.valueOf(object.getAssignee())); //owner才是activiti的assignee
//        task.setCreator(Operator.valueOf(object.getOwner())); //creator对应activiti的owner，通常为null
        task.setStatus(getStatus(object, null));
        task.setPriority(object.getPriority());
        task.setDueTime(object.getDueDate() == null ? 0 : object.getDueDate().getTime());
        task.setClaimTime(object.getClaimTime() == null ? 0 : object.getClaimTime().getTime());
        val localVariables = object.getTaskLocalVariables();
        if(localVariables != null && localVariables.size() > 0){
            String taskVariables = (String)localVariables.get(ActivitiConstants.taskVariables);
            if(taskVariables != null){
                task.setProperties(Serializer.deserialize(taskVariables, Map.class));
            }else{
                task.setProperties(localVariables);
            }
        }
        return task;
    }

    public static ProcessTaskResult toTodoEntity(Task object) {
        val task = new ProcessTaskResult();
        task.setDefinitionCode(object.getProcessDefinitionId().split(":")[0]);
        task.setDefinitionVersion(Integer.parseInt(object.getProcessDefinitionId().split(":")[1]));
        task.setTaskId(Long.parseLong(object.getId()));
        task.setBeginTime(object.getCreateTime().getTime());
        task.setInstanceId(Long.parseLong(object.getProcessInstanceId()));
        task.setActivity(object.getTaskDefinitionKey());
        task.setName(object.getName());
        task.setOwner(Operator.valueOf(object.getAssignee())); //owner才是activiti的assignee
//        task.setCreator(Operator.valueOf(object.getOwner())); //creator对应activiti的owner，通常为null
        task.setStatus(ProcessTask.Status.valueOf("Claimed").name());
        task.setPriority(object.getPriority());
        task.setDueTime(object.getDueDate() == null ? 0 : object.getDueDate().getTime());
        task.setClaimTime(object.getClaimTime() == null ? 0 : object.getClaimTime().getTime());
        val localVariables = object.getTaskLocalVariables();
        if(localVariables != null && localVariables.size() > 0){
            String taskVariables = (String)localVariables.get(ActivitiConstants.taskVariables);
            if(taskVariables != null){
                task.setProperties(Serializer.deserialize(taskVariables, Map.class));
            }else{
                task.setProperties(localVariables);
            }
        }
        return task;
    }

//    static ProcessDefinition toEntity(org.activiti.engine.repository.ProcessDefinition object, ProcessDeployRequest request) {
//        val processDefinition = new ProcessDefinition();
////        processDefinition.setId(Long.parseLong(object.getId()));
//        processDefinition.setDefinitionCode(object.getKey());
//        processDefinition.setDefinitionId(object.getId());
//        processDefinition.setDefinitionVersion(object.getVersion());
//        processDefinition.setApplicationId(request.getApplicationId());
//        return processDefinition;
//    }


//    public static List<ProcessTodoTaskDTO> toEntity(List<ProcessTodoTask> entities) {
//        val result = new ArrayList<ProcessTodoTaskDTO>();
//        if (entities == null) {
//            return result;
//        }
//        for (val entity : entities) {
//            ProcessTodoTaskDTO task = new ProcessTodoTaskDTO()
//                    .setTaskId(entity.getTaskId())
//                    .setStatus(entity.getStatus())
//                    .setActivity(entity.getActivity())
//                    .setName(entity.getName())
//                    .setPriority(entity.getPriority());
//            if (entity.getOwner() != null) {
//                task = task.setOwner(new OperatorDTO(entity.getOwner().getId(), entity.getOwner().getName()));
//            }
//            result.add(task);
//        }
//        return result;
//    }
}
