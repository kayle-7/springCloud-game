package com.springboot.workflow.server.impl;

import com.springboot.workflow.command.CommonJumpTaskCmd;
import com.springboot.workflow.command.CommonTransferTaskCmd;
import com.springboot.workflow.constants.ActivitiConstants;
import com.springboot.workflow.entity.Approver;
import com.springboot.workflow.entity.Operator;
import com.springboot.workflow.entity.model.ProcessDoneTask;
import com.springboot.workflow.entity.model.ProcessInstance;
import com.springboot.workflow.entity.model.ProcessTodoTask;
import com.springboot.workflow.entity.requests.*;
import com.springboot.workflow.entity.results.ProcessInstanceResult;
import com.springboot.workflow.exception.InstanceError;
import com.springboot.workflow.exception.WorkflowException;
import com.springboot.workflow.server.AbstractProcessCommonService;
import com.springboot.workflow.server.WorkflowExecuteService;
import com.springboot.workflow.util.RegexUtils;
import com.springboot.workflow.util.Serializer;
import com.springboot.workflow.util.ValidationUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zx
 */
@Slf4j
@Component
public class WorkflowExecuteServiceImpl extends AbstractProcessCommonService implements WorkflowExecuteService {

    @Override
    public ProcessInstanceResult start(@NonNull final ProcessStartRequest request) throws WorkflowException {
        validateStart(request);

        initProcessDefinitionId(request);
        multiInstanceTaskReady(request);
        createOrGetInstance(request); //启动流程实例
        val todoTasks = getTodoTask(request);
        val startActivity = getInstance(request).getStartActivityId();
        val result = request.toResult(todoTasks, null, ProcessInstance.Status.Running.name());
        result.getBeforeActivities().add(startActivity);
        return result;
    }

    @Override
    public ProcessInstanceResult submit(@NonNull final ProcessSubmitRequest request) throws WorkflowException {
        validateSubmit(request);

        initProcessDefinitionId(request);
        multiInstanceTaskReady(request);
        request.getConditions().put(ActivitiConstants.operation, ActivitiConstants.operationSubmit);//_@operation    submit
        return doSubmit(request);
    }

    @Override
    public ProcessInstanceResult rollback(@NonNull final ProcessRollbackRequest request) throws WorkflowException {
        validateRollback(request);
        return rollbackAndWithdraw(request, ActivitiConstants.operationRollback);
    }

    @Override
    public ProcessInstanceResult withdraw(@NonNull final ProcessRollbackRequest request) throws WorkflowException {
        validateWithdraw(request);
        return rollbackAndWithdraw(request, ActivitiConstants.operationWithdraw);
    }

    private ProcessInstanceResult rollbackAndWithdraw(ProcessRollbackRequest request, String operation) {
        initProcessDefinitionId(request); //初始化流程ID，后续考虑用缓存
        checkTargetActivity(request); //检查target是否在历史任务中并签和或签
        multiInstanceTaskReady(request);
        request.getConditions().put(ActivitiConstants.operation, operation);
        val doneTasks = getProcessEngine().getManagementService().executeCommand(new CommonJumpTaskCmd(request, operation)); //驳回
        List<ProcessTodoTask> todoTasks = getTodoTask(request);
        return toResult(request, doneTasks, todoTasks);
    }

    @Override
    public ProcessInstanceResult runTo(@NonNull final ProcessRunToRequest request) throws WorkflowException {
        validateRunTo(request);

        initProcessDefinitionId(request);
        multiInstanceTaskReady(request);
        request.getConditions().put(ActivitiConstants.operation, ActivitiConstants.operationRunTo);
        val doneTasks = getProcessEngine().getManagementService().executeCommand(new CommonJumpTaskCmd(request, ActivitiConstants.operationRunTo)); //跳转
        mergeTaskOwners(request); //合并权限系统的任务所属人（审批人）
        val todoTasks = claimTasks(request);
        if (CollectionUtils.isEmpty(todoTasks)) {
            return terminatedResult(request, todoTasks, doneTasks);
        } else {
            return request.toResult(todoTasks, doneTasks, ProcessInstance.Status.Running.name());
        }
    }

