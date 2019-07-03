package com.springboot.workflow.listeners;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class FinCheckListenerImpl implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        String user1 = "105:观音";
        String user2 = "106:如来";
        String user3 = "107:玉皇大帝";
        //指定组任务
        delegateTask.addCandidateUser(user1);
        delegateTask.addCandidateUser(user2);
        delegateTask.addCandidateUser(user3);
    }
}
