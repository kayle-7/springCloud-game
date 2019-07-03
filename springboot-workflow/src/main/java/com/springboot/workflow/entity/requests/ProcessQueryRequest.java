package com.springboot.workflow.entity.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessQueryRequest extends ProcessRequest {
    private long instanceId;
}
