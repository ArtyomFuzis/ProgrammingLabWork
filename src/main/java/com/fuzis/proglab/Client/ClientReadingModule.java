package com.fuzis.proglab.Client;

import com.fuzis.proglab.AppData;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Arrays;
import java.util.Set;

public class ClientReadingModule {
    public static AppData.TransferData read()
    {
        return read(20000);
    }
    public static AppData.TransferData read(int capacity) {
        try {
            var bb = ByteBuffer.allocate(capacity);
            Selector selector = Selector.open();
            ClientConnectionModule.socket.register(selector, SelectionKey.OP_READ);
            AppData.TransferData req = null;
            while (req == null) {
                try {
                    System.out.println("aasas2");
                    selector.select();
                    System.out.println("aasas");
                    Set<SelectionKey> keys = selector.selectedKeys();
                    System.out.println(keys.size());
                    for (var iter = keys.iterator(); iter.hasNext(); ) {
                        SelectionKey key = iter.next();
                        iter.remove();
                        if (key.isValid()) {
                            if (key.isReadable()) {
                                ClientConnectionModule.socket.read(bb);
                            }
                        }
                    }
                    System.out.println(Arrays.toString(bb.array()));
                    var bis = new ByteArrayInputStream(bb.array());
                    var is = new ObjectInputStream(bis);
                    req = (AppData.TransferData) is.readObject();
                    selector.close();
                } catch (StreamCorruptedException ex) {
                    System.out.println("Wait for next data: " + ex.getLocalizedMessage());
                }
            }
            return req;
        } catch (IOException ex) {
            System.out.println("Connection error: " + ex.getLocalizedMessage());
            return null;
        } catch (ClassNotFoundException ex) {
            System.out.println("Broken data error: " + ex.getLocalizedMessage());
            return null;
        }
    }
}
