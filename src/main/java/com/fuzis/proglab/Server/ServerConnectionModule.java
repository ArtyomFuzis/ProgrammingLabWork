package com.fuzis.proglab.Server;

import com.fuzis.proglab.AppData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnectionModule {
    public static ServerSocket s_socket;
    public static ObjectInputStream is;
    public static ObjectOutputStream os;
    public static Logger logger = LoggerFactory.getLogger(ServerExecutionModule.class);
    private static void warn(String info) {
        logger.warn(info);
    }

    private static void feedback(String info) {
        logger.info(info);
    }

    private static void error(String info) {
        logger.error(info);
    }
    public static boolean tryconnect()
    {
        try {
            if (s_socket == null) {
                s_socket = new ServerSocket(AppData.PORT);
                feedback("Server Socket created");
            }
            Socket socket = s_socket.accept();
            os = new ObjectOutputStream(socket.getOutputStream());
            is = new ObjectInputStream(socket.getInputStream());

            feedback("Connected");
            return true;
        }
        catch (IOException ex) {
            error("Connection error: " + ex.getLocalizedMessage());
            return false;
        }
    }

}
