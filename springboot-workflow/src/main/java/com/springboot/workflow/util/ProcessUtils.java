package com.springboot.workflow.util;

import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.ProcessEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProcessUtils {
    private static final Map<String, Process> processCache = new ConcurrentHashMap();

    public static Process getProcess(ProcessEngine processEngine, String procDefId) {
        Process process;
        if (processCache.containsKey(procDefId)) {
            return processCache.get(procDefId);
        }
        try {
            return doGetProcess(processEngine, procDefId);
        } catch (ActivitiIllegalArgumentException e) {
            process = doGetProcess(processEngine, procDefId);
        }

        return process;
    }

    public static Process doGetProcess(ProcessEngine processEngine, String procDefId) throws ActivitiIllegalArgumentException {
        BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(procDefId);
        return bpmnModel.getProcesses().get(bpmnModel.getProcesses().size() - 1);
    }

    public static List<UserTask> getNextNode(ProcessEngine processEngine, String procDefId, String taskDefKey, Map<String, Object> map) {
        List userTasks = new ArrayList();

        Process process = getProcess(processEngine, procDefId);

        Collection flowElements = process.getFlowElements();

        FlowElement flowElement = null;
        if (!StringUtils.isEmpty(taskDefKey))
            flowElement = getFlowElementById(taskDefKey, flowElements);
        else {
            flowElement = getStartFlowElement(flowElements);
        }
        getNextNode(flowElements, flowElement, map, userTasks);

        return userTasks;
    }

    private static void getNextNode(Collection<FlowElement> flowElements, FlowElement flowElement, Map<String, Object> map, List<UserTask> nextUser) {
        if ((flowElement instanceof EndEvent))
        {
            if (getSubProcess(flowElements, flowElement) != null) {
                flowElement = getSubProcess(flowElements, flowElement);
            }

        }

        List<SequenceFlow> outGoingFlows = null;
        if ((flowElement instanceof Task))
            outGoingFlows = ((Task)flowElement).getOutgoingFlows();
        else if ((flowElement instanceof Gateway))
            outGoingFlows = ((Gateway)flowElement).getOutgoingFlows();
        else if ((flowElement instanceof StartEvent))
            outGoingFlows = ((StartEvent)flowElement).getOutgoingFlows();
        else if ((flowElement instanceof SubProcess)) {
            outGoingFlows = ((SubProcess)flowElement).getOutgoingFlows();
        }

        if ((outGoingFlows != null) && (outGoingFlows.size() > 0)) {
            for (SequenceFlow sequenceFlow : outGoingFlows) {
                String expression = sequenceFlow.getConditionExpression();
                if ((StringUtils.isEmpty(expression)) ||
                        (Boolean.valueOf(String.valueOf(ActivitiElSupport.result(map, expression))))) {
                    String nextFlowElementID = sequenceFlow.getTargetRef();

                    FlowElement nextFlowElement = getFlowElementById(nextFlowElementID, flowElements);

                    if ((nextFlowElement instanceof UserTask)) {
                        nextUser.add((UserTask)nextFlowElement);
                    } else if ((nextFlowElement instanceof ExclusiveGateway)) {
                        getNextNode(flowElements, nextFlowElement, map, nextUser);
                    } else if ((nextFlowElement instanceof ParallelGateway)) {
                        getNextNode(flowElements, nextFlowElement, map, nextUser);
                    } else if ((nextFlowElement instanceof ReceiveTask)) {
                        getNextNode(flowElements, nextFlowElement, map, nextUser);
                    } else if ((nextFlowElement instanceof StartEvent)) {
                        getNextNode(flowElements, nextFlowElement, map, nextUser);
                    } else if ((nextFlowElement instanceof EndEvent))
                        getNextNode(flowElements, nextFlowElement, map, nextUser);
                }
            }
        }
    }

    private static FlowElement getSubProcess(Collection<FlowElement> flowElements, FlowElement flowElement) {
        for (FlowElement flowElement1 : flowElements) {
            if ((flowElement1 instanceof SubProcess)) {
                for (FlowElement flowElement2 : ((SubProcess) flowElement1).getFlowElements()) {
                    if (flowElement.equals(flowElement2)) {
                        return flowElement1;
                    }
                }
            }
        }
        return null;
    }

    private static FlowElement getFlowElementById(String Id, Collection<FlowElement> flowElements) {
        for (FlowElement flowElement : flowElements) {
            if (flowElement.getId().equals(Id))
            {
                if ((flowElement instanceof SubProcess)) {
                    return getStartFlowElement(((SubProcess)flowElement).getFlowElements());
                }
                return flowElement;
            }

            if ((flowElement instanceof SubProcess)) {
                FlowElement flowElement1 = getFlowElementById(Id, ((SubProcess)flowElement).getFlowElements());
                if (flowElement1 != null) {
                    return flowElement1;
                }
            }
        }
        return null;
    }

    private static FlowElement getStartFlowElement(Collection<FlowElement> flowElements) {
        for (FlowElement flowElement : flowElements) {
            if ((flowElement instanceof StartEvent)) {
                return flowElement;
            }
        }
        return null;
    }
}