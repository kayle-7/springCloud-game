package com.springboot.workflow.server.impl;

import com.springboot.workflow.entity.requests.ProcessDeployRequest;
import com.springboot.workflow.entity.requests.ProcessRequest;
import com.springboot.workflow.exception.DefinitionError;
import com.springboot.workflow.exception.WorkflowException;
import com.springboot.workflow.server.AbstractProcessCommonService;
import com.springboot.workflow.server.WorkflowManagementService;
import com.springboot.workflow.util.ValidationUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.activiti.engine.impl.bpmn.deployer.ResourceNameUtil;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by zx
 */
@Slf4j
@Component
public class WorkflowManagementServiceImpl extends AbstractProcessCommonService implements WorkflowManagementService {

    @Override
    public final boolean deploy(@NonNull final ProcessDeployRequest request) throws WorkflowException {
        validateDeploy(request);

        DeploymentBuilder deployBuilder = getProcessEngine().getRepositoryService().createDeployment()
                .addString(getResourceName(request.getName()), request.getContent());
        if (!StringUtils.isEmpty(request.getKey())) {
            deployBuilder.key(request.getKey());
        }
        if (!StringUtils.isEmpty(request.getOperator())) {
            deployBuilder.name(request.getOperator());
        }
        val deploy = deployBuilder.deploy();
        ActivitiHelper.removeCacheKey(deploy.getKey());

        return true;
    }

    public boolean delete(@NonNull final ProcessRequest request, boolean cascade) {
        validateDelete(request);

        List<ProcessDefinition> processDefinitionList = getProcessEngine().getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionKey(request.getDefinitionCode()).orderByDeploymentId().list();
        if (CollectionUtils.isEmpty(processDefinitionList)) {
            throw DefinitionError.processDefinitionCodeNotFound(request);
        }
        List<String> deploymentIdList = processDefinitionList.stream().map(ProcessDefinition::getDeploymentId).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(deploymentIdList)) {
            throw DefinitionError.processDefinitionCodeNotFound(request);
        }

        //如果流程正在执行则会抛异常;级联删除，流程正在执行也会被删除
        deploymentIdList.forEach(x -> getProcessEngine().getRepositoryService().deleteDeployment(x, cascade));

        log.info("delete process deployment success.");
        return true;
    }

    private static void validateDeploy(ProcessDeployRequest request) throws WorkflowException {
        ValidationUtil.checkStringMin(request.getContent(), 1,"process definition content must be >= 1.");
    }

    private static void validateDelete(ProcessRequest request) throws WorkflowException {
        ValidationUtil.checkStringMax(request.getDefinitionCode(), 40, "process definition code size must be <= 40.");
    }

    private String getResourceName(String name) {
        if (StringUtils.isEmpty(name)) {
            return UUID.randomUUID().toString() + "." + ResourceNameUtil.BPMN_RESOURCE_SUFFIXES[0];
        }
        for (String suffix : ResourceNameUtil.BPMN_RESOURCE_SUFFIXES) {
            if (name.endsWith(suffix)) {
                return name;
            }
        }
        return name + "." + ResourceNameUtil.BPMN_RESOURCE_SUFFIXES[0];
    }

}
