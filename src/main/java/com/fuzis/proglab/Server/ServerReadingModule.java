package com.fuzis.proglab.Server;

import com.fuzis.proglab.AppData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ServerReadingModule
{
    public static Logger logger = LoggerFactory.getLogger(ServerReadingModule.class);
    private static void warn(String info) {
        logger.warn(info);
    }

    private static void feedback(String info) {
        logger.info(info);
    }

    private static void error(String info) {
        logger.error(info);
    }
    public static String getNextInteractive()
    {
        try {
            while(true) {
                AppData.TransferData msg = null;
                while(msg == null) {
                    try {
                        msg = (AppData.TransferData) ServerConnectionModule.is.readObject();
                    }
                    catch (IOException ex)
                    {
                        error("Waiting for data: " + ex.getLocalizedMessage());
                        return null;
                    }
                }
                feedback("Got Request");
                switch (msg.purpose()) {
                    case WriteLine:
                    case Write:
                        ServerExecutionModule.msg_handle(msg);
                        break;
                    case Cmd:
                        if (msg.code() == 2) {
                            return msg.body();
                        } else {
                            ServerExecutionModule.command_handle(msg);
                        }
                        break;
                    case Return:
                        warn("Unexpected return: [" + msg.code() + "] " + msg.body());
                        break;
                }
            }
        }
        catch (ClassNotFoundException ex) {
            error("Data Transfer error: " + ex.getLocalizedMessage());
            return null;
        }
    }
}
