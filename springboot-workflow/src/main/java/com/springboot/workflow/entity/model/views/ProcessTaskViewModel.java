package com.springboot.workflow.entity.model.views;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.workflow.entity.Operator;
import com.springboot.workflow.entity.model.supports.ProcessViewModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
public class ProcessTaskViewModel extends ProcessViewModel {

    private long instanceId;
    private String activity;
    private String name;
    private String status;
    private Operator owner;
    private Date claimTime;
    private Date dueTime;
    private int priority;
    private Map<String, Object> properties;

    @JsonIgnore
    public long getClaimTimestamp()
    {
        if (getClaimTime() == null) {
            return 0L;
        }
        return getClaimTime().getTime();
    }

    @JsonIgnore
    public long getDueTimestamp()
    {
        if (getDueTime() == null) {
            return 0L;
        }
        return getDueTime().getTime();
    }

}