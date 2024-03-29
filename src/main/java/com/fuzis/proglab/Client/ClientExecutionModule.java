package com.fuzis.proglab.Client;

import com.fuzis.proglab.Annotations.InteractiveCommand;
import com.fuzis.proglab.AppData;
import com.fuzis.proglab.Enums.Opinion;
import com.fuzis.proglab.InteractiveInput;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ClientExecutionModule {
    public static void warn(String info) {
        System.out.println("[WARN] " + info);
    }

    public static void feedback(String info) {
        System.out.println("[INFO] " + info);
    }

    public static void error(String info) {
        System.out.println("[ERROR] " + info);
    }

    public static Queue<String> history = new LinkedList<>();
    public static HashMap<String, AppData.InteractiveCommandData> commands;
    public static Boolean exit=false;

    public static class ClientCmds {
        public static void print(Object a) {
            System.out.print(a);
        }

        public static void println(Object a) {
            System.out.println(a);
        }

        public static void println() {
            System.out.println();
        }

        @InteractiveCommand(args = {0}, usage = {"history - показать последние 14 выполненных команд"}, help = "Показывает историю выполнения команд")
        public void history(List<String> argc) {
            if (!history.isEmpty()) {
                System.out.println("Last commands:");
                for (var el : history) {
                    System.out.println(el);
                }
            } else {
                feedback("History is empty");
            }
        }

        @InteractiveCommand(args = {0, 1}, usage = {"help_serv - вывод справки по всем командам сервера", "help_serv <команда> - вывод справки по определенной команде сервера"}, help = "Выводит справку по командам интерактивного режима сервера")
        public void help_serv(List<String> argc) {
            if (argc.isEmpty()) {
                println("Все команды, доступные в интерактивном режиме сервера:");
                println();
                for (var el : commands.keySet()) {
                    println("-----------Команда-----------");
                    println(el + ": " + commands.get(el).help());
                    println("--------Использование--------");
                    for (var el2 : commands.get(el).usage()) println(el2);
                    println();
                }
            } else {
                var el = commands.get(argc.get(0));
                if(el == null)
                {
                    error("No such command");
                    return;
                }
                println("--------Использование--------");
                for (var el2 : el.usage()) println(el2);
            }
        }
        @InteractiveCommand(args = {0, 1}, usage = {"help - вывод справки по всем командам", "help <команда> - вывод справки по определенной команде"}, help = "Выводит справку по командам интерактивного режима")
        public void help(List<String> argc) {
            if (argc.isEmpty()) {
                println("Все команды, доступные в интерактивном режиме:");
                println();
                for (var el : ClientExecutionModule.ClientCmds.class.getMethods()) {
                    var anot = el.getAnnotation(InteractiveCommand.class);
                    if (anot == null) continue;
                    println("-----------Команда-----------");
                    println(el.getName() + ": " + anot.help());
                    println("--------Использование--------");
                    for (var el2 : anot.usage()) println(el2);
                    println();
                }
            } else {
                try {
                    var el = ClientExecutionModule.ClientCmds.class.getMethod(argc.get(0), List.class);
                    var anot = el.getAnnotation(InteractiveCommand.class);
                    println("--------Использование--------");
                    for (var el2 : anot.usage()) println(el2);
                } catch (NoSuchMethodException ex) {
                    error("No such command");
                }
            }
        }
        @InteractiveCommand(args = {0, 1}, usage = {"msg - отправляет сообщение на сервер", "msg <сообщение> - отправка указанного текстового сообщения на сервер"}, help = "Посылает простые сообщения на сервер")
        public void msg(List<String> argc) {
            ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Msg, argc.get(0),0,null ));
            println("Message send");
        }
        @InteractiveCommand(args={0},usage={"exit заканчивает работу клиента"},help="Завершение работы программы ")
        public void exit(List<String> argc)
        {
            feedback("Exiting...");
            exit = true;
        }
    }


    public static void begin() {
        ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Cmd, "", 3, null));
        var response = ClientReadingModule.read();
        if (response.purpose() != AppData.TransferPurpose.Return) GoOutTheWindow();
        while(response.code() != 3)response = ClientReadingModule.read();
        commands = response.map();
    }
    public static Scanner scan;
    public static void GoOutTheWindow() {
        System.out.println("Unbelievable shit really happens...");
        System.exit(666);
    }
    public static String toStr(Object a)
    {
        if(a == null)return null;
        try
        {
            var map = (HashMap<String, Opinion>) a;
            StringBuilder res = new StringBuilder();
            for(var el : map.keySet())
            {
                res.append(el).append(":").append(map.get(el)).append(",");
            }
            return res.toString();
        }
        catch (ClassCastException ex)
        {
            try
            {
                var arr = (ArrayList<String>) a;
                StringBuilder res = new StringBuilder();
                for(var el : arr)res.append(el);
                return res.toString();
            }
            catch (ClassCastException ex2)
            {
                return a.toString();
            }

        }
    }
    public static void interactivecommand_handle(InteractiveInput inp,boolean type)
    {
        if(type)ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Return,toStr(inp.type_interactive()),10,null));
        else ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Return,toStr(inp.id_interactive()),23,null));
        while(true)
        {
            var req = ClientReadingModule.read();
            if(req == null)
            {
                error("Server response error");
            }
            if(req.purpose() == AppData.TransferPurpose.Cmd && req.code()==1)
            {
                feedback("Interactive command execution ended");
                break;
            }
            switch(req.code())
            {
                case 10->ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Return,inp.type_interactive(),10,null));
                case 11->ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Return,toStr(inp.name_interactive()),11,null));
                case 12->ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Return,toStr(inp.sex_interactive()),12,null));
                case 13->ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Return,toStr(inp.quote_interactive()),13,null));
                case 14->ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Return,toStr(inp.height_interactive()),14,null));
                case 15->ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Return,toStr(inp.weight_interactive()),15,null));
                case 16->ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Return,toStr(inp.popularity_interactive()),16,null));
                case 17->ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Return,toStr(inp.age_interactive()),17,null));
                case 18->ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Return,toStr(inp.description_interactive()),18,null));
                case 19->ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Return,toStr(inp.health_interactive()),19,null));
                case 20->ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Return,toStr(inp.isAnimeCharacter_interactive()),20,null));
                case 21->ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Return,toStr(inp.additionalnames_interactive()),21,null));
                case 22->ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Return,toStr(inp.opinions_interactive()),22,null));
                case 23->ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Return,toStr(inp.id_interactive()),23,null));
            }
        }
        var resp = ClientReadingModule.read();
        System.out.print(resp.body());
    }
    public static void start() {
        ClientCmds cmd_class = new ClientCmds();
        begin();
        feedback("Interactive mode started");
        scan = new Scanner(System.in);
        while (!exit && scan.hasNext()) {
            String pre = scan.nextLine().trim();
            if (pre.trim().isEmpty()) continue;
            String[] cmd = pre.split("\\s+");
            try {
                var cmd_func = ClientCmds.class.getMethod(cmd[0], List.class);
                var args_size = cmd_func.getAnnotation(InteractiveCommand.class).args();
                if (Arrays.stream(args_size).noneMatch(x -> x == cmd.length - 1)) {
                    error("Wrong count of arguments");
                    var arg = new ArrayList<String>();
                    arg.add(cmd[0]);
                    cmd_class.help(arg);
                    continue;
                }
                if (cmd.length == 1) cmd_func.invoke(cmd_class, new ArrayList<String>());
                else {
                    cmd_func.invoke(cmd_class, Arrays.stream(Arrays.copyOfRange(cmd, 1, cmd.length)).toList());
                }
                history.add(pre);
                if (history.size() > 14) {
                    history.poll();
                }
            } catch (NoSuchMethodException ex) {
                if(commands.get(cmd[0]) == null)
                {
                    error("No such command");
                }
                else
                {
                    var cur_command = commands.get(cmd[0]);
                    if (Arrays.stream(cur_command.args()).noneMatch(x -> x == cmd.length - 1)) {
                        error("Wrong count of arguments");
                        var arg = new ArrayList<String>();
                        arg.add(cmd[0]);
                        cmd_class.help_serv(arg);
                    }
                    else {
                        ClientWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Cmd,pre,2,null));
                        var res = ClientReadingModule.read();
                        if(res == null)
                        {
                            error("Server response error");
                        }
                        else
                        {
                            //System.out.println(res);
                            switch(res.purpose())
                            {
                                case Return:
                                    switch (res.code())
                                    {
                                        case 1-> System.out.print(res.body());
                                    }
                                    break;
                                case Cmd:
                                    var inp = new InteractiveInput(new InteractiveInput.FakeScanner(scan),ClientCmds::println,ClientCmds::print);
                                    switch (res.code())
                                    {
                                        case 10-> interactivecommand_handle(inp,true);
                                        case 23->interactivecommand_handle(inp,false);
                                    }
                                    break;
                                case Msg:
                                    feedback("Got message: " + res.body());
                                    break;
                            }
                            history.add(pre);
                            if (history.size() > 14) {
                                history.poll();
                            }
                        }

                    }
                }
            } catch (InvocationTargetException | IllegalAccessException e) {
                error("Command execution error");
            }
        }
    }
}
