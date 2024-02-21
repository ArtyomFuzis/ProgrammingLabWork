package com.fuzis.lab5;

import com.fuzis.lab5.Enums.Opinion;
import com.fuzis.lab5.Enums.Popularity;
import com.fuzis.lab5.Enums.Sex;
import com.fuzis.lab5.GeneralObjects.DefaultCartoonPersonCharacter;
import com.fuzis.lab5.annotations.InteractiveCommand;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Самый длинный на момент версии Lab5 класс, сборище ужаснейшего boilerplate и spaghetti кода, что порождает интерактивный режим, первый нормально функциональный режим этой программы, но из-за защиты от пользователей данный класс представляет собой по-настоящему кошмар.
 * <br>
 * Содержит функции для взаимодействия с пользователем через консольный интерфейс: вывод предупреждений, нефатальных ошибок, общей информации, содержит вложенный класс Cmds, содержащий функции, обрабатывающие команды введенные пользователем, а также команду start, которая запускает выполнение интерактивного режима
 * <br>
 * <span color="red">Флаги класса:<ol><li><code>supress_inp_invite</code> регулирует дополнительные уточнения и ошибки во время ввода объектов</li>
 * <li><code>exit</code> показывает что необходимо завершать интерактивный режим, как только закончится выполнение введенный команды</li>
 *
 * </ol></span>
 * <br>
 * <img width="500" src="https://i.pinimg.com/originals/10/64/5b/10645b55bb90177e672712362abbc1cb.jpg">
 */
public class Interactive {
    /**
     * Выводит строку как предупреждение, по умолчанию это "[warn] " +{@literal <info>}
     * @param info строка для вывода
     */
    public static void warn(String info) {
        System.out.println("[warn] " + info);
    }

    /**
     * Выводит строку как просто сообщение обратной связи, по умолчанию это "[info] " +{@literal <info>}
     * @param info строка для вывода
     */
    public static void feedback(String info) {
        System.out.println("[info] " + info);
    }
    /**
     * Выводит строку как просто сообщение об не фатальной ошибке, по умолчанию это "[error] " +{@literal <info>}
     * @param info строка для вывода
     */
    public static void error(String info) {
        System.out.println("[error] " + info);
    }

    /**
     * Сканер, что видит всё сущее и читает из нужного нам потока, сначала это System.in, но может быть заменен на файл или что-то вроде того
     */
    public static Scanner scan;
    /**
     * Наша коллекция, хотя это и так синглтон, так что данное поле нужно скорее для оптимизации читабельности и производительности кода (*только в моем понимании, данное утверждение может быть никак не связанным с реальным миром и работать только для (против) меня :) )
     */
    public static CharacterCollection char_col;
    public static Boolean exit = false;
    public static Boolean supress_inp_invite = false;
    /**
     * Нужно для команды history, при добавлении проверяется кол-во элементов и при необходимости (>14) лишние убираются, по умолчанию реализуется через LinkedList
     */
    public static Queue<String> history = new LinkedList<>();

    /**
     *Был ад, а стала столица ада... Ну что ж, здесь содержатся <s>ненормальные</s> функции, которые вызываются, когда пользователь вызывают соответсвующую их названию команду. Все функции одного шаблона:
     * <code> @InteractiveCommand(args = {...}, usage = {...}, help = "...")
     * <br>public void ...(List{@literal <String>} argc)</code>
     * <br>
     * А также здесь можно встретить класс IDCharacter и рекорд SSPair. Это служебные классы для представления пары персонаж + идентификатор или идентификатор + объект для удобного прохода по элементам/сортировки.
     *А также есть функции println и print, которые работает как System.out.println() и System.out.print(), если supress_inp_invite=false,
     * <br>
     * <div><img width="488" src="https://images.wallpapersden.com/image/download/kurumi-tokisaki-date-a-live_a2xrammUmZqaraWkpJRmZ21lrWZlZ2k.jpg"></div>
     * <img width="488" src="https://img.qoo-img.com/note/202103/14/1ll3BZE222BNjbwBgryjveC3.jpeg">
     */
    public static class Cmds {
        /**
         * Класс для хранения пары ID + персонаж, поля не final и pubic
         */
        public static class IDCharacter {

