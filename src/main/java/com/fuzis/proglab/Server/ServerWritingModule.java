package com.fuzis.proglab.Server;

import com.fuzis.proglab.AppData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class ServerWritingModule {
    public static void write(AppData.TransferData req)
    {
        try {
            var ostr = new ByteArrayOutputStream(1000);
            var os2 = new ObjectOutputStream(ostr);
            os2.writeObject(req);
            var data = ByteBuffer.wrap(ostr.toByteArray());
            final int b_size = 200;

            for(int i = data.limit(); i > 0; i-=b_size+1)
            {
                var arr = new byte[Math.min(b_size+1,i)+2];
                for(int j = 2 ; j <=Math.min(b_size+1,i)+1;j++) {
                    arr[j] = data.get();
                }
                arr[0]=1;
                arr[1]=(byte)Math.min(b_size+1,i);
                ServerConnectionModule.os.write(arr);
            }
            ServerConnectionModule.os.write(new byte[]{2});
        }
        catch (IOException ex)
        {
            ServerConnectionModule.error("Connection Error: "+ex.getLocalizedMessage());
        }

    }
}
