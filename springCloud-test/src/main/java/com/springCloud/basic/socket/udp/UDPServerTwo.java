package com.springCloud.basic.socket.udp;

import com.springCloud.test.util.UDPUtil;

import java.io.IOException;
import java.net.DatagramSocket;

public class UDPServerTwo {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(9801);
        while (true) {
            boolean closed = UDPUtil.receive(socket);
            if (closed) {
                System.out.println("UDP server two closed!");
                socket.close();
                break;
            }
        }
    }
}
