package com.springboot.basic.socket.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 9800);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String sendStr;
        while ((sendStr = br.readLine()) != null) {
                pw.println(sendStr);
            if ("over".equals(sendStr)) {
                break;
            }
            String result = reader.readLine();
            System.out.println(result);
        }
//        while (true) {
//            String sendStr = br.readLine();
//            if (sendStr != null) {
//                pw.println(sendStr);
//            }
//            if ("over".equals(sendStr)) {
//                break;
//            }
//            String result = reader.readLine();
//            System.out.println(result);
//        }
        System.out.println(reader.readLine());
        reader.close();
        pw.close();
        br.close();
        socket.close();
    }
}
