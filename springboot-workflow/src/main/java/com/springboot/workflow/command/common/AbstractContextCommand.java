package com.springboot.workflow.command.common;

import org.activiti.engine.TaskService;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

/**
 * Created by zx
 */
public abstract class AbstractContextCommand<T> implements Command<T> {
    private CommandContext commandContext;

    protected void setCommandContext(CommandContext commandContext)
    {
        this.commandContext = commandContext;
    }

    public CommandContext getCommandContext() {
        return this.commandContext;
    }

    @Override
    public T execute(CommandContext commandContext)
    {
        setCommandContext(commandContext);
        return doExecute(commandContext);
    }

    protected abstract T doExecute(CommandContext paramCommandContext);

    protected TaskService getTaskService() {
        return getCommandContext().getProcessEngineConfiguration().getTaskService();
    }
}