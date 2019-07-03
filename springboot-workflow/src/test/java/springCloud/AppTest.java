//package springCloud;
//
//import com.springboot.workflow.WorkflowApp;
//import com.springboot.workflow.entity.ApprovalData;
//import com.springboot.workflow.entity.Operator;
//import com.springboot.workflow.entity.requests.*;
//import com.springboot.workflow.entity.results.ProcessInstanceResult;
//import com.springboot.workflow.entity.results.ProcessTaskResult;
//import com.springboot.workflow.server.WorkflowExecuteService;
//import com.springboot.workflow.server.WorkflowManagementService;
//import com.springboot.workflow.server.WorkflowQueryService;
//import com.springboot.workflow.util.Serializer;
//import com.springboot.workflow.util.UUIDHelper;
//import lombok.extern.slf4j.Slf4j;
//import lombok.val;
//import org.activiti.engine.*;
//import org.activiti.engine.impl.interceptor.Command;
//import org.activiti.engine.impl.interceptor.CommandContext;
//import org.activiti.engine.repository.Deployment;
//import org.activiti.engine.task.Comment;
//import org.activiti.engine.task.Task;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.annotation.Resource;
//import java.util.*;
//
///**
// * Unit test for simple App.
// */
//@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = WorkflowApp.class)
//public class AppTest implements Command<Comment> {
//
//    @Override
//    public Comment execute(CommandContext commandContext) {
//        val root = commandContext.getExecutionEntityManager().findByRootProcessInstanceId("10001");
//
////        if (root != null) { //清空当前执行流，并重建执行流
////            commandContext.getExecutionEntityManager().deleteChildExecutions(root, null, false);
////            val current = commandContext.getExecutionEntityManager().createChildExecution(root);
////            current.setCurrentFlowElement(getTargetFlowElement());
////            commandContext.getAgenda().planContinueProcessInCompensation(current);
////        }
//        return null;
//    }
//
//    @Autowired
//    private WorkflowExecuteService workflowService;
//    @Autowired
//    private RuntimeService runtimeService;
//    @Autowired
//    private TaskService taskService;
//    @Autowired
//    private HistoryService historyService;
//    @Autowired
//    private RepositoryService repositoryService;
//    @Autowired
//    private ManagementService managementService;
//    @Autowired
//    private WorkflowQueryService workflowQueryService;
//    @Resource
//    private WorkflowManagementService workflowManagementService;
//
//    @Test
//    public void deploy() {
//
//        Deployment deployment = repositoryService.createDeployment()
//                .name("报销但审批")
//                .addClasspathResource("processes/expenseAccount.bpmn20.xml")
//                .deploy();
//
//        System.out.println(deployment.getId());
//        System.out.println(deployment.getDeploymentTime());
//    }
//
//    @Test
//    public void start() {
//        Map<String, List<Operator>> taskOwners = new HashMap<>();
//        taskOwners.put("draft", Arrays.asList(new Operator("101", "唐僧")));
//        taskOwners.put("bizCheck", Arrays.asList(new Operator("102", "孙悟空"), new Operator("103", "猪八戒"), new Operator("104", "沙悟净")));
//        taskOwners.put("finCheck", Arrays.asList(new Operator("105", "观音"), new Operator("106", "如来")));
//        ProcessStartRequest start = new ProcessStartRequest();
//        start.setDefinitionCode("expenseAccount");
//        start.setBusinessKey("expenseAccount-" + UUIDHelper.getRandomStr(3));
//        start.setTaskOwners(taskOwners);
//        start.setOperator(new Operator("101", "唐僧"));
//        start.setDefinitionVersion(1);
//        ProcessInstanceResult result = workflowService.start(start);
//        log.info("result : " + Serializer.serialize(result));
//    }
//
//    @Test
//    public void rollback1() {
//        ProcessRollbackRequest request = new ProcessRollbackRequest();
//        request.setInstanceId(272501);
//        request.setTargetActivity("draft");
//        request.setTaskId(272515);
//        request.setApprovalData(new ApprovalData(null, "bizCheck费用太多不同意", null, null));
//        request.setOperator(new Operator("102", "孙悟空"));
//        ProcessInstanceResult result = workflowService.rollback(request);//驳回任务状态为complete
//        log.info("result : " + Serializer.serialize(result));
//
//    }
//
//    @Test
//    public void submitDraft() {
//        Map<String, List<Operator>> parameterMap = new HashMap<>();
//        parameterMap.put("bizCheck", Arrays.asList(new Operator("102", "孙悟空"), new Operator("103", "猪八戒"), new Operator("104", "沙悟净")));
//        ProcessSubmitRequest request = new ProcessSubmitRequest();
//        request.setInstanceId(280001);
//        request.setTaskOwners(parameterMap);
//        request.setTaskId(295009);
//        request.setOperator(new Operator("101", "唐僧"));
//        request.setCurrentActivity("draft");
//        request.setApprovalData(new ApprovalData("提交", "提交费用报销申请", "submit", null));
//        ProcessInstanceResult result = workflowService.submit(request);
//        log.info("result : " + Serializer.serialize(result));
//    }
//
//    @Test
//    public void submitTask() {
//        ProcessSubmitRequest request = new ProcessSubmitRequest();
//        request.setInstanceId(280001);
//        request.setTaskId(297515);
//        request.setOperator(new Operator("102", "孙悟空"));
//        request.setCurrentActivity("bizCheck");
//        request.setApprovalData(new ApprovalData("同意", "同意报销", "submit", null));
//        ProcessInstanceResult result = workflowService.submit(request);
//        log.info("result : " + Serializer.serialize(result));
//    }
//
//    @Test
//    public void submitTask9() {
//        ProcessSubmitRequest request = new ProcessSubmitRequest();
//        request.setInstanceId(280001);
//        request.setTaskId(297519);
//        request.setOperator(new Operator("104", "沙悟净"));
//        request.setCurrentActivity("bizCheck");
//        request.setApprovalData(new ApprovalData("同意", "同意报销", "submit", null));
//        ProcessInstanceResult result = workflowService.submit(request);
//        log.info("result : " + Serializer.serialize(result));
//    }
//
//    @Test
//    public void submitTask1() {
//        ProcessSubmitRequest request = new ProcessSubmitRequest();
//        request.setInstanceId(282515);
//        request.setTaskId(182515);
//        request.setOperator(new Operator("108", "玉皇大帝"));
//        request.setCurrentActivity("bizCheck");
//        request.setApprovalData(new ApprovalData("同意", "玉皇大帝同意报销", "submit", null));
//        ProcessInstanceResult result = workflowService.submit(request);
//        log.info("result : " + Serializer.serialize(result));
//    }
//
//    @Test
//    public void submitTask2() {
//        ProcessSubmitRequest request = new ProcessSubmitRequest();
//        request.setInstanceId(280001);
//        request.setTaskId(305009);
//        request.setOperator(new Operator("105", "观音"));
//        request.setCurrentActivity("finCheck");
////        request.setApprovalData(new ApprovalData("同意", "finCheck同意报销", "submit", null));
//        ProcessInstanceResult result = workflowService.submit(request);
//        log.info("result : " + Serializer.serialize(result));
//    }
//
//    @Test
//    public void rollback() {
//        ProcessRollbackRequest request = new ProcessRollbackRequest();
//        request.setInstanceId(280001);
//        request.setTargetActivity("draft");
//        request.setTaskId(282518);
//        request.setApprovalData(new ApprovalData("驳回", "bizCheck费用太多不同意", "reject", null));
//        request.setOperator(new Operator("103", "猪八戒"));
//        ProcessInstanceResult result = workflowService.rollback(request);//驳回任务状态为complete
//        log.info("result : " + Serializer.serialize(result));
//
//    }
//
//    @Test
//    public void withdraw() {
//        ProcessRollbackRequest request = new ProcessRollbackRequest();
//        request.setInstanceId(280001);
//        request.setTargetActivity("draft");
//        request.setApprovalData(new ApprovalData("撤回", "draft 撤回申请单", "withdraw", null));
//        request.setOperator(new Operator("101", "唐僧"));
//        ProcessInstanceResult result = workflowService.withdraw(request);//驳回任务状态为complete
//        log.info("result : " + Serializer.serialize(result));
//
//    }
//
//    @Test
//    public void runTo() {
//        ProcessRunToRequest request = new ProcessRunToRequest();
//        request.setInstanceId(207501);
//        String remark = "test stop 175001";
//        workflowService.runTo(request);
//    }
//
//    @Test
//    public void transfer() {
//        ProcessTransferRequest request = new ProcessTransferRequest();
//        request.setInstanceId(280001);
//        request.setTaskId(290015);
//        request.setCurrentActivity("bizCheck");
//        request.setOperator(new Operator("102", "孙悟空"));
//        request.setTransferor(new Operator("108", "玉皇大帝"));
////        request.setOpinion("孙悟空 test transfer");
//        ProcessInstanceResult result = workflowService.transfer(request);
//        log.info("result : " + Serializer.serialize(result));
//    }
//
//    @Test
//    public void transfer1() {
//        ProcessTransferRequest request = new ProcessTransferRequest();
//        request.setInstanceId(192501);
//        request.setTaskId(195018);
//        request.setCurrentActivity("bizCheck");
//        request.setOperator(new Operator("103", "猪八戒"));
//        request.setTransferor(new Operator("108", "玉皇大帝"));
//        request.setOpinion("猪八戒 test transfer");
//        ProcessInstanceResult result = workflowService.transfer(request);
//        log.info("result : " + Serializer.serialize(result));
//    }
//
//    @Test
//    public void getHistory() {
//        ProcessQueryRequest request = new ProcessQueryRequest();
//        request.setInstanceId(280001);
//        List<ProcessTaskResult> results = workflowQueryService.history(request);
//        log.info("result : " + Serializer.serialize(results));
//    }
//
//    @Test
//    public void getTodo() {
//        ProcessQueryRequest request = new ProcessQueryRequest();
//        request.setInstanceId(280001);
//        List<ProcessTaskResult> results = workflowQueryService.todo(request);
//        log.info("result : " + Serializer.serialize(results));
//    }
//
//    @Test
//    public void stopper() {
//        ProcessInstanceRequest request = new ProcessInstanceRequest();
//        request.setInstanceId(207501);
//        String remark = "test stop 175001";
//        workflowService.stop(request, remark);
//    }
//
//    @Test
//    public void test() {
//
//        Task task = taskService.createTaskQuery()
//                .processInstanceId("50001")
//                .taskId("50007")
//                .active().singleResult();
//
//        taskService.createTaskQuery()
//                .processInstanceId("55001")
//                .taskId(Long.toString(55007))
//                .active().singleResult();
//
////        workflowService.test("12517");
////        Object obj = Context.getCommandContext();
////        Object obj1 = Context.getCommandContext().getTaskEntityManager();
////        List<TaskEntity> taskEntityList = Context.getCommandContext().getTaskEntityManager().findTasksByProcessInstanceId("10001");
//
////        if (CollectionUtils.isNotEmpty(taskEntityList)) {
////            taskEntityList.forEach(x -> {
////                System.out.println("taskId : " + x.getId());
////                System.out.println("assignee : " + x.getAssignee());
////                System.out.println("executionId : " + x.getExecutionId());
////                System.out.println("name : " + x.getName());
////                System.out.println("================");
////            });
////        }
//    }
//
//
////    @Test
////    public void getActivity() {
////        String executionId = "5007";
////        List<String> activityIds = workflowService.getActiveActivity(executionId);
////        System.out.println("activityIds : " + Serializer.serialize(activityIds));
////    }
////
////    @Test
////    public void getTask(){
////        List<Task> tasks = workflowService.getTask("2501", null, null);
////        if (CollectionUtils.isNotEmpty(tasks)) {
////            tasks.forEach(x -> {
////                System.out.println("taskId : " + x.getId());
////                System.out.println("assignee : " + x.getAssignee());
////                System.out.println("executionId : " + x.getExecutionId());
////                System.out.println("name : " + x.getName());
////                System.out.println("================");
////            });
////        }
////    }
////
////
////
////    @Test
////    public void getHistory() {
////        List<HistoricTaskInstance> historicTaskInstanceList = workflowService.getHistory("2501");
////        if (CollectionUtils.isNotEmpty(historicTaskInstanceList)) {
////            historicTaskInstanceList.forEach(x -> {
////                log.warn("id : " + x.getId());
////                log.warn("name : " + x.getName());
////                log.warn("create time : " + x.getCreateTime());
////                log.warn("start time : " + x.getStartTime());
////                log.warn("end time : " + x.getEndTime());
////                log.warn("assignee : " + x.getAssignee());
////                log.warn("owner : " + x.getOwner());
////                log.warn("executionId : " + x.getExecutionId());
////                log.warn("task local variables" + Serializer.serialize(x.getTaskLocalVariables()));
////                log.warn("======================================");
////            });
////        }
////    }
//}
