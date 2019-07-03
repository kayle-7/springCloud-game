package com.springboot.workflow.entity.model.supports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.workflow.entity.Operator;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public abstract class ProcessViewModel extends Process {
    private Operator creator;
    private Date beginTime;
    private Date endTime;
    private int revision;

    @JsonIgnore
    public long duration() {
        if (getBeginTime() == null) {
            return 0L;
        }
        if (getEndTime() != null) {
            return getEndTime().getTime() - getBeginTime().getTime();
        }
        return System.currentTimeMillis() - getBeginTime().getTime();
    }

    @JsonIgnore
    public long getBeginTimestamp() {
        if (getBeginTime() == null) {
            return 0L;
        }
        return getBeginTime().getTime();
    }

    @JsonIgnore
    public long getEndTimestamp() {
        if (getEndTime() == null) {
            return 0L;
        }
        return getEndTime().getTime();
    }

}