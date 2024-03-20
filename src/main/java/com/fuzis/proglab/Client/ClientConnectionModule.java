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
            InetSocketAddress addr;
            try {
                 addr = new InetSocketAddress(AppData.ADRESS, AppData.PORT);
            }
            catch (IllegalArgumentException e)
            {
                error("Invalid port, set to 4352");
                addr = new InetSocketAddress(AppData.ADRESS, 4352);
            }
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
