package com.fuzis.proglab.Server;


import com.fuzis.proglab.Annotations.HiddenCommand;
import com.fuzis.proglab.Annotations.InteractiveCommand;
import com.fuzis.proglab.AppData;
import com.fuzis.proglab.Enums.Opinion;
import com.fuzis.proglab.Enums.Popularity;
import com.fuzis.proglab.Enums.Sex;
import com.fuzis.proglab.DefaultCartoonPersonCharacter;
import com.fuzis.proglab.InteractiveInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;

public class ServerExecutionModule {
    public static final Logger logger = LoggerFactory.getLogger(ServerExecutionModule.class);

    public static void warn(String info) {
        logger.warn(info);
        buffer.append("[ServerWARN] ").append(info).append("\n");
    }

    public static void feedback(String info) {
        logger.info(info);
        buffer.append("[ServerINFO] ").append(info).append("\n");
    }

    public static void error(String info) {
        logger.error(info);
        buffer.append("[ServerERROR] ").append(info).append("\n");
    }

    record ScriptData(InteractiveInput.FakeScanner file, String prev_key) {
    }

    public static HashMap<String, ScriptData> executing_scripts = new HashMap<>();
    public static String cur_script = null;
    public static CharacterCollection char_col;
    public static Boolean exit = false;
    public static Boolean supress_inp_invite = false;
    public static InteractiveInput.FakeScanner scan;
    public static InteractiveInput.FakeScanner in_scan;
    public static void println_supress(Object a) {
        if (supress_inp_invite) return;
        println(a);
    }

    public static void print_supress(Object a) {
        if (supress_inp_invite) return;
        print(a);
    }

    static StringBuilder buffer = new StringBuilder();

    public static void print(Object a) {
        logger.trace("Added to response: " + a.toString());
        buffer.append(a.toString());
        //System.out.print(a);
    }

    public static void println(Object a) {
        logger.trace("Added to response: " + a.toString());
        buffer.append(a.toString()).append("\n");
        //System.out.println(a);
    }

    public static void println() {
        buffer.append("\n");
        //System.out.println();
    }

