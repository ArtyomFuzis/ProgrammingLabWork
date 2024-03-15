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
    public static void warn(String info) {
        System.out.println("[WARN] " + info);
    }

    public static void feedback(String info) {
        System.out.println("[INFO] " + info);
    }

    public static void error(String info) {
        System.out.println("[ERROR] " + info);
    }
    public static boolean tryconnect() {
        try {
            InetSocketAddress addr = new InetSocketAddress(InetAddress.getLocalHost(), AppData.PORT);
            socket = SocketChannel.open(addr);
            socket.configureBlocking(false);
            return true;
        } catch (IOException ex) {
            error("Connection error: " + ex.getLocalizedMessage());
            return false;
        }
    }
    public static void connect()
    {
        try {
            while (!ClientConnectionModule.tryconnect()) {
                System.out.println("Server not available, reconnecting");
                Thread.sleep(1000);
            }
        }
        catch (InterruptedException ex)
        {
            ClientExecutionModule.GoOutTheWindow();
        }
    }
}