            public DefaultCartoonPersonCharacter character;
            public String id;

            public IDCharacter(String id, DefaultCartoonPersonCharacter character) {
                this.id = id;
                this.character = character;
            }
        }

        /**
         * System.out.println(a) if (supress_inp_invite)
         * @param a что вывести
         */
        public void println(Object a) {
            if (supress_inp_invite) return;
            System.out.println(a);
        }
        /**
         * System.out.print(a) if (supress_inp_invite)
         * @param a что вывести
         */
        public void print(Object a) {
            if (supress_inp_invite) return;
            System.out.print(a);
        }

        /**
         * Команда помощи, выводит всю информацию об использовании указанную в аннотации InteractiveCommand над функцией-командой при вызове без аргументов и usage конкретной функции если help {@literal <command>}
         */
        @InteractiveCommand(args = {0, 1}, usage = {"help - вывод справки по всем командам", "help <команда> - вывод справки по определенной команде"}, help = "Выводит справку по командам интерактивного режима")
        public void help(List<String> argc) {
            if (argc.isEmpty()) {
                System.out.println("Все команды, доступные в интерактивном режиме:");
                System.out.println();
                for (var el : Cmds.class.getMethods()) {
                    var anot = el.getAnnotation(InteractiveCommand.class);
                    if (anot == null) continue;
                    System.out.println("-----------Команда-----------");
                    System.out.println(el.getName() + ": " + anot.help());
                    System.out.println("--------Использование--------");
                    for (var el2 : anot.usage()) System.out.println(el2);
                    System.out.println();
                }
            } else {
                try {
                    var el = Cmds.class.getMethod(argc.get(0), List.class);
                    var anot = el.getAnnotation(InteractiveCommand.class);
                    System.out.println("--------Использование--------");
                    for (var el2 : anot.usage()) System.out.println(el2);
                } catch (NoSuchMethodException ex) {
                    error("No such command");
                }
            }
        }

        /**
         *Выводит все элементы коллекции построчно с id через toString, если элементов нет, то feedback "It is empty"
         */
        @InteractiveCommand(args = {0}, usage = {"show - вывод всех элементов коллекции"}, help = "Выводит элементы коллекции")
        public void show(List<String> argc) {
            if (CharacterCollection.getInstance().getCharacters().isEmpty()) feedback("It is empty");
            for (var el : CharacterCollection.getInstance().getCharacters().keySet()) {
                System.out.println(el + ": " + CharacterCollection.getInstance().getCharacters().get(el));
            }
        }

        //Я честно не хотел творить так много практически дублирующегося кода, но обобщить это просто невозможно (-_-)

