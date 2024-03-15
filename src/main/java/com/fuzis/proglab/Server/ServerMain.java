/*
"Если меня не кормят, я уже не я" -- так он однажды выразился. Фрекен Бок мечтала,
 чтобы Карлсон не проводил у них время, но тщетно, потому что Малыш и дядя Юлиус были
 на его стороне. Фрекен Бок всегда ворчала, если он появлялся как раз в тот момент,
 когда надо садиться за стол, но сделать она ничего не могла, и Карлсон ел вместе со всеми.
 */
package com.fuzis.proglab.Server;

import com.fuzis.proglab.AppData;
import com.fuzis.proglab.Client.ClientWritingModule;

import java.io.*;

public class ServerMain {
    public static void main(String[] args) {
        if (args.length >= 1) CharacterCollection.fileName = args[0];
        boolean listening_exit = false;
        while (!listening_exit) {
            if (ServerConnectionModule.tryconnect()) ServerExecutionModule.start_interactive();
        }
    }
}