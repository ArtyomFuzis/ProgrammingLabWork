package com.fuzis.proglab.Client;

import com.fuzis.proglab.AppData;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class ClientReadingModule {
    public static AppData.TransferData read() {
        try {
            Selector selector = Selector.open();
            ClientConnectionModule.socket.register(selector, SelectionKey.OP_READ);
            while (true) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                for (var iter = keys.iterator(); iter.hasNext(); ) {
                    SelectionKey key = iter.next();
                    iter.remove();
                    if (key.isValid()) {
                        if (key.isReadable()) {
                            selector.close();
                            return read_inner();
                        }
                    }
                }
            }
        }
        catch (IOException ex)
        {
            ClientConnectionModule.error("Connection fail while reading waiting: " + ex.getLocalizedMessage());
        }
        return null;
    }
    private static AppData.TransferData read_inner() {
        try {
            ArrayList<ByteBuffer> arr = new ArrayList<>();
            int whole_length = 0;
            Thread.sleep(150);
            while (true) {
                ByteBuffer code_b = ByteBuffer.allocate(1);
                ClientConnectionModule.socket.read(code_b);
                code_b.flip();
                int code = code_b.get();
                if (code == 1) {
                    code_b.flip();
                    ClientConnectionModule.socket.read(code_b);
                    code_b.flip();
                    int length = (code_b.get() + 256) % 256;
                    ByteBuffer buf = ByteBuffer.allocate(length);
                    ClientConnectionModule.socket.read(buf);
                    arr.add(buf);
                    whole_length += length;
                } else if (code == 2) break;
            }
            byte[] res_arr = new byte[whole_length];
            int i = 0;
            for (var el : arr) {
                for (var el2 : el.array()) {
                    res_arr[i++] = el2;
                }
            }
            return (AppData.TransferData) (new ObjectInputStream(new ByteArrayInputStream(res_arr))).readObject();
        } catch (IOException ex) {
            ClientConnectionModule.error("Connection reading error: " + ex.getLocalizedMessage());
            ClientConnectionModule.connect();
            return read();
        }
        catch (ClassNotFoundException ex)
        {
            ClientConnectionModule.error("Wrong data error: " + ex.getLocalizedMessage());
        }
        catch (InterruptedException ex)
        {
            ClientExecutionModule.GoOutTheWindow();
        }
        return null;
    }
}
