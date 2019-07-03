package com.springboot.workflow;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableTransactionManagement
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class WorkflowApp {

    /**
     * 虚拟机默认4G内存
     * -XX:+PrintGCDetails -Xmx32M -Xms32M
     * 打印详细GC日志 最大对内存32M 初始对内存32M
     *
     * jar包启动
     * -server -Xmx32M -Xms32M
     */
    public static void main(String[] args) {
        SpringApplication.run(WorkflowApp.class, args);
    }

}