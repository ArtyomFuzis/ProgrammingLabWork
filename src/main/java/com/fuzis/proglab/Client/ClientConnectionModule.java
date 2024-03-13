package com.fuzis.proglab.Client;

import com.fuzis.proglab.AppData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class ClientConnectionModule {
    public static SocketChannel socket;

    public static boolean tryconnect() {
        try {
            InetSocketAddress addr = new InetSocketAddress(InetAddress.getLocalHost(), AppData.PORT);
            socket = SocketChannel.open(addr);
            socket.configureBlocking(false);
            return true;
        } catch (IOException ex) {
            System.out.println("Connect error: " + ex.getLocalizedMessage());
            return false;
        }
    }
}
