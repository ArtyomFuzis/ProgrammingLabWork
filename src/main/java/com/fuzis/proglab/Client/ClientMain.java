package com.fuzis.proglab.Client;

import com.fuzis.proglab.AppData;
import com.fuzis.proglab.Server.ServerWritingModule;

import java.io.*;

import java.net.InetAddress;

import java.net.InetSocketAddress;

import java.nio.ByteBuffer;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import java.util.Arrays;
import java.util.Set;

public class ClientMain {

    public static void main(String[] argc){
        ClientConnectionModule.tryconnect();
        ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"execute_script 1.script",2,null));

        ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"show",2,null));


        ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"info",2,null));

        ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.WriteLine,"info",2,null));
        ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"info",3,null));
        /*var res = ClientReadingModule.read();
        System.out.println(res.map());*/

        

    }

}