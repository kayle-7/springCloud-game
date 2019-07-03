package com.springCloud.test.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPUtil {

    public static boolean receive(DatagramSocket socket) throws IOException {
        byte[] receive = new byte[1024];
        DatagramPacket packetReceive = new DatagramPacket(receive, 1024);
        socket.receive(packetReceive);
        String receiveStr = new String(receive, 0, packetReceive.getLength());
        System.out.println("UDP receive : " + receiveStr);
        boolean closed = "over".equals(receiveStr);
        boolean client = receiveStr.contains("UDP server two");
        String sendStr;
        if (closed) {
            sendStr = "UDP server received and closed!";
            DatagramPacket packetSend = new DatagramPacket(sendStr.getBytes(), sendStr.length(), packetReceive.getAddress(), packetReceive.getPort());
            socket.send(packetSend);
        } else {
            if (client) {
                sendStr = "UDP server one received!";
            } else {
                sendStr = "UDP server two : UDP server received!";
                DatagramPacket packetSend = new DatagramPacket(sendStr.getBytes(), sendStr.length(), packetReceive.getAddress(), packetReceive.getPort());
                socket.send(packetSend);
            }
        }
        System.out.println(sendStr);
        return closed;
    }
}
