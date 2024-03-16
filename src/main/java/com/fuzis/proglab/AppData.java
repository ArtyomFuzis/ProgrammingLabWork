package com.fuzis.proglab;

import java.io.Serial;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

public class AppData {
    public static int PORT;
    public static InetAddress ADRESS;


    public enum TransferPurpose
    {
        Msg,
        Cmd,
        Return
    }
    public record TransferData(TransferPurpose purpose, String body, Integer code,HashMap<String,InteractiveCommandData> map) implements Serializable
    {
        @Serial
        private static final long serialVersionUID = 1L;
    }
    public record InteractiveCommandData(int[] args, String[] usage, String help) implements Serializable{
        @Serial
        private static final long serialVersionUID = 3L;
    }
}
