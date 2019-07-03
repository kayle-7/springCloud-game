package com.springboot.workflow.exception;

import com.springboot.workflow.entity.requests.ProcessInstanceRequest;
import org.activiti.engine.task.Task;

public class InstanceError {
    public static final int FLOW_INSTANCE_NOT_FOUND = 701;
    public static final int TRANSITION_INSTANCE_NOT_FOUND = 702;
    public static final int GATEWAY_OUT_TRANSITION_IS_NULL = 708;
    public static final int GATEWAY_IN_TRANSITION_IS_NULL = 709;
    public static final int PROCESS_INSTANCE_TERMINATED = 711;
    public static final int APPLICATION_ID_ERROR = 721;
    public static final int OPERATOR_ILLEGAL = 724;
    public static final int PROCESS_INSTANCE_DATA_IS_NULL = 731;
    public static final int PROCESS_TASK_ASSIGNEE_MISS_EXCEPTION = 737;
    public static final int PROCESS_TASK_DATE_NULL_EXCEPTION = 738;
    public static final int PROCESS_CALL_BACK_DATA_NULL_EXCEPTION = 739;
    public static final int PROCESS_ACTIVITY_NOT_EXIST = 740;
    public static final int PROCESS_COUNTER_SIGN_ACTION_EXCEPTION = 741;
    public static final int PROCESS_COUNTER_SIGN_REDUCE_EXCEPTION = 742;
    private static final int PROCESS_INSTANCE_ERROR = 506;
    public static final int PROCESS_INSTANCE_NOT_FOUND = 506001;
    public static final int PROCESS_INSTANCE_HAS_STOPPED = 506002;
    public static final int PROCESS_INSTANCE_NOT_ACTIVATED = 506003;
    public static final int PROCESS_BUSINESS_KEY_MORE_THEN_ONE_INSTANCE = 511061;
    public static final int PROCESS_BUSINESS_KEY_NOT_FOUND = 511062;
    public static final int PROCESS_TASK_MISS_EXCEPTION = 516121;
    public static final int PROCESS_TASK_NOT_FOUND = 516122;
    public static final int PROCESS_TASK_ACTIVITY_ERROR = 516123;
    public static final int PROCESS_TASK_OPERATOR_ERROR = 516124;
    public static final int PROCESS_TASK_HAS_EXPIRED = 516125;
    public static final int PROCESS_ACTIVITY_JUST_ONLY_ONE = 521181;
    public static final int PROCESS_ACTIVITY_NOT_FOUND = 521182;
    public static final int PROCESS_ACTIVITY_ARE_NOT_HISTORY_NODE = 521183;
    public static final int PROCESS_NOT_ALLOWED_TRANSFER_TO_YOURSELF = 526241;
    public static final int PROCESS_PERMISSION_DESERIALIZE_ERROR = 506099;

    public static ProcessException permissionDeserialize(String instanceId) {
        return new ProcessException(506099,
                String.format("instanceId [%s] ,permission string deserialize error.", instanceId));
    }

    private static String commonInstanceError(ProcessInstanceRequest request) {
        return String.format("process code [%s], version [%d], instance [%d], operator [%s]",
                request.getDefinitionCode(),
                request.getDefinitionVersion(),
                request.getInstanceId(),
                request.getOperator().toString());
    }

    public static ProcessException processInstanceNotFound(ProcessInstanceRequest request) {
        return new ProcessException(506001,
                String.format("%s, process instance not found", commonInstanceError(request)));
    }

    public static ProcessException processNodeNotFound(ProcessInstanceRequest request) {
        return new ProcessException(506003,
                String.format("%s, process Node not found", commonInstanceError(request)));
    }
    public static ProcessException instanceStopped(ProcessInstanceRequest request) {
        return new ProcessException(506002,
                String.format("%s, process instance has stopped.", commonInstanceError(request)));
    }

    public static ProcessException activeProcessMoreThenOneInstance(ProcessInstanceRequest request, String businessKey) {
        return new ProcessException(511061,
                String.format("%s, process businessKey [%s] has more then one instance.", commonInstanceError(request),
                        businessKey));
    }

    public static ProcessException processInstanceNotActivated(ProcessInstanceRequest request, String businessKey) {
        return new ProcessException(506003,
                String.format("%s, process businessKey [%s] not activated.", commonInstanceError(request),
                        businessKey));
    }

    public static ProcessException taskMissException(ProcessInstanceRequest request) {
        return new ProcessException(516121,
                String.format("%s, task miss.", commonInstanceError(request)));
    }

    public static ProcessException activityJustOnlyOne(ProcessInstanceRequest request) {
        return new ProcessException(521181,
                String.format("%s, run to activity just only one.", commonInstanceError(request)));
    }

    public static ProcessException activityOwnersOnlyOne(ProcessInstanceRequest request) {
        return new ProcessException(521181,
                String.format("%s, activity owner just only one.", commonInstanceError(request)));
    }

    public static ProcessException taskNotFound(ProcessInstanceRequest request, String activity, long taskId) {
        return new ProcessException(516122,
                String.format("%s, activity [%s], task [%d], task not found.", commonInstanceError(request),
                        activity, taskId));
    }

    public static ProcessException taskActivityError(ProcessInstanceRequest request, String activity, long taskId) {
        return new ProcessException(516123,
                String.format("%s, activity [%s], task [%d], task activity error.", commonInstanceError(request),
                        activity, taskId));
    }

    public static ProcessException taskOperatorError(ProcessInstanceRequest request, String activity, long taskId) {
        return new ProcessException(516124,
                String.format("%s, activity [%s], task [%d], task operator error.", commonInstanceError(request),
                        activity, taskId));
    }

    public static ProcessException taskHasExpired(ProcessInstanceRequest request, String activity, long taskId) {
        return new ProcessException(516125,
                String.format("%s, activity [%s], task [%d], task has expired.", commonInstanceError(request),
                        activity, taskId));
    }

    public static ProcessException businessKeyNotFound(long instanceId) {
        return new ProcessException(511062,
                String.format("process instance [%d] business key not found", instanceId));
    }

    public static ProcessException activityNotFound(ProcessInstanceRequest request, String activity) {
        return new ProcessException(521182,
                String.format("%s, activity [%s], activity not found.", commonInstanceError(request),
                        activity));
    }

    public static ProcessException activityAreNotHistoryNode(ProcessInstanceRequest request, String activity) {
        return new ProcessException(521183,
                String.format("%s, activity [%s], activity are not history node.", commonInstanceError(request),
                        activity));
    }

    public static ProcessException notAllowedTransferToYourself(ProcessInstanceRequest request, String activity) {
        return new ProcessException(526241,
                String.format("%s, activity [%s], not allowed transfer to yourself.", commonInstanceError(request),
                        activity));
    }

    public static ProcessException tansferorSameAsAssignee(Task task) {
        return new ProcessException(526241,
                String.format("task id %s, assignee [%s], tansferor same as assignee.", task.getId(), task.getAssignee()));
    }
}