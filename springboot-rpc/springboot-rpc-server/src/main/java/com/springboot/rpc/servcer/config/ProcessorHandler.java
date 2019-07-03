package com.springboot.rpc.servcer.config;

import com.springboot.rpc.dto.RpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class ProcessorHandler implements Runnable{

    private Socket socket;
    private Object service;

    public ProcessorHandler() {

    }

    public ProcessorHandler(Socket socket, Object service) {
        this.socket = socket;
        this.service = service;
    }


    @Override
    public void run() {
        System.out.println("开始执行服务端端处理方法");
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            //拿到输入流
            /*Object*/ RpcRequest request = (RpcRequest)ois.readObject();
            Object result = invoke(request);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(result);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object invoke(RpcRequest request) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Object[] params = request.getParameters();
        Class<?>[] types = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            types[i] = params[i].getClass();
        }
        Method method = service.getClass().getMethod(request.getMethodName(), types);
        Object result = method.invoke(service, params);
        return result;
    }
}
