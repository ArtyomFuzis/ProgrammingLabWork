package com.fuzis.proglab.Client;

import com.fuzis.proglab.AppData;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class ClientMain {
    public static void main(String[] args) {
        try {
            try {
                if (args.length >= 1) AppData.ADRESS = InetAddress.getByName(args[0]);
                else AppData.ADRESS = InetAddress.getLocalHost();
            } catch (UnknownHostException ex) {
                ClientConnectionModule.error("Host not found, redirect to localhost");
                AppData.ADRESS = InetAddress.getLocalHost();
            }
        }
        catch (UnknownHostException ex)
        {
            ClientConnectionModule.error("Net permissions error");
        }
        try {
            if (args.length >= 2) AppData.PORT = Integer.parseInt(args[1]);
            else AppData.PORT = 4352;
        }
        catch (NumberFormatException ex)
        {
            ClientConnectionModule.error("Port not found, redirect to 4352");
            AppData.PORT = 4352;
        }
        ClientConnectionModule.connect();
        ClientExecutionModule.start();
    }

}