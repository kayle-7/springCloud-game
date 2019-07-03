package com.springboot.workflow.server;

public interface Server {

    default void restart() throws Exception {
        stop();
        start();
    }

    void start() throws Exception;

    void stop();

}