    @Override
    public ProcessInstanceResult transfer(@NonNull final ProcessTransferRequest request) throws WorkflowException {
        validateTransfer(request);

        if (stopped(request)) {
            throw InstanceError.instanceStopped(request);
        }
        if (request.getOperator().toString().equals(request.getTransferor().toString())) {
            throw InstanceError.notAllowedTransferToYourself(request, request.getCurrentActivity());
        }
        List<Task> tasks = getTodoTasks(request, request.getOperator().toString(), request.getCurrentActivity(), request.getTaskId());
        if (CollectionUtils.isEmpty(tasks)) {
            throw InstanceError.taskNotFound(request, request.getCurrentActivity(), request.getTaskId());
        }
        if (tasks.size() > 1) {
            throw InstanceError.taskActivityError(request, request.getCurrentActivity(), request.getTaskId());
        }

        request.getConditions().put(ActivitiConstants.operation, ActivitiConstants.operationTransfer);
        Task task = tasks.get(0);

        Operator taskTransfer = request.getTransferor();
        claim(task, taskTransfer);
//        addComments(request.getConditions(),task.getId(),null,false); //任务没有完成，可以不用加入注解内容
        //增加转交记录
        getProcessEngine().getManagementService().executeCommand(new CommonTransferTaskCmd(request, task, this));

        return request.toResult(new ArrayList<ProcessTodoTask>(){ { add(convertToProcessTodoTask(task));}},
                null,
                ProcessInstance.Status.Running.name());
    }

    @Override
    public void stop(@NonNull final ProcessInstanceRequest request, final String remark) throws WorkflowException {
        validateStop(request);

        if (!stopped(request)){
            getProcessEngine().getRuntimeService().suspendProcessInstanceById(Long.toString(request.getInstanceId()));
            getProcessEngine().getRuntimeService().deleteProcessInstance(Long.toString(request.getInstanceId()), remark);
        }
    }

    @Override
    public ProcessInstanceResult update(ProcessDataRequest paramProcessDataRequest) throws WorkflowException {
        return null;
    }

    //region    参数校验===============================================
    private static void validateStart(ProcessStartRequest request) throws WorkflowException {
        ValidationUtil.checkBlank(request.getDefinitionCode(), "process definition code is blank.");
        ValidationUtil.checkStringSize(request.getBusinessKey(), 1, 250, "process business key range must be 1to 250.");
        RegexUtils.checkData(request.getBusinessKey(),
                "^[\\w-]*$",
                "process business key must be letter(A-Z/a-z), digital(0-9), underline(_) and joiner(-).");
        ValidationUtil.checkOwner(request.getTaskOwners(), "workflow start task owners");
        ValidationUtil.checkOperator(request.getOperator(), "workflow start ");

    }

    private static void validateSubmit(ProcessSubmitRequest request) throws WorkflowException {
        ValidationUtil.checkNumberMin(request.getInstanceId(), 1L, "process instance id < 1L.");
        ValidationUtil.checkOwner(request.getTaskOwners(), "workflow submit task owners");
        ValidationUtil.checkNumberMin(request.getTaskId(), 1L, "process task id < 1L.");
        ValidationUtil.checkOperator(request.getOperator(), "workflow submit");
        ValidationUtil.checkStringSize(request.getCurrentActivity(), 1, 60, "process current activity size must be 1 to 60.");
        RegexUtils.checkData(request.getCurrentActivity(),
                "^[\\w-]*$",
                "process current activity must be letter(A-Z/a-z), digital(0-9), underline(_) and joiner(-).");
    }

    private static void validateRollback(ProcessRollbackRequest request) throws WorkflowException {
        ValidationUtil.checkNumberMin(request.getInstanceId(), 1L, "process instance id < 1L.");
        ValidationUtil.checkNumberMin(request.getTaskId(), 1L, "process task id < 1L.");
        ValidationUtil.checkOperator(request.getOperator(), "workflow rollback ");

        ValidationUtil.checkStringSize(request.getTargetActivity(), 1, 60, "process target activity size must be 1 to 60.");
        RegexUtils.checkData(request.getTargetActivity(),
                "^[\\w-]*$",
                "process target activity must be letter(A-Z/a-z), digital(0-9), underline(_) and joiner(-).");
//        request.setDefinitionCode("expenseAccount");
    }

    private static void validateWithdraw(ProcessRollbackRequest request) throws WorkflowException {
        ValidationUtil.checkNumberMin(request.getInstanceId(), 1L, "process instance id < 1L.");
        ValidationUtil.checkOperator(request.getOperator(), "workflow rollback ");

        ValidationUtil.checkStringSize(request.getTargetActivity(), 1, 60, "process target activity size must be 1 to 60.");
        RegexUtils.checkData(request.getTargetActivity(),
                "^[\\w-]*$",
                "process target activity must be letter(A-Z/a-z), digital(0-9), underline(_) and joiner(-).");
//        request.setDefinitionCode("expenseAccount");
    }

