package com.springboot.workflow.exception;

import com.springboot.workflow.entity.Operator;

public class ProcessException extends RuntimeException {
    private int code;

    ProcessException(int code, String message) {
        super(message);
        setCode(code);
    }

    public ProcessException(int code, String message, Throwable t) {
        super(message, t);
        setCode(code);
    }

    public static ProcessException badRequest(String message) {
        return new ProcessException(400, message);
    }

    public static ProcessException serverInternalError()
    {
        return new ProcessException(500, "internal server error");
    }

    public static ProcessException flowModelIsNull(String content) {
        return new ProcessException(600,
                String.format("flow model is null[content=%s] ", content));
    }

    public static ProcessException flowObjectNotFound(long id) {
        return new ProcessException(601,
                String.format("flow object [id=%d] is null", id));
    }

    public static ProcessException flowObjectIdIsRepeat(long id) {
        return new ProcessException(610,
                String.format("flow object [id=%d] id is repeat", id));
    }

    public static ProcessException flowObjectNotFound(String code) {
        return new ProcessException(601,
                String.format("flow object [definition code=%s] is null", code));
    }

    public static ProcessException applicationNotFound(long id) {
        return new ProcessException(602,
                String.format("application [id=%d] is null", id));
    }

    public static ProcessException startEventMustIn0Out1(long id) {
        return new ProcessException(611,
                String.format("start event [id=%d] in degree = 0 and out degree = 1", id));
    }

    public static ProcessException stopEventMustIn1Out0(long id) {
        return new ProcessException(612,
                String.format("stop event [id=%d] in degree = 1 and out degree = 0", id));
    }

    public static ProcessException forkGatewayMustIn1(long id) {
        return new ProcessException(613,
                String.format("fork gateway [id=%d] in degree = 1", id));
    }

    public static ProcessException joinGatewayMustOut1(long id) {
        return new ProcessException(614,
                String.format("join gateway [id=%d] out degree = 1", id));
    }

    public static ProcessException activityMustIn1Out1(long id) {
        return new ProcessException(615,
                String.format("activity [id=%d] in degree = 1 and out degree = 1", id));
    }

    public static ProcessException activityCodeIsNull(long id) {
        return new ProcessException(617,
                String.format("activity [id=%d]  is null", id));
    }

    public static ProcessException activityCodeIsRepeat(long id, long repeatId) {
        return new ProcessException(618,
                String.format("activity [id=%d] definitionId is repeat[repeatId=%d]", id,
                        repeatId));
    }

    public static ProcessException activityNotFound() {
        return new ProcessException(616, "activity not found");
    }

    public static ProcessException startEventAtLeastOne() {
        return new ProcessException(619, "start event at least one");
    }

    public static ProcessException stoptEventAtLeastOne() {
        return new ProcessException(620, "stop event at least one");
    }

    public static ProcessException transitionFromNotFound(long id) {
        return new ProcessException(621,
                String.format("transition [id=%d] from not found", id));
    }

    public static ProcessException transitionToNotFound(long id) {
        return new ProcessException(622,
                String.format("transition [id=%d] to not found", id));
    }

    public static ProcessException existWorkflowRing(String code) {
        return new ProcessException(630,
                String.format("exist workflow ring [definitionId=%s]", code));
    }

    public static ProcessException flowShouldBeginFromStartEvent(long id) {
        return new ProcessException(631,
                String.format("flow [id=%d] should begin from StartEvent", id));
    }

    public static ProcessException flowShouldEndFromStopEvent(long id) {
        return new ProcessException(632,
                String.format("flow [id=%d] should end from StopEvent", id));
    }

    public static ProcessException gatewayOutTransitionIsNull(long id) {
        return new ProcessException(708,
                String.format("gateway [id=%d] out connectingObjects is null", id));
    }

    public static ProcessException gatewayInTransitionIsNull(long id)
    {
        return new ProcessException(709,
                String.format("gateway [id=%d] in connectingObjects is null", id));
    }

    public static ProcessException flowObjectInstanceNotFound(long id) {
        return new ProcessException(701,
                String.format("instance [id=%d] is null", id));
    }

    public static ProcessException transitionInstanceNotFound(long id) {
        return new ProcessException(702,
                String.format("connectingObject instance [id=%d] is null", id));
    }

    public static ProcessException processInstanceTerminated(long id) {
        return new ProcessException(711,
                String.format("instance [id=%d] terminated", id));
    }

    public static ProcessException applicationIdError(String sourceId, String targetId) {
        return new ProcessException(721,
                String.format("application id error[sourceId=%s][targetId=%s]", sourceId, targetId));
    }

    public static ProcessException operatorIllegal(Operator operator) {
        return new ProcessException(724,
                String.format("creator illegal [operator=%s]", operator == null ? "null" : operator
                        .toString()));
    }

    public static ProcessException taskAssigneeMissException(String taskId) {
        return new ProcessException(737,
                String.format("taskId [%s] ,task assingee miss !", taskId));
    }

    public static ProcessException callbackNullException(String instanceId, String taskId) {
        return new ProcessException(739,
                String.format("instanceId [%s] ,task [%s] call back return data null !", instanceId, taskId));
    }

    public static ProcessException historyTaskDateNullException(String taskId) {
        return new ProcessException(738,
                String.format("task [%s] start date is null !", taskId));
    }

    public static ProcessException activityNotExist(String activity) {
        return new ProcessException(740,
                String.format("Process Activity[%s] Not exist!", activity));
    }

    public static ProcessException counterSignActiveTaskNotExist() {
        return new ProcessException(741,
                "Process counterSign active tasks not exists!");
    }

    public static ProcessException counterSignReduceException() {
        return new ProcessException(742,
                "Process counterSign reduce exception!");
    }

    public int getCode()
    {
        return this.code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }
}