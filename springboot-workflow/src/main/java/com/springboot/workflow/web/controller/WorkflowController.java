package com.springboot.workflow.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.springboot.workflow.config.WorkflowIdGenerator;
import com.springboot.workflow.constants.AutoComplete;
import com.springboot.workflow.entity.ApiResult;
import com.springboot.workflow.entity.ApprovalData;
import com.springboot.workflow.entity.Operator;
import com.springboot.workflow.entity.requests.*;
import com.springboot.workflow.entity.results.ProcessInstanceResult;
import com.springboot.workflow.entity.results.ProcessTaskResult;
import com.springboot.workflow.exception.WorkflowException;
import com.springboot.workflow.server.WorkflowExecuteService;
import com.springboot.workflow.server.WorkflowManagementService;
import com.springboot.workflow.server.WorkflowQueryService;
import com.springboot.workflow.util.FileUtilsExt;
import com.springboot.workflow.util.UUIDHelper;
import com.springboot.workflow.util.guid.IdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Api(description = "zx")
@Slf4j
@RestController
public class WorkflowController {

    @Resource
    private ProcessEngine processEngine;

    private ProcessEngine getProcessEngine() {
        return this.processEngine;
    }

    @Resource
    private WorkflowExecuteService workflowExecuteService;
    @Resource
    private WorkflowExecuteService workflowService;
    @Resource
    private WorkflowQueryService workflowQueryService;
    @Resource
    private WorkflowManagementService workflowManagementService;

    @ApiOperation(value = "部署流程")
    @RequestMapping(value = "/deploy", method = RequestMethod.POST)
    public ResponseEntity<ApiResult> deploy(@RequestBody JsonNode json) {

        ProcessDeployRequest request = new ProcessDeployRequest();
        request.setName(json.get("name").asText());
        request.setContent(FileUtilsExt.readFileToString(this, "processes/" + json.get("content").asText()));//"expenseAccount.bpmn20.xml"
        request.setKey(json.get("key").asText());
        request.setOperator(json.get("operator").asText());
        boolean bool = workflowManagementService.deploy(request);
        return ApiResult.successInfo(this.getClass(), bool, "deploy success");
    }

    @ApiOperation(value = "启动流程")
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public ResponseEntity<ApiResult> start(@RequestBody JsonNode json) {
        ProcessStartRequest start = new ProcessStartRequest();
        start.setDefinitionCode(json.get("definitionCode").asText());
        start.setBusinessKey(StringUtils.isBlank(json.get("businessKey").asText()) ? "expenseAccount-" + UUIDHelper.getRandomStr(3) : json.get("businessKey").asText());
        start.setOperator(new Operator(json.get("operator").get("id").asText(), json.get("operator").get("name").asText()));
        start.setTaskOwners(getNextOwners(json));

        ProcessInstanceResult result = workflowService.start(start);
        return ApiResult.successInfo(this.getClass(), result, "start success");
    }

    @ApiOperation(value = "提交任务")
    @RequestMapping(value = "/submitTask", method = RequestMethod.POST)
    public ResponseEntity<ApiResult> submitTask(@RequestBody JsonNode json) {
        ProcessSubmitRequest request = new ProcessSubmitRequest();

        request.setInstanceId(json.get("instanceId").asLong());
        request.setTaskId(json.get("taskId").asLong());
        request.setOperator(new Operator(json.get("operator").get("id").asText(), json.get("operator").get("name").asText()));
        request.setCurrentActivity(json.get("currentActivity").asText());
        request.setApprovalData(getApprovalData(json));

        request.setTaskOwners(getNextOwners(json));
        ProcessInstanceResult result = workflowService.submit(request);
        return ApiResult.successInfo(this.getClass(), result, "submitTask success");
    }

