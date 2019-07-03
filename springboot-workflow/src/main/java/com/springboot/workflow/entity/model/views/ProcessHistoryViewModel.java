package com.springboot.workflow.entity.model.views;

import com.springboot.workflow.entity.model.supports.ProcessViewModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessHistoryViewModel extends ProcessViewModel {

    private long instanceId;
    private String activity;
    private long taskId;

}