    public static void send() {
        ServerWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Return, buffer.toString(), 1, null));
        buffer = new StringBuilder();
    }

    public static class Cmds {
        public static class IDCharacter {
            public DefaultCartoonPersonCharacter character;
            public String id;

            public IDCharacter(String id, DefaultCartoonPersonCharacter character) {
                this.id = id;
                this.character = character;
            }
        }



        @HiddenCommand
        @InteractiveCommand(args = {0, 1}, usage = {"help - вывод справки по всем командам", "help <команда> - вывод справки по определенной команде"}, help = "Выводит справку по командам интерактивного режима")
        public void help(List<String> argc) {
            if (argc.isEmpty()) {
                println("Все команды, доступные в интерактивном режиме:");
                println();
                for (var el : Cmds.class.getMethods()) {
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
                    var el = Cmds.class.getMethod(argc.get(0), List.class);
                    var anot = el.getAnnotation(InteractiveCommand.class);
                    println("--------Использование--------");
                    for (var el2 : anot.usage()) println(el2);
                } catch (NoSuchMethodException ex) {
                    error("No such command");
                }
            }
        }

        @InteractiveCommand(args = {0}, usage = {"show - вывод всех элементов коллекции"}, help = "Выводит элементы коллекции")
        public void show(List<String> argc) {
            if (char_col.getCharacters().isEmpty()) feedback("It is empty");
            for (var el : char_col.getCharacters().keySet()) {
                println(el + ": " + char_col.getCharacters().get(el));
            }
        }

        public Cmds.IDCharacter add_interactive() {
            InteractiveInput inp;
            if(scan != in_scan) inp = new InteractiveInput(scan,ServerExecutionModule::println_supress,ServerExecutionModule::print_supress);
            else inp = new InteractiveInput(ServerWritingModule.class, ServerReadingModule.class);
            var id = inp.id_interactive();
            if (char_col.getCharacters().containsKey(id)) {
                println_supress("Объект с данным id уже существует, попробуйте использовать update");
                if(scan == in_scan)ServerWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"",1,null));
                return null;
            }
            String name = inp.name_interactive();
            Sex sex = inp.sex_interactive();
            String quote = inp.quote_interactive();
            Double height = inp.height_interactive();
            Double weight = inp.weight_interactive();
            Popularity popul = inp.popularity_interactive();
            String description = inp.description_interactive();
            Double age = inp.age_interactive();
            Integer health = inp.health_interactive();
            Boolean isAnimeCharacter = inp.isAnimeCharacter_interactive();
            List<String> additionalNames = inp.additionalnames_interactive();
            HashMap<String, Opinion> opinions = inp.opinions_interactive();
            if(scan == in_scan)ServerWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"",1,null));
            return new Cmds.IDCharacter(id, new DefaultCartoonPersonCharacter(name, sex, quote, opinions, additionalNames, height, weight, isAnimeCharacter, popul, description, age, health));
        }

        @InteractiveCommand(args = {0}, usage = {"add - добавление персонажа в коллекцию, значение вводится построчно:", "<id> - строка-индификатор", "<name> - имя", "<sex> пол персонажа, элемент из перечисления Sex", "<quote> - строка, цитата персонажа", "<height> - рост персонажа", "<weight> - вес персонажа", "<popularity> - популярность персонажа, элемент из перечисления Popularity", "<description> - строка, описание персонажа", "<age> - возраст персонажа", "<health> - значение здоровья персонажа в целочисленных условных единицах", "<isAnimeCharacter> - является ли アニメ персонажем, значение Yes/No", "<additionalNames> - строка, дополнительные имена персонажа, перечисление через запятую", "<opinions> - мнения о других персонажах (не обязательно из коллекции) в виде <имя>:<отношение>, <имя2>:<отношение2>... отношение - значение из перечисления Opinion"}, help = "Производит добавления элемента в коллекцию")
        public void add(List<String> argc) {
            var new_charac = add_interactive();
            if(new_charac == null)return;
            char_col.add(new_charac.id, new_charac.character);
            feedback("Successful add");
        }

        @InteractiveCommand(args = {1}, usage = {"update <id>- изменение персонажа, содержащегося в коллекции, можно изменять конкретные поля, в остальных останутся предыдущие значения, end - для выхода", "<name> - имя", "<sex> пол персонажа, элемент из перечисления Sex", "<quote> - строка, цитата персонажа", "<height> - рост персонажа", "<weight> - вес персонажа", "<popularity> - популярность персонажа, элемент из перечисления Popularity", "<description> - строка, описание персонажа", "<age> - возраст персонажа", "<health> - значение здоровья персонажа в целочисленных условных единицах", "<isAnimeCharacter> - является ли アニメ персонажем, значение Yes/No", "<additionalNames> - строка, дополнительные имена персонажа, перечисление через запятую", "<opinions> - мнения о других персонажах (не обязательно из коллекции) в виде <имя>:<отношение>, <имя2>:<отношение2>... отношение - значение из перечисления Opinion"}, help = "Изменяет элемент в коллекции")
        public void update(List<String> argc) {
            InteractiveInput inp;
            if(scan != in_scan) inp = new InteractiveInput(scan,ServerExecutionModule::println_supress,ServerExecutionModule::print_supress);
            else inp = new InteractiveInput(ServerWritingModule.class, ServerReadingModule.class);
            String id = argc.get(0);
            var charac = char_col.getCharacter(id);
            if (charac == null) {
                error("Character not found");
                return;
            }
            while (true) {
                var str = inp.type_interactive();
                feedback("Updating: " + str);
                switch (str) {
                    case "name" -> charac.setName(inp.name_interactive());
                    case "sex" -> charac.setSex(inp.sex_interactive());
                    case "quote" -> charac.setQuote(inp.quote_interactive());
                    case "opinions" -> charac.setOpinions(inp.opinions_interactive());
                    case "additionalnames" -> charac.setAdditionalNames(inp.additionalnames_interactive());
                    case "height" -> charac.setHeight(inp.height_interactive());
                    case "weight" -> charac.setWeight(inp.weight_interactive());
                    case "age" -> charac.setAge(inp.age_interactive());
                    case "health" -> charac.setHealth(inp.health_interactive());
                    case "isanimecharacter" -> charac.setAnimeCharacter(inp.isAnimeCharacter_interactive());
                    case "popularity" -> charac.setPopularity(inp.popularity_interactive());
                    case "description" -> charac.setDescription(inp.description_interactive());
                    case "end" -> {
                        if(in_scan == scan)ServerWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"",1,null));
                        feedback("End update");
                        return;
                    }
                    default -> error("Field not found!!!");
                }
            }
        }

        @HiddenCommand
        @InteractiveCommand(args = {0}, usage = {"save - сохранить коллекцию в ранее указанный файл"}, help = "Сохраняет коллекцию")
        public void save(List<String> argc) {
            char_col.save();
        }

        @HiddenCommand
        @InteractiveCommand(args = {0}, usage = {"exit_server - завершить работу интерактивного режима"}, help = "Осуществляет выход из программы/подпрограммы")
        public void exit_server(List<String> argc) {
            exit = true;
            feedback("Exiting...");
        }

        @InteractiveCommand(args = {0}, usage = {"clear - полная очистка коллекции"}, help = "Осуществляет очистку коллекции")
        public void clear(List<String> argc) {
            char_col.clear();
            feedback("Successful clearing");
        }

        @InteractiveCommand(args = {1}, usage = {"remove_by_id <id> - удалить элемент с указанным id"}, help = "Удаляет указанный элемент")
        public void remove_by_id(List<String> argc) {
            if (null == char_col.deleteCharacter(argc.get(0))) error("key not found");
            else feedback("Successful remove");
        }

        @InteractiveCommand(args = {0}, usage = {"add_if_max - добавляет новый элемент если он больше любого другого в коллекции, формат ввода объекта, такой же как и у add"}, help = "Добавляет элемент если он максимальный")
        public void add_if_max(List<String> argc) {
            var new_charac = add_interactive();
            if (char_col.getCharacters().values().stream().allMatch(x -> x.compareTo(new_charac.character) <= 0)) {
                char_col.add(new_charac.id, new_charac.character);
                feedback("Successful add");
            } else {
                feedback("It is lower than something -> not added");
            }
        }

        @InteractiveCommand(args = {0}, usage = {"add_if_min - добавляет новый элемент если он меньше любого другого в коллекции, формат ввода объекта, такой же как и у add"}, help = "Добавляет элемент если он минимальный")
        public void add_if_min(List<String> argc) {
            var new_charac = add_interactive();
            if (char_col.getCharacters().values().stream().allMatch(x -> x.compareTo(new_charac.character) >= 0)) {
                char_col.add(new_charac.id, new_charac.character);
                feedback("Successful add");
            } else {
                feedback("It is bigger than something -> not added");
            }
        }

        @InteractiveCommand(args = {0}, usage = {"min_by_age - выводит какой-то объект из коллекции, значение поля age которого - минимально"}, help = "Выводит персонажа, минимального по возрасту")
        public void min_by_age(List<String> argc) {
            if (char_col.size() == 0) {
                feedback("Collection is empty");
                return;
            }
            ArrayList<Cmds.IDCharacter> id_character_arr = new ArrayList<>();
            for (var el : char_col.getCharacters().keySet()) {
                id_character_arr.add(new Cmds.IDCharacter(el, char_col.getCharacter(el)));
            }
            var res = id_character_arr.stream().min((o1, o2) -> {
                if (Objects.equals(o1.character.getAge(), o2.character.getAge())) return 0;
                if (o1.character.getAge() == null) return 1;
                if (o2.character.getAge() == null) return -1;
                if (o1.character.getAge() > o2.character.getAge()) return 1;
                return -1;
            }).get();
            println(res.id + ": " + res.character);
        }

        public static record SSPair(String one, Object two) {

        }

        @InteractiveCommand(args = {0}, usage = {"print_field_descending_name - выводит имена элементов из коллекции, отсортированные по убыванию"}, help = "Выводит имена персонажей, в порядке убывания")
        public void print_field_descending_name(List<String> argc) {
            if (char_col.getCharacters().isEmpty()) {
                feedback("Collection is empty");
                return;
            }
            ArrayList<Cmds.SSPair> names = new ArrayList<>();
            for (var el : char_col.getCharacters().keySet()) {
                names.add(new Cmds.SSPair(el, char_col.getCharacter(el).getName()));
            }
            names.sort(((o1, o2) -> -((String) o1.two).compareTo((String) o2.two)));
            for (var el : names) {
                println(el.one + ": " + el.two);
            }
        }

        @InteractiveCommand(args = {0}, usage = {"print_field_ascending_health - выводит здоровье элементов из коллекции, отсортированные по возрастанию"}, help = "Выводит здоровье персонажей, в порядке возрастания")
        public void print_field_ascending_health(List<String> argc) {
            if (char_col.getCharacters().isEmpty()) {
                feedback("Collection is empty");
                return;
            }
            ArrayList<Cmds.SSPair> ages = new ArrayList<>();
            for (var el : char_col.getCharacters().keySet()) {
                ages.add(new Cmds.SSPair(el, char_col.getCharacter(el).getHealth()));
            }
            ages.sort(((o1, o2) -> {
                if (o1.two == o2.two && o1.two == null) return 0;
                if (o1.two == null) return 1;
                if (o2.two == null) return -1;
                return ((Integer) o1.two).compareTo((Integer) o2.two);
            }));
            for (var el : ages) {
                println(el.one + ": " + el.two);
            }
        }

        @InteractiveCommand(args = {0}, usage = {"info - выводит некоторую информацию о коллекции"}, help = "Выводит информацию о коллекции персонажей")
        public void info(List<String> argc) {
            println(char_col.getInfo());
        }

        @InteractiveCommand(args = {1}, usage = {"execute_script <filename> - запускает скрипт по адресу <filename>"}, help = "Запускает скрипт")
        public void execute_script(List<String> argc) {
            if (executing_scripts.containsKey(argc.get(0))) {
                error("Script is already executing");
                return;
            }
            try {
                File f = new File(argc.get(0));
                scan = new InteractiveInput.FakeScanner(new Scanner(f));
                feedback("Start executing script: " + argc.get(0));
                supress_inp_invite = true;
                executing_scripts.put(argc.get(0), new ScriptData(scan, cur_script));
                cur_script = argc.get(0);
            } catch (FileNotFoundException ex) {
                error("File not found or unable to read");
            }
        }
    }

    public static void msg_handle(AppData.TransferData msg) {
        ServerConnectionModule.feedback("Got message: " + msg.body());
    }

    public static void command_handle(AppData.TransferData msg) {
        switch (msg.code()) {
            case 3:
                var map = new HashMap<String, AppData.InteractiveCommandData>();
                for (var el : Cmds.class.getMethods()) {
                    var annot = el.getAnnotation(InteractiveCommand.class);
                    if (annot == null || el.getAnnotation(HiddenCommand.class) != null) continue;
                    map.put(el.getName(), new AppData.InteractiveCommandData(annot.args(), annot.usage(), annot.help()));
                }
                ServerWritingModule.write(new AppData.TransferData(AppData.TransferPurpose.Return,
                        "", 3, map));
                break;
        }
    }

    public static String getNextInteractive() {
        while (true) {
            AppData.TransferData msg = ServerReadingModule.read();
            ServerConnectionModule.feedback("Got Request: " + msg);
            if (msg == null) return null;
            switch (msg.purpose()) {
                case Msg:
                    ServerExecutionModule.msg_handle(msg);
                    break;
                case Cmd:
                    if (msg.code() == 2) {
                        return msg.body();
                    } else {
                        ServerExecutionModule.command_handle(msg);
                    }
                    break;
                case Return:
                    ServerConnectionModule.warn("Unexpected return: [" + msg.code() + "] " + msg.body());
                    break;
            }
        }
    }

    public static void start_interactive() {
        exit = false;
        char_col = CharacterCollection.getInstance();
        Cmds cmd_class = new Cmds();
        ServerConnectionModule.feedback("Interactive mode server started");
        in_scan = new InteractiveInput.FakeScanner((a) -> {
            var res = getNextInteractive();
            if (res == null) a.close();
            a.add(res);
        });
        supress_inp_invite = false;
        while (scan == null || !scan.equals(in_scan)) {
            if(scan != null)send();
            scan = in_scan;
            while (!exit && scan.hasNext()) {
                String pre = scan.nextLine().trim();
                if (pre.trim().isEmpty()) continue;
                String[] cmd = pre.split("\\s+");
                try {
                    var cmd_func = Cmds.class.getMethod(cmd[0], List.class);
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
                } catch (NoSuchMethodException ex) {
                    error("No such command");
                } catch (InvocationTargetException | IllegalAccessException e) {
                    error("Command execution error");
                }
                if(scan.equals(in_scan))send();
                while (!scan.hasNext() && !exit) {
                    var prev = executing_scripts.get(cur_script).prev_key;
                    if (prev != null) {
                        scan = executing_scripts.get(prev).file;
                        executing_scripts.remove(cur_script);
                        feedback("End "+cur_script+" inner script execution");
                        cur_script = prev;
                    } else {
                        executing_scripts.remove(cur_script);
                        cur_script = null;
                        feedback("End all script executions");
                        break;
                    }
                }
            }
        }
        ServerConnectionModule.feedback("Interactive mode exit");
    }
}
