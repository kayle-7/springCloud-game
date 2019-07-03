package com.springboot.workflow.server.impl;

import com.springboot.workflow.entity.requests.ProcessDeployRequest;
import com.springboot.workflow.server.Server;
import com.springboot.workflow.server.WorkflowManagementService;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Data
@Component
@Slf4j
@EnableScheduling
public class AutoDeploymentServer implements Server {

    @Autowired
    private WorkflowManagementService processManagementService;

    @Resource
    @Setter
    private ProcessEngine processEngine;

    private static final String DEPLOYMENT_PATH = "processes/";
    private static boolean keep = true;

    @Override
//    @Scheduled(initialDelay = 1000, fixedDelay = 1000 * 60 * 60 * 24) //  todo    添加定时任务
    public void start() {
        if (!keep) {
            return;
        }
        keep = false;
        ProcessDeployRequest processDeployRequest = new ProcessDeployRequest();
        processManagementService.deploy(processDeployRequest);
        log.info("AutoDeploymentServer deploy end");
    }

    private boolean hasDeployment(ProcessDeployRequest processDeployRequest) {
        List<Deployment> list = processEngine.getRepositoryService().createDeploymentQuery()
                .deploymentKey(processDeployRequest.getName()).list();
        return !CollectionUtils.isEmpty(list);
    }

    @Override
    public void stop() {

    }
}
