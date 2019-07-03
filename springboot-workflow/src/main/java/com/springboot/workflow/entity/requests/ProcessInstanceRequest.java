package com.springboot.workflow.entity.requests;

import com.springboot.workflow.entity.model.ProcessDoneTask;
import com.springboot.workflow.entity.model.ProcessTask;
import com.springboot.workflow.entity.model.ProcessTodoTask;
import com.springboot.workflow.entity.results.ProcessInstanceResult;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class ProcessInstanceRequest extends ProcessRequest {

    @NotNull(message = "process definition code is null.")
    @Size(min = 5, max = 60, message = "process definition code size must be {min} to {max}.")
    String definitionCode; //流程唯一标识

    long instanceId; //流程实例ID

    public ProcessTaskRequest toTaskRequest(ProcessTask.Status status) {
        val request = toTaskRequest();
        request.setOperator(getOperator());
        request.setStatus(status);
        request.setIncludeComments(true);
        return request;
    }

    public ProcessTaskRequest toTaskRequest() {
        val taskRequest = new ProcessTaskRequest();
        taskRequest.setDefinitionVersion(getDefinitionVersion());
        taskRequest.setInstanceId(getInstanceId());
        taskRequest.setDefinitionCode(getDefinitionCode());
        return taskRequest;
    }

    public ProcessInstanceResult toResult(List<ProcessTodoTask> todoTasks, List<ProcessDoneTask> doneTasks, String status) {
        val result = new ProcessInstanceResult();
        result.setInstanceId(getInstanceId());
        result.setDefinitionCode(getDefinitionCode());
        result.setDefinitionVersion(getDefinitionVersion());
        result.setStatus(status);
        if (!CollectionUtils.isEmpty(doneTasks)) {
            result.setDoneTasks(doneTasks);
            for (val task : doneTasks) {
                result.getBeforeActivities().add(task.getActivity());
            }
        }
        if (!CollectionUtils.isEmpty(todoTasks)) {
            result.setTodoTasks(todoTasks);
            for (val task : todoTasks) {
                result.getCurrentActivities().add(task.getActivity());
            }
        }
        return result;
    }
}
