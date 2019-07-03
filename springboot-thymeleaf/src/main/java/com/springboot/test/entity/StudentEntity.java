package com.springboot.test.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "student对象", description = "student类")
public class StudentEntity {
    @ApiModelProperty(value = "学生id", name = "id", example = "1")
    private Integer id;
    @ApiModelProperty(value = "学生名称", name = "name", required = true)
    private String name;
    private String sex;
    private Integer age;

}
