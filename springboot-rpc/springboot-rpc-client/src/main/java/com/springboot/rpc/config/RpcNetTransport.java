package com.springboot.rpc.config;

import com.springboot.rpc.dto.RpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RpcNetTransport {

    private String host;
    private int port;

    public RpcNetTransport(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private Socket newSocket() {
        System.out.println("创建一个新的连接");
        Socket socket = null;
        try {
            socket = new Socket(host, port);
        } catch (Exception e) {
            System.out.println("创建一个新的连接失败！");
            e.printStackTrace();
        }
        return socket;
    }

    public Object send(RpcRequest rpcRequest) {
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        Object o = null;
        try {
            socket = newSocket();
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(rpcRequest);
            oos.flush();

            ois = new ObjectInputStream(socket.getInputStream());
            o = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (oos != null) {
                try {
                    oos.close();
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
        return o;
    }
}
