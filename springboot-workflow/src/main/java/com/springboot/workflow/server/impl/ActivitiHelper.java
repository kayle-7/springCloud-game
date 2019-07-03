package com.springboot.workflow.server.impl;

import com.springboot.workflow.entity.requests.ProcessRequest;
import com.springboot.workflow.exception.DefinitionError;
import lombok.val;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActivitiHelper {

    private static final Map<String, String> processDefinitionIds = new ConcurrentHashMap<>();

    //需要保证definitionKey是全局跨应用唯一，还需要验证业务逻辑
    public static String getProcessDefinitionId(ProcessEngine processEngine, ProcessRequest request) {
        String key = getCacheKey(request);
        if (!processDefinitionIds.containsKey(key)) {
            ProcessDefinitionQuery query = processEngine.getRepositoryService().createProcessDefinitionQuery()
                    .processDefinitionKey(request.getDefinitionCode());
            if (request.latestVersion()) {
                val processDefinition = query.latestVersion().singleResult();
                if (processDefinition != null) {
                    processDefinitionIds.put(request.getDefinitionCode(), processDefinition.getId()); //code
                    request.setDefinitionVersion(processDefinition.getVersion());
                    key = getCacheKey(request);
                    processDefinitionIds.put(key, processDefinition.getId()); //code-version
                }
            } else {
                val processDefinition = query.processDefinitionVersion(request.getDefinitionVersion())
                        .singleResult();
                if (processDefinition != null) {
                    processDefinitionIds.put(key, processDefinition.getId()); //code-version
                }
            }
        }
        if (!processDefinitionIds.containsKey(key)) {
            throw DefinitionError.processDefinitionCodeNotFound(request);
        }
        return processDefinitionIds.get(key);
    }

    static void removeCacheKey(String key) {
        if (!StringUtils.isEmpty(key)) {
            processDefinitionIds.remove(key);
        }
    }

    public static int getDefinitionVersion(ProcessRequest request) {
        val key = getCacheKey(request);
        val definitionId = processDefinitionIds.get(key);
        if (StringUtils.isEmpty(definitionId)) {
            return 0;
        } else {
            return Integer.parseInt(definitionId.split(":")[1]);
        }
    }

    private static String getCacheKey(ProcessRequest request) {
        if (request.latestVersion()) {
            return request.getDefinitionCode();
        } else {
            return String.format("%s-%d", request.getDefinitionCode(), request.getDefinitionVersion());
        }
    }
}
