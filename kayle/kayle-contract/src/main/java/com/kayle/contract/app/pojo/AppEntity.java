package com.kayle.contract.app.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : zx
 * create at:  2019-03-23  14:01
 * @description:
 */
@Data
public class AppEntity implements Serializable {
    private int id;
    private long appId;
    private String appName;
    private String memo;
    private Date buildTime;
}