    private static void validateRunTo(ProcessRunToRequest request) throws WorkflowException {
        ValidationUtil.checkNumberMin(request.getInstanceId(), 1L, "process instance id < 1L.");
        ValidationUtil.checkNumberMin(request.getTaskId(), 1L, "process task id < 1L.");
        ValidationUtil.checkOperator(request.getOperator(), "workflow rollback ");

        ValidationUtil.checkStringSize(request.getTargetActivity(), 1, 60, "process target activity size must be 1 to 60.");
        RegexUtils.checkData(request.getTargetActivity(),
                "^[\\w-]*$",
                "process target activity must be letter(A-Z/a-z), digital(0-9), underline(_) and joiner(-).");
//        request.setDefinitionCode("expenseAccount");
    }

    private static void validateTransfer(ProcessTransferRequest request) throws WorkflowException {
        ValidationUtil.checkNumberMin(request.getInstanceId(), 1L, "process instance id < 1L.");
        ValidationUtil.checkNumberMin(request.getTaskId(), 1L, "process task id < 1L.");
        ValidationUtil.checkOperator(request.getOperator(), "workflow transfer ");
        ValidationUtil.checkOperator(request.getTransferor(), "workflow transfer ");

        ValidationUtil.checkStringSize(request.getCurrentActivity(), 1, 60, "process current activity size must be 1 to 60.");
        RegexUtils.checkData(request.getCurrentActivity(),
                "^[\\w-]*$",
                "process current activity must be letter(A-Z/a-z), digital(0-9), underline(_) and joiner(-).");
//        request.setDefinitionCode("expenseAccount");

    }

    private static void validateStop(ProcessInstanceRequest request) throws WorkflowException {
        ValidationUtil.checkNumberMin(request.getInstanceId(), 1L, "process instance id < 1L.");
    }
    //endregion    参数校验===============================================

    //region ========================================================
    private void createOrGetInstance(ProcessStartRequest request) {
        request.getConditions().put(ActivitiConstants.creatorName, request.getOperator().toString());
        // 2. 保证等幂性原则，通过processDefinitionId和businessKey查询是否已经有流程启动。businessKey是业务唯一编码
        val query = getProcessEngine().getRuntimeService().createProcessInstanceQuery()
                .processDefinitionKey(request.getDefinitionCode())
                .processInstanceBusinessKey(request.getBusinessKey());
        if (query.count() == 0) {
            val instance = getProcessEngine().getRuntimeService().startProcessInstanceByKey(
                    request.getDefinitionCode(),
                    request.getBusinessKey(),
                    request.getVariables());
            request.setInstanceId(Long.parseLong(instance.getId()));
        } else if (query.active().count() == 1) {
            // 3.2 返回推送业务系统消息
            val instance = query.active().list().get(0);
            request.setInstanceId(Long.parseLong(instance.getId()));
        } else if (query.active().count() > 1) {
            // 3.3 异常情况，不应存在一个businessKey中有两条活跃实例
            log.info("createOrGetInstance,active Process More Then One Instance :" + Serializer.serialize(request));
            throw InstanceError.activeProcessMoreThenOneInstance(request, request.getBusinessKey());
        } else {
            // 3.4 异常情况，流程实例未激活
            log.info("createOrGetInstance,process Instance Not Activated:" + Serializer.serialize(request));
            throw InstanceError.processInstanceNotActivated(request, request.getBusinessKey());
        }
    }

    private List<ProcessTodoTask> getTodoTask(ProcessDataRequest request) {
        val result = new ArrayList<ProcessTodoTask>();
        val tasks = getTaskQuery(request).list();
        for (val task : tasks) {
            //转换成返回的todoTask对象
            result.add(convertToProcessTodoTask(task));
        }
        return result;
    }

    private Task getTodoTask(String instanceId, long taskId) {
        return getProcessEngine().getTaskService().createTaskQuery()
                .processInstanceId(instanceId)
                .taskId(Long.toString(taskId))
                .active().singleResult();
    }

