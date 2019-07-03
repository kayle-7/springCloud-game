package com.springboot.workflow.entity.model.views;

import com.springboot.workflow.entity.model.supports.ProcessViewModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ProcessInstanceViewModel extends ProcessViewModel {

    private String status;
    private String beginActivity;
    private String endActivity;
    private String businessKey;
    private Map<String, Object> condition;

}