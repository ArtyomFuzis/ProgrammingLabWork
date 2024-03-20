/*
"Если меня не кормят, я уже не я" -- так он однажды выразился. Фрекен Бок мечтала,
 чтобы Карлсон не проводил у них время, но тщетно, потому что Малыш и дядя Юлиус были
 на его стороне. Фрекен Бок всегда ворчала, если он появлялся как раз в тот момент,
 когда надо садиться за стол, но сделать она ничего не могла, и Карлсон ел вместе со всеми.
 */
package com.fuzis.proglab.Server;

import com.fuzis.proglab.AppData;
import com.fuzis.proglab.Client.ClientConnectionModule;
import com.fuzis.proglab.Client.ClientWritingModule;

import java.io.*;

public class ServerMain {
    public static void main(String[] args) {
        if (args.length >= 1) CharacterCollection.fileName = args[0];
        try {
            if (args.length >= 2) AppData.PORT = Integer.parseInt(args[1]);
            else AppData.PORT = 4352;
        }
        catch (NumberFormatException ex)
        {
            ClientConnectionModule.error("Port not found, redirect to 4352");
            AppData.PORT = 4352;
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> CharacterCollection.getInstance().save(), "Shutdown"));
        boolean listening_exit = false;
        while (!listening_exit) {
            if (ServerConnectionModule.tryconnect()) ServerExecutionModule.start_interactive();
        }
    }
}