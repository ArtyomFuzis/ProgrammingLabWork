package com.fuzis.proglab.Server;

import com.fuzis.proglab.AppData;
import com.fuzis.proglab.Client.ClientConnectionModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class ServerReadingModule {
    public static void interrupt() {
        System.out.println("Shit happens");
    }

    public static Logger logger = LoggerFactory.getLogger(ServerReadingModule.class);

    public static AppData.TransferData read() {
        try {
            ArrayList<byte[]> arr = new ArrayList<>();
            int whole_length = 0;
            while (true) {
                int code = ServerConnectionModule.is.read();
                if (code == 1) {
                    int length = (ServerConnectionModule.is.read() + 256) % 256;
                    var buf = new byte[length];
                    while (ServerConnectionModule.is.available() < length) {
                        interrupt();
                    }
                    ServerConnectionModule.is.read(buf);
                    arr.add(buf);
                    whole_length += length;
                } else if (code == 2) break;
            }
            byte[] res_arr = new byte[whole_length];
            int i = 0;
            for (var el : arr) {
                for (var el2 : el) {
                    res_arr[i++] = el2;
                }
            }
            return (AppData.TransferData) (new ObjectInputStream(new ByteArrayInputStream(res_arr))).readObject();
        } catch (IOException ex) {
            ServerConnectionModule.error("Connection reading error: " + ex.getLocalizedMessage());
        } catch (ClassNotFoundException ex) {
            ServerConnectionModule.error("Wrong data error: " + ex.getLocalizedMessage());
        }
        return null;
    }
}
