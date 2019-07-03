package com.springboot.workflow.entity.requests;

import com.springboot.workflow.entity.Operator;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ProcessSubmitRequest extends ProcessDataRequest {

    @Min(value=1L, message="process instance id < {min}.")
    private long instanceId;

    @NotNull(message="current activity is null.")
    @Size(min=1, max=60, message="process current activity size must be {min} to {max}.")
    @Pattern(regexp="^[\\w-]*$", message="process current activity must be letter(A-Z/a-z), digital(0-9), underline(_) and joiner(-).")
    private String currentActivity;

    @Min(value=1L, message="process task id < {min}.")
    private long taskId;

    @NotNull(message="process handler is null.")
    private Operator operator;

}