package com.springboot.workflow.listeners;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class DraftListenerImpl implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        String user = "101:唐僧";
        delegateTask.setAssignee(user);
    }
}
