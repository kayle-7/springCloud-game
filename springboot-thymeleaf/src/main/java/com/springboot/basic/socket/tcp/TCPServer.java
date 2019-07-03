package com.springboot.basic.socket.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(9800);
        Socket socket = serverSocket.accept();
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("addr : " + socket.getInetAddress() + " connected!");
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        String str;
        while ((str = br.readLine()) != null) {
            if ("over".equals(str)) {
                break;
            }
            System.out.println(str);
            pw.println(str.toUpperCase());
        }
        pw.println("over! addr : " + socket.getInetAddress() + " closed!");
        pw.close();
        br.close();
        socket.close();
        serverSocket.close();
        System.out.println("addr : " + socket.getInetAddress() + " closed!");
    }
}
