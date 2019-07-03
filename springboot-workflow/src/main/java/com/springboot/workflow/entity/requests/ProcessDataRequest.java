package com.springboot.workflow.entity.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.workflow.constants.ActivitiConstants;
import com.springboot.workflow.entity.ApprovalData;
import com.springboot.workflow.entity.Operator;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class ProcessDataRequest extends ProcessInstanceRequest {
    private Map<String, Object> conditions; //流转条件
    private Map<String, List<Operator>> taskOwners; //任务的所属人
    private Map<String, String> data; //额外数据
    private ApprovalData approvalData;

    @JsonIgnore
    public Map<String, Object> getVariables() {
        if (getConditions() == null) {
            return new HashMap<>();
        }

        return getConditions().entrySet().stream()
                .filter(t -> t.getValue() != null && !t.getKey().startsWith(ActivitiConstants.prefixName))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void setApprovalData(ApprovalData data) {
        if (conditions == null) {
            conditions = new HashMap<>();
        }
        if (data != null && data.getAction() != null && data.getAction().trim().length() > 0) {
            conditions.put("_@action", data.getAction());
        }

        if (data != null && data.getRemark() != null && data.getRemark().trim().length() > 0) {
            conditions.put("_@remark", data.getRemark());
        }

        if (data != null && data.getOpinion() != null && data.getOpinion().trim().length() > 0) {
            conditions.put("_@opinion", data.getOpinion());
        }

        if (data != null && data.getComplete() != null) {
            conditions.put("_@complete", data.getComplete().name());
        }
    }
}