    @ApiOperation(value = "驳回")
    @RequestMapping(value = "/rollback", method = RequestMethod.POST)
    public ResponseEntity<ApiResult> rollback(@RequestBody JsonNode json) {
        ProcessRollbackRequest request = new ProcessRollbackRequest();
        request.setInstanceId(json.get("instanceId").asLong());
        request.setTaskId(json.get("taskId").asLong());
        request.setOperator(new Operator(json.get("operator").get("id").asText(), json.get("operator").get("name").asText()));
        request.setTargetActivity(json.get("targetActivity").asText());
        request.setApprovalData(getApprovalData(json));

        ProcessInstanceResult result = workflowService.rollback(request);//驳回任务状态为complete
        return ApiResult.successInfo(this.getClass(), result, "rollback success");
    }

    @ApiOperation(value = "撤回")
    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public ResponseEntity<ApiResult> withdraw(@RequestBody JsonNode json) {
        ProcessRollbackRequest request = new ProcessRollbackRequest();
        request.setInstanceId(json.get("instanceId").asLong());
        request.setOperator(new Operator(json.get("operator").get("id").asText(), json.get("operator").get("name").asText()));
        request.setTargetActivity(json.get("targetActivity").asText());
        request.setApprovalData(getApprovalData(json));

        ProcessInstanceResult result = workflowService.withdraw(request);//驳回任务状态为complete
        return ApiResult.successInfo(this.getClass(), result, "withdraw success");
    }

