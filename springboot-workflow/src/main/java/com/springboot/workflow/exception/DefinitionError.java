package com.springboot.workflow.exception;

import com.springboot.workflow.entity.requests.ProcessRequest;

public class DefinitionError {
    public static final int FLOW_MODEL_IS_NULL = 600;
    public static final int FLOW_OBJECT_NOT_FOUND = 601;
    public static final int APPLICATION_NOT_FOUND = 602;
    public static final int FLOW_OBJECT_ID_IS_REPEAT = 610;
    public static final int EVENT_START_IN_0_OUT_1 = 611;
    public static final int EVENT_STOP_IN_1_OUT_0 = 612;
    public static final int GATEWAY_FORK_IN_1 = 613;
    public static final int GATEWAY_JOIN_OUT_1 = 614;
    public static final int ACTIVITY_IN_1_OUT_1 = 615;
    public static final int ACTIVITY_NOT_FOUND = 616;
    public static final int ACTIVITY_CODE_IS_NULL = 617;
    public static final int ACTIVITY_CODE_IS_REPEAT = 618;
    public static final int START_EVENT_AT_LEAST_ONE = 619;
    public static final int STOP_EVENT_AT_LEAST_ONE = 620;
    public static final int TRANSITION_FROM_NOT_FOUND = 621;
    public static final int TRANSITION_TO_NOT_FOUND = 622;
    public static final int EXIST_WORKFLOW_RING = 630;
    public static final int FLOW_SHOULD_BEGIN_FROM_START_EVENT = 631;
    public static final int FLOW_SHOULD_END_FROM_STOP_EVENT = 632;

    //以505开头的表示服务器运行的流程定义发生了错误
    private static final int PROCESS_DEFINITION_ERROR = 505;
    public static final int PROCESS_DEFINITION_CODE_NOT_FOUND = PROCESS_DEFINITION_ERROR * 1000 + 1;

    private static String commonDefinitionError(ProcessRequest request) {
        return String.format("process code [%s], version [%d], operator [%s]",
                request.getDefinitionCode(), request.getDefinitionVersion(),
                request.getOperator().toString());
    }

    //流程定义不存在
    public static ProcessException processDefinitionCodeNotFound(ProcessRequest request) {
        return new ProcessException(PROCESS_DEFINITION_CODE_NOT_FOUND,
                String.format("%s, process definition code not found", commonDefinitionError(request)));
    }
}
