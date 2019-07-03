package com.springboot.workflow.entity.results;

import com.springboot.workflow.entity.model.ProcessDoneTask;
import com.springboot.workflow.entity.model.ProcessTodoTask;
import lombok.Data;

import java.util.*;

/**
 * Created by zx
 */
@Data
public class ProcessInstanceResult {

    private String definitionCode;
    private int definitionVersion;
    private long instanceId;
    private String status;
    private Set<String> beforeActivities = new HashSet();
    private List<ProcessDoneTask> doneTasks = new ArrayList();
    private Set<String> currentActivities = new HashSet();
    private List<ProcessTodoTask> todoTasks = new ArrayList();

    private String getCondition(Map<String, Object> conditions, String conditionName) {
        Object object = conditions.getOrDefault(conditionName, "");
        if (object != null) {
            return object.toString();
        }
        return "";
    }
}