    @ApiOperation(value = "跳转")
    @RequestMapping(value = "/runTo", method = RequestMethod.POST)
    public void runTo(@RequestBody JsonNode json) {
        ProcessRunToRequest request = new ProcessRunToRequest();
        request.setInstanceId(json.get("instanceId").asLong());
        String remark = "test stop 175001";
        workflowService.runTo(request);
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public ResponseEntity<ApiResult> transfer(@RequestBody JsonNode json) {
        ProcessTransferRequest request = new ProcessTransferRequest();
        request.setInstanceId(json.get("instanceId").asLong());
        request.setTaskId(json.get("taskId").asLong());
        request.setOperator(new Operator(json.get("operator").get("id").asText(), json.get("operator").get("name").asText()));
        request.setCurrentActivity(json.get("currentActivity").asText());
        request.setTransferor(new Operator(json.get("transferor").get("id").asText(), json.get("transferor").get("name").asText()));
        request.setOpinion(json.get("opinion").asText());

        ProcessInstanceResult result = workflowService.transfer(request);
        return ApiResult.successInfo(this.getClass(), result, "transfer success");
    }

    @RequestMapping(value = "/getHistory", method = RequestMethod.POST)
    public ResponseEntity<ApiResult> getHistory(@RequestBody JsonNode json) {
        ProcessQueryRequest request = new ProcessQueryRequest();
        request.setInstanceId(json.get("instanceId").asLong());

        List<ProcessTaskResult> results = workflowQueryService.history(request);
        return ApiResult.successInfo(this.getClass(), results, "getHistory success");
    }

    @RequestMapping(value = "/getTodo", method = RequestMethod.POST)
    public ResponseEntity<ApiResult> getTodo(@RequestBody JsonNode json) {
        ProcessQueryRequest request = new ProcessQueryRequest();
        request.setInstanceId(json.get("instanceId").asLong());
        List<ProcessTaskResult> results = workflowQueryService.todo(request);
        return ApiResult.successInfo(this.getClass(), results, "getTodo success");
    }

    @RequestMapping(value = "/stopper", method = RequestMethod.POST)
    public void stopper(@RequestBody JsonNode json) {
        ProcessInstanceRequest request = new ProcessInstanceRequest();
        request.setInstanceId(json.get("instanceId").asLong());
        workflowService.stop(request, json.get("remark").asText());
    }

    @RequestMapping(value = "getStudent", method = RequestMethod.POST)
    public List<ProcessTransferRequest> getStudent(@RequestBody ProcessTransferRequest request) {
        workflowExecuteService.transfer(request);

        return new ArrayList<>();
    }

    @RequestMapping(value = "getStudentList", method = RequestMethod.POST)
    public List<ProcessTransferRequest> getStudentList(@Validated ProcessTransferRequest studentEntity) {
        return new ArrayList<>();
    }

    private Map<String, List<Operator>> getNextOwners(JsonNode request) {
        Map<String, List<Operator>> nextOwners = null;
        if (request.get("taskOwners") != null) {
            nextOwners = new HashMap<>();
            Iterator<String> i = request.get("taskOwners").fieldNames();
            while (i.hasNext()) {
                String key = i.next();
                List<Operator> list = new ArrayList<>();
                for (JsonNode node : request.get("taskOwners").get(key)) {
                    list.add(new Operator(node.get("id").asText(), node.get("name").asText()));
                }
                nextOwners.put(key, list);
            }
        }
        return nextOwners;
    }

    private ApprovalData getApprovalData(JsonNode request) {
        if (request == null) {
            return null;
        }
        ApprovalData data = new ApprovalData();
        if (StringUtils.isNotBlank(request.get("action").asText())) {
            data.setAction(request.get("action").asText());
        }
        if (StringUtils.isNotBlank(request.get("opinion").asText())) {
            data.setOpinion(request.get("opinion").asText());
        }
        if (StringUtils.isNotBlank(request.get("remark").asText())) {
            data.setRemark(request.get("remark").asText());
        }
        if (request.get("autoSkip") != null) {
            if (request.get("autoSkip").asBoolean()) {
                data.setComplete(AutoComplete.Skip);
            } else {
                data.setComplete(AutoComplete.Continue);
            }
        }
        return data;
    }

    public static void main(String[] args) {
        long maxWorkerId = ~(-1L << 16);
        int maxRandomBit = (int)~(-1L << 2);
        long sequenceMask = ~(-1L << 5);

        System.out.println(~(-1L << 4));
        System.out.println(~(-1L << 16));
        System.out.println(~(-1 << 5));
        System.out.println(~(19));

        if (1 == 1) {
            return;
        }

        System.out.println(new Date("2019/01/01").getTime());
        WorkflowIdGenerator workflowIdGenerator = new WorkflowIdGenerator();
        for (int j = 0; j < 100; j++) {
            System.out.println("Thread.currentThread().getId() : " + Thread.currentThread().getId());
            System.out.println(workflowIdGenerator.getNextId());
        }

//        String ss = "${123123}";
//        int i = ss.lastIndexOf("}");
//        String sss = ss.substring(2, i);
//        System.out.println(sss);

//        IdWorker idWorker = new IdWorker(31,31);
//        System.out.println("idWorker="+idWorker.nextId());
        IdWorker id = new IdWorker(0, 1);
//        System.out.println("id="+id.nextId());
//        System.out.println(id.datacenterId);
//        System.out.println(id.workerId);

        for (int i = 0; i < 9000; i++) {
            System.err.println(id.nextId());
        }
//        String s = "/^[\\$\\{]{1}\\w+\\}$/";
//        String s1 = "^\\$\\{\\w+}$";
//        java.util.regex.Pattern p = java.util.regex.Pattern.compile(s1);
//        Matcher m = p.matcher("${123adadasf}");
//        System.out.println(m.matches());
    }

    public List<Deployment> getDeploymentLikeNameAndKey(String name, String key, boolean last) {
        DeploymentQuery deploymentQuery = getProcessEngine().getRepositoryService().createDeploymentQuery();
        if (StringUtils.isNoneBlank(name)) {
            deploymentQuery = deploymentQuery.deploymentNameLike(name);
        }
        if (StringUtils.isNotBlank(key)) {
            deploymentQuery = deploymentQuery.deploymentKeyLike(key);
        }
        List<Deployment> deploymentList;
        if (last) {
            deploymentList = deploymentQuery.orderByDeploymenTime().latest().list();
        } else {
            deploymentList = deploymentQuery.orderByDeploymenTime().asc().list();
        }
        log.info("get deployment success.");
        return deploymentList;
    }

    public List<ProcessDefinition> getProcessDefinitionLikeNameAndKey(String name, String key, boolean lastVersion) {
        ProcessDefinitionQuery processDefinitionQuery = getProcessEngine().getRepositoryService().createProcessDefinitionQuery();
        if (StringUtils.isNoneBlank(name)) {
            processDefinitionQuery = processDefinitionQuery.processDefinitionNameLike(name);
        }
        if (StringUtils.isNotBlank(key)) {
            processDefinitionQuery = processDefinitionQuery.processDefinitionKeyLike(key);
        }
        List<ProcessDefinition> processDefinitionList;
        if (lastVersion) {
            processDefinitionList = processDefinitionQuery.latestVersion().list();
        } else {
            processDefinitionList = processDefinitionQuery.orderByProcessDefinitionVersion().asc().list();
        }
        log.info("get process definition success.");
        return processDefinitionList;
    }

    public boolean deleteProcessDeployment(List<String> deploymentIdList, boolean cascade, String key) {
        if (CollectionUtils.isEmpty(deploymentIdList)) {
            if (StringUtils.isBlank(key)) {
                log.warn("deleteProcessDeployment() key is blank.");
                return false;
            }
            List<ProcessDefinition> processDefinitionList = getProcessEngine().getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(key).orderByDeploymentId().list();
            if (CollectionUtils.isEmpty(processDefinitionList)) {
                log.warn("deleteProcessDeployment() processDefinitionList is empty.");
                return false;
            }
            deploymentIdList = processDefinitionList.stream().map(ProcessDefinition::getDeploymentId).collect(Collectors.toList());
        }

        if (CollectionUtils.isEmpty(deploymentIdList)) {
            log.warn("deleteProcessDeployment() deploymentIdList is empty.");
            return false;
        }
        deploymentIdList.forEach(x -> {
            //如果流程正在执行则会抛异常;级联删除，流程正在执行也会被删除
            getProcessEngine().getRepositoryService().deleteDeployment(x, cascade);
        });

        log.info("delete process deployment success.");
        return true;
    }

    public boolean setTaskVariable(String taskId, String variableName, Object value) {
        if (StringUtils.isBlank(taskId)) {
            log.warn("setTaskVariable() taskId is blank.");
            return false;
        }
        if (StringUtils.isBlank(variableName)) {
            log.warn("setTaskVariable() variableName is blank.");
            return false;
        }
        if (value == null) {
            log.warn("setTaskVariable() value is null.");
            return false;
        }
        getProcessEngine().getTaskService().setVariable(taskId, variableName, value);
        log.info("set variable success.");
        return true;

    }

    public boolean setTaskVariables(String taskId, Map<String, Object> variables) {
        if (StringUtils.isBlank(taskId)) {
            log.warn("setTaskVariables() taskId is blank.");
            return false;
        }
        if (variables == null || variables.size() == 0) {
            log.warn("setTaskVariables() variables is empty.");
            return false;
        }
        checkVariables(variables);

        getProcessEngine().getTaskService().setVariables(taskId, variables);
        log.info("set variables success.");
        return true;

    }


    public boolean setTaskAssignee(String taskId, String userId, boolean delegate) {
        if (StringUtils.isBlank(taskId)) {
            log.warn("transferTask() taskId is blank.");
            return false;
        }
        if (StringUtils.isBlank(userId)) {
            log.warn("setTaskAssignee() userId is blank.");
            return false;
        }
        if (delegate) {
            //委托
            getProcessEngine().getTaskService().delegateTask(taskId, userId);
        } else {
            getProcessEngine().getTaskService().setAssignee(taskId, userId);
        }
        log.info("set task assignee success.");
        return true;
    }

    public List<String> getActiveActivity(String executionId) {
        if (StringUtils.isBlank(executionId)) {
            log.warn("getActiveActivity() executionId is blank.");
            return new ArrayList<>();
        }
        return getProcessEngine().getRuntimeService().getActiveActivityIds(executionId);
    }

    private void checkVariables (Map<String, Object> variables) {
        variables.forEach((k, v) ->
                {
                    if (StringUtils.isBlank(k) || v == null) {
                        log.warn("setTaskVariables() variables key or value error!");
                        throw new WorkflowException("variables key or value error!");
                    }
                }
        );
    }
}
