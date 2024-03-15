package com.fuzis.proglab.Client;

import com.fuzis.proglab.AppData;

import java.io.*;


public class ClientMain {


    public static void main(String[] argc) {
        ClientConnectionModule.connect();
        ClientExecutionModule.start();
    }

}