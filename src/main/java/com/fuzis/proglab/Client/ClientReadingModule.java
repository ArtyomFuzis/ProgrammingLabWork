package com.fuzis.proglab.Client;

import com.fuzis.proglab.AppData;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ClientReadingModule {
    public static AppData.TransferData read() {
        try {
            ArrayList<ByteBuffer> arr = new ArrayList<>();
            int whole_length = 0;
            while (true) {
                ByteBuffer code_b = ByteBuffer.allocate(1);
                ClientConnectionModule.socket.read(code_b);
                code_b.flip();
                int code = code_b.get();
                if (code == 1) {
                    code_b.flip();
                    ClientConnectionModule.socket.read(code_b);
                    code_b.flip();
                    int length = (code_b.get() + 255) % 255;
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
        }
        catch (ClassNotFoundException ex)
        {
            ClientConnectionModule.error("Wrong data error: " + ex.getLocalizedMessage());
        }
        return null;
    }
}
