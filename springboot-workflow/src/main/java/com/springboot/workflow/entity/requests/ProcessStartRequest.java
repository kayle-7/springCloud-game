package com.springboot.workflow.entity.requests;

import com.springboot.workflow.entity.Operator;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ProcessStartRequest extends ProcessDataRequest {

    @NotNull(message="process business key is null.")
    @Size(min=1, max=250, message="process business key range must be {min} to {max}.")
    @Pattern(regexp="^[\\w-]*$", message="process business key must be letter(A-Z/a-z), digital(0-9), underline(_) and joiner(-).")
    private String businessKey;

    @NotNull(message="process creator is null.")
    private Operator operator;

}
