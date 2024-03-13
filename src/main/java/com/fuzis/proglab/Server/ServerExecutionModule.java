package com.fuzis.proglab.Server;


import com.fuzis.proglab.Annotations.InteractiveCommand;
import com.fuzis.proglab.AppData;
import com.fuzis.proglab.Enums.Opinion;
import com.fuzis.proglab.Enums.Popularity;
import com.fuzis.proglab.Enums.Sex;
import com.fuzis.proglab.DefaultCartoonPersonCharacter;
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
    public static class FakeScanner {
        private Scanner scan;
        private LinkedList<String> arr;
        private boolean end = false;
        private Consumer<FakeScanner> insert_callback;
        public FakeScanner(Scanner _scan) {
            scan = _scan;
        }

        public FakeScanner(Consumer<FakeScanner> _insert_callback) {
            arr = new LinkedList<>();
            insert_callback = _insert_callback;
        }
        public void close(){end = true;}
        public boolean hasNext()
        {
            if(scan != null)return scan.hasNext();
            return !end;
        }
        public void add(String a)
        {
            arr.add(a);
        }
        public String nextLine() {
            if(scan != null)return scan.nextLine();
            if(end)throw new IllegalStateException("FakeScanner is closed");
            while(arr.isEmpty() && !end)
            {
                insert_callback.accept(this);
            }
            if(end){exit= true; return "";}
            return arr.pop();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FakeScanner that = (FakeScanner) o;
            if(this.scan != null || that.scan != null) {
                if(this.scan == null || that.scan == null) return false;
                return this.scan.equals(that.scan);
            }
            return this.arr.equals(that.arr) && this.end==that.end && this.insert_callback.equals(that.insert_callback);
        }
    }

    public static void warn(String info) {
        logger.warn(info);
    }

    public static void feedback(String info) {
        logger.info(info);
    }

    public static void error(String info) {
        logger.error(info);
    }

    record ScriptData(FakeScanner file, String prev_key) {
    }

    public static HashMap<String, ScriptData> executing_scripts = new HashMap<>();
    public static String cur_script = null;
    public static CharacterCollection char_col;
    public static Boolean exit = false;
    public static Boolean supress_inp_invite = false;
    public static FakeScanner scan;

    public static class Cmds {
        public static class IDCharacter {
            public DefaultCartoonPersonCharacter character;
            public String id;

            public IDCharacter(String id, DefaultCartoonPersonCharacter character) {
                this.id = id;
                this.character = character;
            }
        }

        public void println_supress(Object a) {
            if (supress_inp_invite) return;
            println(a);
        }

        public void print_supress(Object a) {
            if (supress_inp_invite) return;
            print(a);
        }

        public void print(Object a) {
            System.out.print(a);
        }

        public void println(Object a) {
            System.out.println(a);
        }

        public void println() {
            System.out.println();
        }
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

        public String name_interactive() {
            println_supress("Введите имя персонажа");
            String name = null;
            boolean match = false;
            while (!match) {
                name = scan.nextLine();
                if (name.matches("\\s*")) {
                    println_supress("Воспринято как <null>");
                    break;
                }
                match = name.matches("\\s*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*");
                if (!match) {
                    println_supress("Введенное значение не может быть именем, попробуйте ещё раз");
                }
            }
            return name;
        }

        public Sex sex_interactive() {
            println_supress("Введите пол персонажа");
            print_supress("Возможные варианты ввода: ");
            for (int i = 0; i < Sex.values().length; i++) {
                if (i != Sex.values().length - 1) print_supress(Sex.values()[i] + ", ");
                else println_supress(Sex.values()[i]);
            }
            Sex sex = null;
            boolean match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println_supress("Воспринято как <null>");
                    break;
                }
                try {
                    data = data.trim();
                    sex = Sex.valueOf(data.substring(0, 1).toUpperCase() + data.substring(1).toLowerCase(Locale.ROOT));
                    match = true;
                } catch (IllegalArgumentException ex) {
                    println_supress("Введенное значение не может быть полом, попробуйте ещё раз");
                }
            }
            return sex;
        }

        public String quote_interactive() {
            println_supress("Введите цитату персонажа");
            String quote = null;
            boolean match = false;
            while (!match) {
                quote = scan.nextLine();
                if (quote.matches("\\s*")) {
                    println_supress("Воспринято как <null>");
                    break;
                }
                match = quote.matches("(?:\\s*[a-zA-Zа-яА-Яё,Ё_.?\"'`!#@$0-9]+\\s*)+");
                if (!match) {
                    println_supress("Введенное значение не может быть цитатой, попробуйте ещё раз");
                }
            }
            return quote;
        }

        public Double height_interactive() {
            println_supress("Введите рост персонажа");
            Double height = null;
            boolean match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println_supress("Воспринято как <null>");
                    break;
                }
                try {
                    data = data.trim();
                    height = Double.valueOf(data);
                    match = true;
                } catch (NumberFormatException ex) {
                    println_supress("Введенное значение не может быть ростом, попробуйте ещё раз");
                }
            }
            return height;
        }

        public Double weight_interactive() {
            println_supress("Введите вес персонажа");
            Double weight = null;
            boolean match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println_supress("Воспринято как <null>");
                    break;
                }
                try {
                    data = data.trim();
                    weight = Double.valueOf(data);
                    match = true;
                } catch (NumberFormatException ex) {
                    println_supress("Введенное значение не может быть весом, попробуйте ещё раз");
                }
            }
            return weight;
        }

        public Popularity popularity_interactive() {
            println_supress("Введите популярность персонажа");
            print_supress("Возможные варианты ввода: ");
            for (int i = 0; i < Popularity.values().length; i++) {
                if (i != Popularity.values().length - 1) print_supress(Popularity.values()[i] + ", ");
                else println_supress(Popularity.values()[i]);
            }
            Popularity popul = null;
            boolean match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println_supress("Воспринято как <null>");
                    break;
                }
                try {
                    data = data.trim();
                    popul = Popularity.valueOf(data.substring(0, 1).toUpperCase() + data.substring(1).toLowerCase(Locale.ROOT));
                    match = true;
                } catch (IllegalArgumentException ex) {
                    println_supress("Введенное значение не может быть популярностью, попробуйте ещё раз");
                }
            }
            return popul;
        }

        public Double age_interactive() {
            println_supress("Введите возраст персонажа");
            Double age = null;
            boolean match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println_supress("Воспринято как <null>");
                    break;
                }
                try {
                    data = data.trim();
                    age = Double.valueOf(data);
                    match = true;
                } catch (NumberFormatException ex) {
                    println_supress("Введенное значение не может быть возрастом, попробуйте ещё раз");
                }
            }
            return age;
        }

        public String description_interactive() {
            println_supress("Введите краткое описание персонажа (одна строка)");
            String description = null;
            boolean match = false;
            while (!match) {
                description = scan.nextLine();
                if (description.matches("\\s*")) {
                    println_supress("Воспринято как <null>");
                    break;
                }
                match = description.matches("(?:\\s*[\\-a-zA-Zа-яА-ЯёЁ_,.?\"'`!#@$0-9]+\\s*)+");
                if (!match) {
                    println_supress("Введенное значение не может быть описанием, попробуйте ещё раз");
                }
            }
            return description;
        }

        public Integer health_interactive() {
            println_supress("Введите здоровье персонажа (целое число)");
            Integer health = null;
            boolean match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println_supress("Воспринято как <null>");
                    break;
                }
                try {
                    data = data.trim();
                    health = Integer.valueOf(data);
                    match = true;
                } catch (NumberFormatException ex) {
                    println_supress("Введенное значение не может быть здоровьем, попробуйте ещё раз");
                }
            }
            return health;
        }

        public Boolean isAnimeCharacter_interactive() {
            println_supress("Введите является ли данный персонажем аниме  (Да/Нет/Yes/No/はい/いいえ)");
            Boolean isAnimeCharacter = null;
            boolean match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println_supress("Воспринято как <null>");
                    break;
                }
                match = data.toLowerCase(Locale.ROOT).matches("\\s*(?:да|yes|はい)\\s*");
                if (match) {
                    isAnimeCharacter = true;
                    break;
                }
                match = data.toLowerCase(Locale.ROOT).matches("\\s*(?:нет|no|いいえ)\\s*");
                if (match) isAnimeCharacter = false;
                if (!match) {
                    println_supress("Введенное значение не входит в Да/Нет/Yes/No/はい/いいえ, попробуйте ещё раз");
                }
            }
            return isAnimeCharacter;
        }

        public List<String> additionalnames_interactive() {
            println_supress("Введите дополнительные имена персонажа в строке через запятую");
            ArrayList<String> additionalNames = null;
            boolean match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println_supress("Воспринято как <null>");
                    break;
                }
                match = data.matches("(?:\\s*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*,\\s*)*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*");
                if (!match) {
                    println_supress("Введенное значение не не соответствует формату ввода, попробуйте ещё раз");
                    continue;
                }
                additionalNames = new ArrayList<>(Arrays.stream(data.trim().split("\\s*,\\s*")).toList());
            }
            return additionalNames;
        }

        public HashMap<String, Opinion> opinions_interactive() {
            println_supress("Введите мнения персонажа о других персонажах(не обязательно находящихся в коллекции), в формате: <имя>:<отношение>,<имя2>:<отношение2>...");
            print_supress("Возможные варианты ввода <отношение>: ");
            for (int i = 0; i < Opinion.values().length; i++) {
                if (i != Opinion.values().length - 1) print_supress(Opinion.values()[i] + ", ");
                else println_supress(Opinion.values()[i]);
            }
            HashMap<String, Opinion> opinions = null;
            boolean match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println_supress("Воспринято как <null>");
                    break;
                }
                match = data.matches("(?:\\s*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*:\\s*[a-zA-Z]+\\s*,\\s*)*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*:\\s*[a-zA-Z]+\\s*");
                if (!match) {
                    println_supress("Введенное значение не не соответствует формату ввода, попробуйте ещё раз");
                    continue;
                }
                var pre_data = data.trim().split("\\s*,\\s*");
                opinions = new HashMap<>();
                try {
                    for (var el : pre_data) {
                        var separated = el.split("\\s*:\\s*");
                        opinions.put(separated[0], Opinion.valueOf(separated[1].substring(0, 1).toUpperCase() + separated[1].substring(1).toLowerCase(Locale.ROOT)));
                    }
                } catch (IllegalArgumentException ex) {
                    println_supress("Введенное значение не не соответствует формату ввода (неверное значение отношения), попробуйте ещё раз");
                    match = false;
                }
            }
            return opinions;
        }

        public Cmds.IDCharacter add_interactive() {
            println_supress("Введите id персонажа");
            String id = null;
            Boolean match = false;
            while (!match) {
                id = scan.nextLine();
                match = id.matches("\\s*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*");
                if (!match) {
                    println_supress("Введенное значение не может быть id, попробуйте ещё раз");
                    continue;
                }
                match = !char_col.getCharacters().containsKey(id);
                if (!match) {
                    println_supress("Объект с данным id уже существует, попробуйте использовать update");
                }
            }
            String name = name_interactive();
            Sex sex = sex_interactive();
            String quote = quote_interactive();
            Double height = height_interactive();
            Double weight = weight_interactive();
            Popularity popul = popularity_interactive();
            String description = description_interactive();
            Double age = age_interactive();
            Integer health = health_interactive();
            Boolean isAnimeCharacter = isAnimeCharacter_interactive();
            List<String> additionalNames = additionalnames_interactive();
            HashMap<String, Opinion> opinions = opinions_interactive();
            return new Cmds.IDCharacter(id, new DefaultCartoonPersonCharacter(name, sex, quote, opinions, additionalNames, height, weight, isAnimeCharacter, popul, description, age, health));
        }

        @InteractiveCommand(args = {0}, usage = {"add - добавление персонажа в коллекцию, значение вводится построчно:", "<id> - строка-индификатор", "<name> - имя", "<sex> пол персонажа, элемент из перечисления Sex", "<quote> - строка, цитата персонажа", "<height> - рост персонажа", "<weight> - вес персонажа", "<popularity> - популярность персонажа, элемент из перечисления Popularity", "<description> - строка, описание персонажа", "<age> - возраст персонажа", "<health> - значение здоровья персонажа в целочисленных условных единицах", "<isAnimeCharacter> - является ли アニメ персонажем, значение Yes/No", "<additionalNames> - строка, дополнительные имена персонажа, перечисление через запятую", "<opinions> - мнения о других персонажах (не обязательно из коллекции) в виде <имя>:<отношение>, <имя2>:<отношение2>... отношение - значение из перечисления Opinion"}, help = "Производит добавления элемента в коллекцию")
        public void add(List<String> argc) {
            var new_charac = add_interactive();
            char_col.add(new_charac.id, new_charac.character);
            feedback("Successful add");
        }

        @InteractiveCommand(args = {1}, usage = {"update <id>- изменение персонажа, содержащегося в коллекции, можно изменять конкретные поля, в остальных останутся предыдущие значения, end - для выхода", "<name> - имя", "<sex> пол персонажа, элемент из перечисления Sex", "<quote> - строка, цитата персонажа", "<height> - рост персонажа", "<weight> - вес персонажа", "<popularity> - популярность персонажа, элемент из перечисления Popularity", "<description> - строка, описание персонажа", "<age> - возраст персонажа", "<health> - значение здоровья персонажа в целочисленных условных единицах", "<isAnimeCharacter> - является ли アニメ персонажем, значение Yes/No", "<additionalNames> - строка, дополнительные имена персонажа, перечисление через запятую", "<opinions> - мнения о других персонажах (не обязательно из коллекции) в виде <имя>:<отношение>, <имя2>:<отношение2>... отношение - значение из перечисления Opinion"}, help = "Изменяет элемент в коллекции")
        public void update(List<String> argc) {
            String id = argc.get(0);
            var charac = char_col.getCharacter(id);
            if (charac == null) {
                error("Character not found");
                return;
            }
            boolean is_ended = false;
            while (!is_ended) {
                println_supress("Введите какое поле вы желаете изменить, иначе End для выхода");
                println_supress("Варианты ввода: Name, Sex, Quote, Opinions, AdditionalNames, Height, Weight, Age, Health, IsAnimeCharacter, Popularity, Description");
                String str = scan.nextLine().trim().toLowerCase();
                switch (str) {
                    case "name" -> charac.setName(name_interactive());
                    case "sex" -> charac.setSex(sex_interactive());
                    case "quote" -> charac.setQuote(quote_interactive());
                    case "opinions" -> charac.setOpinions(opinions_interactive());
                    case "additionalnames" -> charac.setAdditionalNames(additionalnames_interactive());
                    case "height" -> charac.setHeight(height_interactive());
                    case "weight" -> charac.setWeight(weight_interactive());
                    case "age" -> charac.setAge(age_interactive());
                    case "health" -> charac.setHealth(health_interactive());
                    case "isanimecharacter" -> charac.setAnimeCharacter(isAnimeCharacter_interactive());
                    case "popularity" -> charac.setPopularity(popularity_interactive());
                    case "description" -> charac.setDescription(description_interactive());
                    case "end" -> {
                        feedback("End update");
                        return;
                    }
                    default -> error("Field not found!!!");
                }
            }
        }

        @InteractiveCommand(args = {0}, usage = {"save - сохранить коллекцию в ранее указанный файл"}, help = "Сохраняет коллекцию")
        public void save(List<String> argc) {
            char_col.save();
        }

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
            if (null == char_col.deleteCharacter(argc.get(0))) feedback("key not found");
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
                scan = new FakeScanner(new Scanner(f));
                feedback("Start executing script: " + argc.get(0));
                supress_inp_invite = true;
                executing_scripts.put(argc.get(0), new ScriptData(scan, cur_script));
                cur_script = argc.get(0);
            } catch (FileNotFoundException ex) {
                error("File not found or unable to read");
            }
        }
    }
    public static void msg_handle(AppData.TransferData msg)
    {
        ServerExecutionModule.feedback("Got message: " + msg.body());
    }
    public static void command_handle(AppData.TransferData msg){
        switch(msg.code())
        {
            case 3:
                var map = new HashMap<String, AppData.InteractiveCommandData>();
                for(var el : Cmds.class.getMethods())
                {
                    var annot = el.getAnnotation(InteractiveCommand.class);
                    if(annot == null)continue;
                    map.put(el.getName(),new AppData.InteractiveCommandData(annot.args(), annot.usage(), annot.help()));
                }
                ServerWritingModule.send(new AppData.TransferData(AppData.TransferPurpose.Return,
                        "",3,map));
                break;
        }
    }
    public static void start_interactive() {
        exit = false;
        char_col = CharacterCollection.getInstance();
        Cmds cmd_class = new Cmds();
        feedback("Interactive mode server started");
        var in_scan = new FakeScanner((a)->{
            var res = ServerReadingModule.getNextInteractive();
            if(res == null)a.close();
            a.add(res);
        });
        supress_inp_invite = false;
        while (scan == null || !scan.equals(in_scan)) {
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
                while(!scan.hasNext() && !exit)
                {
                    var prev =  executing_scripts.get(cur_script).prev_key;
                    if(prev != null) {
                        scan = executing_scripts.get(prev).file;
                        executing_scripts.remove(cur_script);
                        cur_script = prev;
                    }
                    else
                    {
                        executing_scripts.remove(cur_script);
                        cur_script = null;
                        feedback("End script execution");
                        break;
                    }
                }
            }
        }
        feedback("Interactive mode exit");
    }
}
