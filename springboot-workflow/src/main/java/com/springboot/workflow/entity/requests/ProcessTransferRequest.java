package com.springboot.workflow.entity.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.workflow.constants.ActivitiConstants;
import com.springboot.workflow.entity.Operator;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class ProcessTransferRequest extends ProcessInstanceRequest {

    @Min(value=1L, message="process instance id < {min}.")
    private long instanceId;

    @Min(value=1L, message="process task id < {min}.")
    private long taskId;

    @Size(min=1, max=60, message="process current activity size must be {min} to {max}.")
    @Pattern(regexp="^[\\w-]*$", message="process current activity must be letter(A-Z/a-z), digital(0-9), underline(_) and joiner(-).")
    private String currentActivity;

    @NotNull(message="process handler is null.")
    private Operator operator;

    @NotNull(message="process transferor is null.")
    private Operator transferor;
    private Operator opsOperator;

    //转交意见
    private String opinion;

    //流转条件
    private Map<String, Object> conditions;

    public Map<String, Object> getConditions() {
        if (conditions == null) {
            conditions = new HashMap<>();
        }
        return conditions;
    }

    @JsonIgnore
    public Map<String, Object> getVariables() {
        if (getConditions() == null) {
            return new HashMap<>();
        }

        return getConditions().entrySet().stream()
                .filter(t -> t.getValue() != null && !t.getKey().startsWith(ActivitiConstants.prefixName))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void setOpinion(String opinion) {
        if (StringUtils.isNotEmpty(opinion)) {
            this.opinion = opinion;
            if (conditions == null) {
                conditions = new HashMap<>();
            }
            conditions.put(ActivitiConstants.opinionName, opinion);
        }
    }

}