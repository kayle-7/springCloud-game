package com.springboot.workflow.entity.results;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessResult {

    private String applicationId;
    private String definitionCode;
    private int definitionVersion;
    private String currentActivity;
    private Set<String> nextActivities;

}