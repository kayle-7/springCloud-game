package com.springboot.basic.socket.udp;

import com.springboot.test.util.UDPUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class UDPServerOne {

    public static void main(String[] args) throws IOException {

        DatagramSocket socket = new DatagramSocket();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String send;
        while ((send = br.readLine()) != null) {
            DatagramPacket packet = new DatagramPacket(send.getBytes(), send.length(), InetAddress.getByName("localhost"), 9801);
            socket.send(packet);
            if (UDPUtil.receive(socket)) {
                br.close();
                socket.close();
                break;
            }
        }
        System.out.println("UDP server one closed!");
    }
}
