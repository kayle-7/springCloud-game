package com.springboot.workflow.command.common;

import org.activiti.engine.impl.interceptor.CommandContext;

/**
 * Created by zx
 */
public interface BaseCmd {
    CommandContext getCommandContext();
}
