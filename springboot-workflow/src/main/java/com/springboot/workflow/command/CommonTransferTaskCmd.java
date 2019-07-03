package com.springboot.workflow.command;

import com.springboot.workflow.constants.ActivitiConstants;
import com.springboot.workflow.entity.model.ProcessDoneTask;
import com.springboot.workflow.entity.requests.ProcessTransferRequest;
import com.springboot.workflow.server.impl.WorkflowExecuteServiceImpl;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntityImpl;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 跳转策略：
 * a.当前活动的任务节点中仅包含目标节点，无需跳转。[幂等]
 * b.当前活动的任务节点中既包含了目标节点还包含了其他活动节点，报错。[幂等]
 * c.当前活动的任务节点不包含目标节点，清空当前的活动任务，并完成相应的任务历史，最后从目标节点处执行流转逻辑。
 */
public class CommonTransferTaskCmd extends AbstractCmd<List<ProcessDoneTask>> {
    private final ProcessTransferRequest request;
//    private CommandContext commandContext;
    private Task task;
    private WorkflowExecuteServiceImpl workflowExecuteService;
    /**
     * Instantiates a new Common jump task cmd.
     *
     * @param request the request
     */
    public CommonTransferTaskCmd(ProcessTransferRequest request, Task task, WorkflowExecuteServiceImpl workflowExecuteService) {
        this.request = request;
        this.task = task;
        this.workflowExecuteService = workflowExecuteService;
    }

    @Override
    public List<ProcessDoneTask> doExecute(CommandContext commandContext) {
        recordOperationWithHistoricTask(commandContext, ActivitiConstants.operationTransfer);
        return null;
    }

    private String getTaskOperation(){
        Object operation = request.getConditions().get(ActivitiConstants.operation);
        return operation == null ? ActivitiConstants.deleted : operation.toString();
    }

    private void recordOperationWithHistoricTask(CommandContext commandContext, String operation) {
        String taskId = commandContext.getProcessEngineConfiguration().getIdGenerator().getNextId();
        HistoricTaskInstanceEntity hisTask = new HistoricTaskInstanceEntityImpl();
        hisTask.setId(taskId);
        hisTask.setAssignee(request.getOperator().toString());
        hisTask.setTaskDefinitionKey(task.getTaskDefinitionKey());
        hisTask.setName(task.getName());
        hisTask.setProcessInstanceId(Long.toString(request.getInstanceId()));
        hisTask.setProcessDefinitionId(task.getProcessDefinitionId());
        hisTask.setStartTime(task.getCreateTime());
        hisTask.setClaimTime(new Date());
        hisTask.markEnded(operation);
        hisTask.setPriority(ActivitiConstants.taskDefaultPriority);
        if(workflowExecuteService.isMultiInstanceNodes(task.getProcessDefinitionId(), task.getTaskDefinitionKey())){
            // 多实例节点用不同的ExecutionId
            String executionId = commandContext.getProcessEngineConfiguration().getIdGenerator().getNextId();
            hisTask.setExecutionId(executionId);
        }else{
            // 单实例节点用相同的ExecutionId
            hisTask.setExecutionId(task.getExecutionId());
        }
        commandContext.getHistoricTaskInstanceEntityManager().insert(hisTask, false);
        getConditions().put(ActivitiConstants.transferInfo, request.getTransferor().toString());
//        opinionNameFormat();
        historicTaskComments(hisTask,null, true);
    }

    private void opinionNameFormat(){
        Map<String,Object> conditions = getConditions();
        Object operationValue = conditions.get(ActivitiConstants.operation);
        Object opinionValue = conditions.get(ActivitiConstants.opinionName);
        String opinion = null;
        if(Objects.nonNull(operationValue) && StringUtils.equals(operationValue.toString(), ActivitiConstants.operationTransfer)){
            if(Objects.nonNull(opinionValue)){
                opinion = String.format(ActivitiConstants.taskTransferInfoExt, request.getOperator(),request.getTransferor(),opinionValue.toString());
            }else {
                opinion = String.format(ActivitiConstants.taskTransferInfo,request.getOperator(),request.getTransferor());
            }
        }else{
            opinion = Objects.isNull(opinionValue) ? null:opinionValue.toString();
        }
        conditions.put(ActivitiConstants.transferInfo,request.getTransferor());
        conditions.put(ActivitiConstants.opinionName,opinion);
    }

    @Override
    public Map<String, Object> getConditions() {
        return request.getConditions();
    }
}
