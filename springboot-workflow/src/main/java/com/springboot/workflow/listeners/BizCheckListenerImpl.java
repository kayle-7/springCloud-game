package com.springboot.workflow.listeners;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class BizCheckListenerImpl implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        String user1 = "102:孙悟空";
        String user2 = "103:猪八戒";
        String user3 = "104:沙悟净";
        //指定组任务
        delegateTask.addCandidateUser(user1);
        delegateTask.addCandidateUser(user2);
        delegateTask.addCandidateUser(user3);
    }
}
