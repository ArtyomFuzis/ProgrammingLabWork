package com.fuzis.proglab.Client;

import com.fuzis.proglab.AppData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class ClientWritingModule {
    public static void write(AppData.TransferData data) {
        try {
            var ostr = new ByteArrayOutputStream(255);
            var os = new ObjectOutputStream(ostr);
            os.writeObject(data);
            ClientConnectionModule.socket.write(ByteBuffer.wrap(ostr.toByteArray()));
        }
        catch (IOException ex)
        {
            System.out.println("Connection error: " + ex.getLocalizedMessage());
        }
    }
}