    //完成一个任务，并记录下评论
    //如果该任务被完成(doneTask不为空)，则跳过该步骤
    private void completeTask(ProcessSubmitRequest request) {
        TaskInfo task = getTodoTask(Long.toString(request.getInstanceId()), request.getTaskId());
        if (task == null) {
            val doneTask = getTaskHistoriesQuery(
                    Long.toString(request.getInstanceId()),
                    request.getOperator().toString(),
                    request.getCurrentActivity(),
                    Long.toString(request.getTaskId())).finished().singleResult();
            if (doneTask == null) {
                throw InstanceError.taskNotFound(request, request.getCurrentActivity(), request.getTaskId());
            }
            return;
        }

        if (!request.getCurrentActivity().equals(task.getTaskDefinitionKey())) {
            throw InstanceError.taskActivityError(request, request.getCurrentActivity(), request.getTaskId());
        }
        if (!request.getOperator().getId().equals(Operator.valueOf(task.getAssignee()).getId())) {
            throw InstanceError.taskOperatorError(request, request.getCurrentActivity(), request.getTaskId());
        }
        if (task.getDueDate() != null && task.getDueDate().getTime() < System.currentTimeMillis()) {
            throw InstanceError.taskHasExpired(request, request.getCurrentActivity(), request.getTaskId());
        }
        addComments(request, task); //添加额外的评论数据
        log.info("complete task:" + task.getId());
        getProcessEngine().getTaskService().complete(task.getId(), request.getVariables());
    }

    //多实例任务的前置处理(多签处理)
    //如果该流程定义包含多实例节点(以DefinitionId为缓存Key)，则对每一个多实例节点做前置处理，见注释1和2
    private void multiInstanceTaskReady(ProcessDataRequest request) {
        if (request.getConditions() == null) {
            request.setConditions(new HashMap<>());
        }
        if (request.getTaskOwners() == null) {
            request.setTaskOwners(new HashMap<>());
            return;
        }
        val nodes = getMixInstanceNodes(request.getDefinitionId());
        if(CollectionUtils.isEmpty(nodes)){
            throw InstanceError.processNodeNotFound(request);
        } else {
            nodes.forEach((k, v) -> {
                val owners = request.getTaskOwners().get(k);
                if (!CollectionUtils.isEmpty(owners)) {
                    if (v instanceof MultiInstanceNode) {
                        val value = owners.stream().map(Operator::toString).collect(Collectors.toList());
                        String variable = ((MultiInstanceNode)v).getListVariable();
                        request.getConditions().put(variable.substring(2, variable.lastIndexOf("}")), value);
                    } else if (v instanceof SingleInstanceNode) {
                        if (owners.size() != 1) {
                            throw InstanceError.activityOwnersOnlyOne(request);
                        }
                        String assignee = ((SingleInstanceNode)v).getAssignee();
                        request.getConditions().put(assignee.substring(2, assignee.lastIndexOf("}")), owners.get(0));
                    }
                }
            });
        }
    }

    private ProcessInstanceResult doSubmit(ProcessSubmitRequest request){
        completeTask(request); //完成已分配的任务
        List<ProcessDoneTask> doneTasks = getDoneTask(Long.toString(request.getInstanceId()), null, null, Long.toString(request.getTaskId()));
        List<ProcessTodoTask> todoTasks = getTodoTask(request);

        return toResult(request, doneTasks, todoTasks);
    }

    private ProcessInstanceResult toResult(ProcessInstanceRequest request, List<ProcessDoneTask> doneTasks, List<ProcessTodoTask> todoTasks) {
        if (CollectionUtils.isEmpty(todoTasks)) { //没有待办
            return terminatedResult(request, todoTasks, doneTasks);
        } else {
            return request.toResult(todoTasks, doneTasks, ProcessInstance.Status.Running.name());
        }
    }

    private boolean stopped(ProcessInstanceRequest request) {
        val instance = getInstance(request);
        return instance == null || (instance.getEndTime() != null);
    }

    private void checkTargetActivity(ProcessRollbackRequest request) {
        val doneTasks = getTaskHistoriesQuery(Long.toString(request.getInstanceId()),null, request.getTargetActivity(),null).finished().list();
        if (CollectionUtils.isEmpty(doneTasks)) {
            log.info("checkTargetActivity:" + Serializer.serialize(request));
            throw InstanceError.activityAreNotHistoryNode(request, request.getTargetActivity());
        }
    }