        /**
         * А можно это не комментить и никому не смотреть что там внутри? Там одинаковых 12 кусков кода, которые я долго пытался обобщить, но ничего не вышло... (~_~) Там интерактивчик с пользователем и его реплей если несоответствие regex, пустая строка = null. А если что-то непредвиденное случится, то это схэнделит start (например конец файла скрипта). Если supress_inp_invite=true, то подсказки для ввода не вводятся(активируется при выполнении скриптов)
         * @return нового персонажа, сформированного после данного "интерактивчика"
         */
        public IDCharacter add_interactive() {
            println("Введите id персонажа");
            String id = null;
            Boolean match = false;
            while (!match) {
                id = scan.nextLine();
                match = id.matches("\\s*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*");
                if (!match) {
                    println("Введенное значение не может быть id, попробуйте ещё раз");
                    continue;
                }
                match = !CharacterCollection.getInstance().getCharacters().containsKey(id);
                if (!match) {
                    println("Объект с данным id уже существует, попробуйте использовать update");
                }
            }
            println("Введите имя персонажа");
            String name = null;
            match = false;
            while (!match) {
                name = scan.nextLine();
                if (name.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                match = name.matches("\\s*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*");
                if (!match) {
                    println("Введенное значение не может быть именем, попробуйте ещё раз");
                }
            }
            println("Введите пол персонажа");
            print("Возможные варианты ввода: ");
            for (int i = 0; i < Sex.values().length; i++) {
                if (i != Sex.values().length - 1) print(Sex.values()[i] + ", ");
                else println(Sex.values()[i]);
            }
            Sex sex = null;
            match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                try {
                    data = data.trim();
                    sex = Sex.valueOf(data.substring(0, 1).toUpperCase() + data.substring(1).toLowerCase(Locale.ROOT));
                    match = true;
                } catch (IllegalArgumentException ex) {
                    println("Введенное значение не может быть полом, попробуйте ещё раз");
                }
            }
            println("Введите цитату персонажа");
            String quote = null;
            match = false;
            while (!match) {
                quote = scan.nextLine();
                if (quote.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                match = quote.matches("(?:\\s*[a-zA-Zа-яА-Яё,Ё_.?\"'`!#@$0-9]+\\s*)+");
                if (!match) {
                    println("Введенное значение не может быть цитатой, попробуйте ещё раз");
                }
            }
            println("Введите рост персонажа");
            Double height = null;
            match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                try {
                    data = data.trim();
                    height = Double.valueOf(data);
                    match = true;
                } catch (NumberFormatException ex) {
                    println("Введенное значение не может быть ростом, попробуйте ещё раз");
                }
            }
            println("Введите вес персонажа");
            Double weight = null;
            match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                try {
                    data = data.trim();
                    weight = Double.valueOf(data);
                    match = true;
                } catch (NumberFormatException ex) {
                    println("Введенное значение не может быть весом, попробуйте ещё раз");
                }
            }
            println("Введите популярность персонажа");
            print("Возможные варианты ввода: ");
            for (int i = 0; i < Popularity.values().length; i++) {
                if (i != Popularity.values().length - 1) print(Popularity.values()[i] + ", ");
                else println(Popularity.values()[i]);
            }
            Popularity popul = null;
            match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                try {
                    data = data.trim();
                    popul = Popularity.valueOf(data.substring(0, 1).toUpperCase() + data.substring(1).toLowerCase(Locale.ROOT));
                    match = true;
                } catch (IllegalArgumentException ex) {
                    println("Введенное значение не может быть популярностью, попробуйте ещё раз");
                }
            }
            println("Введите краткое описание персонажа (одна строка)");
            String description = null;
            match = false;
            while (!match) {
                description = scan.nextLine();
                if (description.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                match = description.matches("(?:\\s*[\\-a-zA-Zа-яА-ЯёЁ_,.?\"'`!#@$0-9]+\\s*)+");
                if (!match) {
                    println("Введенное значение не может быть описанием, попробуйте ещё раз");
                }
            }
            println("Введите возраст персонажа");
            Double age = null;
            match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                try {
                    data = data.trim();
                    age = Double.valueOf(data);
                    match = true;
                } catch (NumberFormatException ex) {
                    println("Введенное значение не может быть возрастом, попробуйте ещё раз");
                }
            }
            println("Введите здоровье персонажа (целое число)");
            Integer health = null;
            match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                try {
                    data = data.trim();
                    health = Integer.valueOf(data);
                    match = true;
                } catch (NumberFormatException ex) {
                    println("Введенное значение не может быть здоровьем, попробуйте ещё раз");
                }
            }
            println("Введите является ли данный персонажем аниме  (Да/Нет/Yes/No/はい/いいえ)");
            Boolean isAnimeCharacter = null;
            match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println("Воспринято как <null>");
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
                    println("Введенное значение не входит в Да/Нет/Yes/No/はい/いいえ, попробуйте ещё раз");
                }
            }
            println("Введите дополнительные имена персонажа в строке через запятую");
            ArrayList<String> additionalNames = null;
            match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                match = data.matches("(?:\\s*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*,\\s*)*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*");
                if (!match) {
                    println("Введенное значение не не соответствует формату ввода, попробуйте ещё раз");
                    continue;
                }
                additionalNames = new ArrayList<>(Arrays.stream(data.trim().split("\\s*,\\s*")).toList());
            }
            println("Введите мнения персонажа о других персонажах(не обязательно находящихся в коллекции), в формате: <имя>:<отношение>,<имя2>:<отношение2>...");
            print("Возможные варианты ввода <отношение>: ");
            for (int i = 0; i < Opinion.values().length; i++) {
                if (i != Opinion.values().length - 1) print(Opinion.values()[i] + ", ");
                else println(Opinion.values()[i]);
            }
            HashMap<String, Opinion> opinions = null;
            match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                match = data.matches("(?:\\s*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*:\\s*[a-zA-Z]+\\s*,\\s*)*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*:\\s*[a-zA-Z]+\\s*");
                if (!match) {
                    println("Введенное значение не не соответствует формату ввода, попробуйте ещё раз");
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
                    println("Введенное значение не не соответствует формату ввода (неверное значение отношения), попробуйте ещё раз");
                    match = false;
                }
            }
            return new IDCharacter(id, new DefaultCartoonPersonCharacter(name, sex, quote, opinions, additionalNames, height, weight, isAnimeCharacter, popul, description, age, health));
        }

        /**
         * Получает нового персонажа из <code>add_interactive()</code> и добавляет его в коллекцию, если операция успешна feedback "Successful add"
         *
         */
        @InteractiveCommand(args = {0}, usage = {"add - добавление персонажа в коллекцию, значение вводится построчно:", "<id> - строка-индификатор", "<name> - имя", "<sex> пол персонажа, элемент из перечисления Sex", "<quote> - строка, цитата персонажа", "<height> - рост персонажа", "<weight> - вес персонажа", "<popularity> - популярность персонажа, элемент из перечисления Popularity", "<description> - строка, описание персонажа", "<age> - возраст персонажа", "<health> - значение здоровья персонажа в целочисленных условных единицах", "<isAnimeCharacter> - является ли アニメ персонажем, значение Yes/No", "<additionalNames> - строка, дополнительные имена персонажа, перечисление через запятую", "<opinions> - мнения о других персонажах (не обязательно из коллекции) в виде <имя>:<отношение>, <имя2>:<отношение2>... отношение - значение из перечисления Opinion"}, help = "Производит добавления элемента в коллекцию")
        public void add(List<String> argc) {
            var new_charac = add_interactive();
            CharacterCollection.getInstance().add(new_charac.id, new_charac.character);
            feedback("Successful add");
        }

        /**
         * Обновление элемента, то есть просто удаляет прошлый элемент по указанному id и меняет его на новый, если необходимо сохранить прошлое значение поля - значение {@literal "<def>"}. Действует практически так же, как и add, но, к сожалению реализован в полном формате (около 300 строк)
         */
        @InteractiveCommand(args = {1}, usage = {"update <id>- изменение персонажа, содержащегося в коллекции, значение изменений вводится построчно(строка \"<def>\" - для сохранения прошлых значений):", "<name> - имя", "<sex> пол персонажа, элемент из перечисления Sex", "<quote> - строка, цитата персонажа", "<height> - рост персонажа", "<weight> - вес персонажа", "<popularity> - популярность персонажа, элемент из перечисления Popularity", "<description> - строка, описание персонажа", "<age> - возраст персонажа", "<health> - значение здоровья персонажа в целочисленных условных единицах", "<isAnimeCharacter> - является ли アニメ персонажем, значение Yes/No", "<additionalNames> - строка, дополнительные имена персонажа, перечисление через запятую", "<opinions> - мнения о других персонажах (не обязательно из коллекции) в виде <имя>:<отношение>, <имя2>:<отношение2>... отношение - значение из перечисления Opinion"}, help = "Изменяет элемент в коллекции")
        public void update(List<String> argc) {
            String id = argc.get(0);
            var charac = CharacterCollection.getInstance().getCharacter(id);
            if (charac == null) {
                error("Character not found");
                return;
            }
            CharacterCollection.getInstance().deleteCharacter(id);
            Boolean match = false;
            println("Введите имя персонажа");
            String name = null;
            match = false;
            while (!match) {
                name = scan.nextLine();
                if (name.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                if (name.matches("\\s*<def>\\s*")) {
                    println("Воспринято как предыдущее значение");
                    name = charac.getName();
                    break;
                }
                match = name.matches("\\s*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*");
                if (!match) {
                    println("Введенное значение не может быть именем, попробуйте ещё раз");
                }
            }
            println("Введите пол персонажа");
            print("Возможные варианты ввода: ");
            for (int i = 0; i < Sex.values().length; i++) {
                if (i != Sex.values().length - 1) print(Sex.values()[i] + ", ");
                else println(Sex.values()[i]);
            }
            Sex sex = null;
            match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                if (data.matches("\\s*<def>\\s*")) {
                    println("Воспринято как предыдущее значение");
                    sex = charac.getSex();
                    break;
                }
                try {
                    data = data.trim();
                    sex = Sex.valueOf(data.substring(0, 1).toUpperCase() + data.substring(1).toLowerCase(Locale.ROOT));
                    match = true;
                } catch (IllegalArgumentException ex) {
                    println("Введенное значение не может быть полом, попробуйте ещё раз");
                }
            }
            println("Введите цитату персонажа");
            String quote = null;
            match = false;
            while (!match) {
                quote = scan.nextLine();
                if (quote.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                if (quote.matches("\\s*<def>\\s*")) {
                    println("Воспринято как предыдущее значение");
                    quote = charac.getQuote();
                    break;
                }
                match = quote.matches("(?:\\s*[a-zA-Zа-яА-Яё,Ё_.?\"'`!#@$0-9]+\\s*)+");
                if (!match) {
                    println("Введенное значение не может быть цитатой, попробуйте ещё раз");
                }
            }
            println("Введите рост персонажа");
            Double height = null;
            match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                if (data.matches("\\s*<def>\\s*")) {
                    println("Воспринято как предыдущее значение");
                    height = charac.getHeight();
                    break;
                }
                try {
                    data = data.trim();
                    height = Double.valueOf(data);
                    match = true;
                } catch (NumberFormatException ex) {
                    println("Введенное значение не может быть ростом, попробуйте ещё раз");
                }
            }
            println("Введите вес персонажа");
            Double weight = null;
            match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                if (data.matches("\\s*<def>\\s*")) {
                    println("Воспринято как предыдущее значение");
                    weight = charac.getWeight();
                    break;
                }
                try {
                    data = data.trim();
                    weight = Double.valueOf(data);
                    match = true;
                } catch (NumberFormatException ex) {
                    println("Введенное значение не может быть весом, попробуйте ещё раз");
                }
            }
            println("Введите популярность персонажа");
            print("Возможные варианты ввода: ");
            for (int i = 0; i < Popularity.values().length; i++) {
                if (i != Popularity.values().length - 1) print(Popularity.values()[i] + ", ");
                else println(Popularity.values()[i]);
            }
            Popularity popul = null;
            match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                if (data.matches("\\s*<def>\\s*")) {
                    println("Воспринято как предыдущее значение");
                    popul = charac.getPopularity();
                    break;
                }
                try {
                    data = data.trim();
                    popul = Popularity.valueOf(data.substring(0, 1).toUpperCase() + data.substring(1).toLowerCase(Locale.ROOT));
                    match = true;
                } catch (IllegalArgumentException ex) {
                    println("Введенное значение не может быть популярностью, попробуйте ещё раз");
                }
            }
            println("Введите краткое описание персонажа (одна строка)");
            String description = null;
            match = false;
            while (!match) {
                description = scan.nextLine();
                if (description.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                if (description.matches("\\s*<def>\\s*")) {
                    println("Воспринято как предыдущее значение");
                    description = charac.getDescription();
                    break;
                }
                match = description.matches("(?:\\s*[\\-a-zA-Zа-яА-ЯёЁ_,.?\"'`!#@$0-9]+\\s*)+");
                if (!match) {
                    println("Введенное значение не может быть описанием, попробуйте ещё раз");
                }
            }
            println("Введите возраст персонажа");
            Double age = null;
            match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                if (data.matches("\\s*<def>\\s*")) {
                    println("Воспринято как предыдущее значение");
                    age = charac.getAge();
                    break;
                }
                try {
                    data = data.trim();
                    age = Double.valueOf(data);
                    match = true;
                } catch (NumberFormatException ex) {
                    println("Введенное значение не может быть возрастом, попробуйте ещё раз");
                }
            }
            println("Введите здоровье персонажа (целое число)");
            Integer health = null;
            match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                if (data.matches("\\s*<def>\\s*")) {
                    println("Воспринято как предыдущее значение");
                    health = charac.getHealth();
                    break;
                }
                try {
                    data = data.trim();
                    health = Integer.valueOf(data);
                    match = true;
                } catch (NumberFormatException ex) {
                    println("Введенное значение не может быть здоровьем, попробуйте ещё раз");
                }
            }
            println("Введите является ли данный персонажем аниме  (Да/Нет/Yes/No/はい/いいえ)");
            Boolean isAnimeCharacter = null;
            match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                if (data.matches("\\s*<def>\\s*")) {
                    println("Воспринято как предыдущее значение");
                    isAnimeCharacter = charac.getAnimeCharacter();
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
                    println("Введенное значение не входит в Да/Нет/Yes/No/はい/いいえ, попробуйте ещё раз");
                }
            }
            println("Введите дополнительные имена персонажа в строке через запятую");
            List<String> additionalNames = null;
            match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                if (data.matches("\\s*<def>\\s*")) {
                    println("Воспринято как предыдущее значение");
                    additionalNames = charac.getAdditionalNames();
                    break;
                }
                match = data.matches("(?:\\s*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*,\\s*)*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*");
                if (!match) {
                    println("Введенное значение не не соответствует формату ввода, попробуйте ещё раз");
                    continue;
                }
                additionalNames = new ArrayList<>(Arrays.stream(data.trim().split("\\s*,\\s*")).toList());
            }
            println("Введите мнения персонажа о других персонажах(не обязательно находящихся в коллекции), в формате: <имя>:<отношение>,<имя2>:<отношение2>...");
            print("Возможные варианты ввода <отношение>: ");
            for (int i = 0; i < Opinion.values().length; i++) {
                if (i != Opinion.values().length - 1) print(Opinion.values()[i] + ", ");
                else println(Opinion.values()[i]);
            }
            Map<String, Opinion> opinions = null;
            match = false;
            while (!match) {
                String data = scan.nextLine();
                if (data.matches("\\s*")) {
                    println("Воспринято как <null>");
                    break;
                }
                if (data.matches("\\s*<def>\\s*")) {
                    println("Воспринято как предыдущее значение");
                    opinions = charac.getOpinions();
                    break;
                }
                match = data.matches("(?:\\s*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*:\\s*[a-zA-Z]+\\s*,\\s*)*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*:\\s*[a-zA-Z]+\\s*");
                if (!match) {
                    println("Введенное значение не не соответствует формату ввода, попробуйте ещё раз");
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
                    println("Введенное значение не не соответствует формату ввода (неверное значение отношения), попробуйте ещё раз");
                    match = false;
                }
            }
            var new_charac = new DefaultCartoonPersonCharacter(name, sex, quote, opinions, additionalNames, height, weight, isAnimeCharacter, popul, description, age, health);
            CharacterCollection.getInstance().add(id, new_charac);
            feedback("Successful update");
        }

        /**
         * Сохраняет коллекцию в указанный в fileName (в CharacterCollection) файл
         */
        @InteractiveCommand(args = {0}, usage = {"save - сохранить коллекцию в ранее указанный файл"}, help = "Сохраняет коллекцию")
        public void save(List<String> argc) {
            char_col.save();
        }

        /**
         * Завершает интерактивный режим
         */
        @InteractiveCommand(args = {0}, usage = {"exit - завершить работу интерактивного режима"}, help = "Осуществляет выход из программы/подпрограммы")
        public void exit(List<String> argc) {
            exit = true;
            feedback("Exiting...");
        }

        /**
         * Полностью очищает коллекцию <s>этично ли очищать персонажей мультиков?</s>
         */
        @InteractiveCommand(args = {0}, usage = {"clear - полная очистка коллекции"}, help = "Осуществляет очистку коллекции")
        public void clear(List<String> argc) {
            CharacterCollection.getInstance().clear();
            feedback("Successful clearing");
        }

        /**
         * Удаляет элемент с указанным id
         */
        @InteractiveCommand(args = {1}, usage = {"remove_by_id <id> - удалить элемент с указанным id"}, help = "Удаляет указанный элемент")
        public void remove_by_id(List<String> argc) {
            CharacterCollection.getInstance().deleteCharacter(argc.get(0));
            feedback("Successful remove");
        }

        /**
         * Выводит историю выполнения (последние 14 успешно выполненных команд), если их нет - то feedback "History is empty"
         */
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

        /**
         * Добавляет новый элемент если он больше любого другого в коллекции (те самые круги ада с add_interactive)
         */
        @InteractiveCommand(args = {0}, usage = {"add_if_max - добавляет новый элемент если он больше любого другого в коллекции, формат ввода объекта, такой же как и у add"}, help = "Добавляет элемент если он максимальный")
        public void add_if_max(List<String> argc) {
            var new_charac = add_interactive();
            if (char_col.getCharacters().values().stream().allMatch(x -> x.compareTo(new_charac.character) <= 0)) {
                CharacterCollection.getInstance().add(new_charac.id, new_charac.character);
                feedback("Successful add");
            } else {
                feedback("It is lower than something -> not added");
            }
        }
        /**
         * Добавляет новый элемент если он меньше любого другого в коллекции (те самые круги ада с add_interactive)
         */
        @InteractiveCommand(args = {0}, usage = {"add_if_min - добавляет новый элемент если он меньше любого другого в коллекции, формат ввода объекта, такой же как и у add"}, help = "Добавляет элемент если он минимальный")
        public void add_if_min(List<String> argc) {
            var new_charac = add_interactive();
            if (char_col.getCharacters().values().stream().allMatch(x -> x.compareTo(new_charac.character) >= 0)) {
                CharacterCollection.getInstance().add(new_charac.id, new_charac.character);
                feedback("Successful add");
            } else {
                feedback("It is bigger than something -> not added");
            }
        }

        /**
         * Выводит какой-то объект из коллекции, значение поля age которого - минимально, если коллекция пуста - feedback "Collection is empty"
         */
        @InteractiveCommand(args = {0}, usage = {"min_by_age - выводит какой-то объект из коллекции, значение поля age которого - минимально"}, help = "Выводит персонажа, минимального по возрасту")
        public void min_by_age(List<String> argc) {
            if (char_col.size() == 0) {
                feedback("Collection is empty");
                return;
            }
            ArrayList<IDCharacter> id_character_arr = new ArrayList<>();
            for (var el : char_col.getCharacters().keySet()) {
                id_character_arr.add(new IDCharacter(el, char_col.getCharacter(el)));
            }
            var res = id_character_arr.stream().min((o1, o2) -> {
                if (Objects.equals(o1.character.getAge(), o2.character.getAge())) return 0;
                if (o1.character.getAge() == null) return 1;
                if (o2.character.getAge() == null) return -1;
                if (o1.character.getAge() > o2.character.getAge()) return 1;
                return -1;
            }).get();
            System.out.println(res.id + ": " + res.character);
        }

        /**
         * Рекорд для хранения пары строка-идентификатор + объект, удобно для сортировки с сохранением ключей
         */
        public static record SSPair(String one, Object two) {

        }

        /**
         * Выводит имена элементов из коллекции, отсортированные по убыванию
         */
        @InteractiveCommand(args = {0}, usage = {"print_field_descending_name - выводит имена элементов из коллекции, отсортированные по убыванию"}, help = "Выводит имена персонажей, в порядке убывания")
        public void print_field_descending_name(List<String> argc) {
            ArrayList<SSPair> names = new ArrayList<>();
            for (var el : char_col.getCharacters().keySet()) {
                names.add(new SSPair(el, char_col.getCharacter(el).getName()));
            }
            names.sort(((o1, o2) -> -((String) o1.two).compareTo((String) o2.two)));
            for (var el : names) {
                System.out.println(el.one + ": " + el.two);
            }
        }

        /**
         * Выводит здоровье элементов из коллекции, отсортированное по возрастанию
         */
        @InteractiveCommand(args = {0}, usage = {"print_field_ascending_health - выводит здоровье элементов из коллекции, отсортированное по возрастанию"}, help = "Выводит здоровье персонажей, в порядке возрастания")
        public void print_field_ascending_health(List<String> argc) {
            ArrayList<SSPair> ages = new ArrayList<>();
            for (var el : char_col.getCharacters().keySet()) {
                ages.add(new SSPair(el, char_col.getCharacter(el).getHealth()));
            }
            ages.sort(((o1, o2) -> {
                if (o1.two == o2.two && o1.two == null) return 0;
                if (o1.two == null) return 1;
                if (o2.two == null) return -1;
                return ((Integer) o1.two).compareTo((Integer) o2.two);
            }));
            for (var el : ages) {
                System.out.println(el.one + ": " + el.two);
            }
        }

        /**
         * Выводит getInfo коллекции
         */
        @InteractiveCommand(args = {0}, usage = {"info - выводит некоторую информацию о коллекции"}, help = "Выводит информацию о коллекции персонажей")
        public void info(List<String> argc) {
            System.out.println(char_col.getInfo());
        }

        /**
         * Запускает скрипт, единственный аргумент - имя скрипта, по сути простая подмена scan на сканер от файла
         */
        @InteractiveCommand(args = {1}, usage = {"execute_script <filename> - запускает скрипт по адресу <filename>"}, help = "Запускает скрипт")
        public void execute_script(List<String> argc) {
            try {
                File f = new File(argc.get(0));
                scan = new Scanner(f);
                supress_inp_invite = true;
            } catch (FileNotFoundException ex) {
                error("File not found or unable to read");
            }
        }
    }

    /**
     * Метод, который мне даже понравилось писать, несмотря на его малую читабельность и кучу <s>костылей</s> прикольных идей и возможностей для реализации. Запускает интерактивный режим программы, а также контролирует чтобы программа не улетела в неконтролируемые Exceptы при выполнении введенных пользователем команд, к тому же проверяет правильное ли кол-во аргументов подано на вход, если нет - рассказывает пользователю как ему правильно себя вести (вызывает help от запрашиваемой команды). Более того, этот метод также записывает историю успешного выполнения команд и просто тащит всю программу на себе.
     *<br>
     * <img width="500" src="https://chillywilly.club/uploads/posts/2023-03/thumbs/1679759427_chillywilly-club-p-takina-inoue-art-art-joyreactor-devushki-58.jpg">
     */
    public static void start() {
        char_col = CharacterCollection.getInstance();
        Cmds cmd_class = new Cmds();
        feedback("Interactive mode started");
        var in_scan = new Scanner(System.in);
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
                    history.add(pre);
                    if (history.size() > 14) {
                        history.poll();
                    }
                } catch (NoSuchMethodException ex) {
                    error("No such command");
                } catch (InvocationTargetException | IllegalAccessException e) {
                    error("Command execution error");
                }
            }
        }
    }
}
