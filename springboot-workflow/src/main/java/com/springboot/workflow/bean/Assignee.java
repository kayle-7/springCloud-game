package com.springboot.workflow.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by zx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Assignee implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String role;

}
