package com.springboot.workflow.entity;

import com.springboot.workflow.constants.AutoComplete;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by zx
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalData {

    String action;
    String opinion;
    String remark;
    AutoComplete complete;

}