    //任务的认领，将任务分配给某个处理人(Assignee)
    private void claim(Task task, Operator operator) {
        if (operator == null) {
            return;
        }
        if (operator instanceof Approver) { //包含委托人的情况下，记录原处理人(Bailor)到Owner
            if (((Approver) operator).getBailor() != null){
                getProcessEngine().getTaskService().setOwner(task.getId(), ((Approver) operator).getBailor().toString());
            }
        }
        if (StringUtils.isEmpty(task.getAssignee())) { //没有处理人，直接认领
            getProcessEngine().getTaskService().claim(task.getId(), operator.toString());
        } else { //有处理人
            if (operator.getId().equals(Operator.valueOf(task.getAssignee()).getId())) {//与当前处理人一致
                throw InstanceError.tansferorSameAsAssignee(task);
            } else {//与当前处理人不一致，先取消认领然后再分配处理人
                getProcessEngine().getTaskService().unclaim(task.getId());
                getProcessEngine().getTaskService().claim(task.getId(), operator.toString());
            }
        }
        task.setAssignee(operator.toString());
    }

    private List<Task> getTodoTasks(ProcessInstanceRequest request, String owner, String activity, long taskId) {
        TaskQuery query = getTaskQuery(request, owner);
        if (StringUtils.isEmpty(activity)) {
            query = query.taskDefinitionKey(activity);
        }
        if (taskId > 0) {
            query = query.taskId(Long.toString(taskId));
        }
        return query.list();
    }

    /**
     * 合并任务所属人，从permission系统中根据permissionData抓取符合条件的owner合并到taskOwners中
     */
    private void mergeTaskOwners(ProcessDataRequest request) {
//        if (request.getTaskOwners() == null) {
//            request.setTaskOwners(new HashMap<>());
//        }
//        val taskOwners = processOwnerService.getOwners(request);
//        if (taskOwners == null) {
//            return;
//        }
//        for (val entry : taskOwners.entrySet()) {
//            val key = Strings.trim(entry.getKey());
//            if (!request.getTaskOwners().containsKey(key)) {
//                request.getTaskOwners().put(key, new ArrayList<>());
//            }
//            for (val owner : entry.getValue()) {
//                if (!request.getTaskOwners().get(key).contains(owner)) {
//                    request.getTaskOwners().get(key).add(owner);
//                }
//            }
//        }
    }

    /**
     * 任务认领优先级
     * 1. 如果该节点为会签节点，则取activiti设置到流程变量中的owner来认领任务
     * 2. 如果该节点不是会签节点，则取taskOwners中对应该节点的第一个owner
     * 3. 如果传入的taskOwners不包含该task的owner，则取数据库中存储的对应的owner(节点owner不存在时抛出异常)
     **/
    private List<ProcessTodoTask> claimTasks(ProcessDataRequest request) {
//        val result = new ArrayList<ProcessTodoTask>();
//        val tasks = getTaskQuery(request, null).list();
//        for (val task : tasks) {
//            val node = multiInstanceNodes.get(request.getDefinitionId()).get(task.getTaskDefinitionKey());
//            if (node != null) { //若任务节点为多实例且流程变量保存了会签人，则以该会签人来认领此任务
//                val owner = (String) getProcessEngine().getRuntimeService().getVariable(task.getExecutionId(), node.getElementVariable());
//                claim(task, Operator.valueOf(owner),request);
//            } else { //否则以taskOwners集合为准
//                List<Operator> operators = null;
//                if(!CollectionUtils.isEmpty(request.getTaskOwners())){
//                    operators = request.getTaskOwners().get(task.getTaskDefinitionKey());
//                }
//                if (Sets.isEmpty(operators)) {
//                    val taskOwners = processTaskOwnerMapper.getTaskOwners(request, request.getInstanceId(), task.getTaskDefinitionKey());
//                    if (!Sets.isEmpty(taskOwners)) {
//                        claim(task, new Operator(taskOwners.get(0).getOwnerId(), taskOwners.get(0).getOwnerName()),request); //选取一个owner认领任务
//                    } else {
//                        Log.info(this.getClass(), "owner not found:" + task.getTaskDefinitionKey());
//                    }
//                } else {
//                    claim(task, operators.get(0),request); //选取一个owner认领任务
//                }
//            }
//            //转换成返回的todoTask对象
//            result.add(convertToProcessTodoTask(task));
//
//        }
//        return result;
        return null;
    }
    //endregion =====================================================
}
