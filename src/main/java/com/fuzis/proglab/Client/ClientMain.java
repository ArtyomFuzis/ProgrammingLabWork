package com.fuzis.proglab.Client;

import com.fuzis.proglab.AppData;

import java.io.*;


public class ClientMain {

    public static void main(String[] argc) throws IOException {
        ClientConnectionModule.tryconnect();

        
        ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"show",2,null));

        ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"execute_script 1.script",2,null));

        ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"info",2,null));

        ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.WriteLine,"info",2,null));
        ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.WriteLine,"info123",2,null));

        /*var res = ClientReadingModule.read();
        System.out.println(res.map());*/

        

    }

}