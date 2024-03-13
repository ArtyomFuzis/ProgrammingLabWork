package com.fuzis.proglab.Server;

import com.fuzis.proglab.AppData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ServerWritingModule {
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
    public static void send(AppData.TransferData transfer)
    {
        try {
            ServerConnectionModule.os.writeObject(transfer);
        }
        catch (IOException ex)
        {
            error("Connection error: " + ex.getLocalizedMessage());
        }
    }
}
