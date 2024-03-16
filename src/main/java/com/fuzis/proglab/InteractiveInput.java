package com.fuzis.proglab;

import com.fuzis.proglab.Enums.Opinion;
import com.fuzis.proglab.Enums.Popularity;
import com.fuzis.proglab.Enums.Sex;
import com.fuzis.proglab.Server.ServerExecutionModule;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

public class InteractiveInput {
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

        public void close() {
            end = true;
        }

        public boolean hasNext() {
            if (scan != null) return scan.hasNext();
            return !end;
        }

        public void add(String a) {
            arr.add(a);
        }

        public String nextLine() {
            if (scan != null) return scan.nextLine();
            if (end) throw new IllegalStateException("FakeScanner is closed");
            while (arr.isEmpty() && !end) {
                insert_callback.accept(this);
            }
            if (end) {
                return "";
            }
            return arr.pop();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FakeScanner that = (FakeScanner) o;
            if (this.scan != null || that.scan != null) {
                if (this.scan == null || that.scan == null) return false;
                return this.scan.equals(that.scan);
            }
            return this.arr.equals(that.arr) && this.end == that.end && this.insert_callback.equals(that.insert_callback);
        }
    }
    FakeScanner scan;
    Consumer<Object> println_supress;
    Consumer<Object> print_supress;
    Method writing_method;
    Method reading_method;
    boolean mode;
    public InteractiveInput(FakeScanner scan, Consumer<Object> println_supress, Consumer<Object> print_supress)
    {
        this.print_supress=print_supress;
        this.println_supress=println_supress;
        this.scan=scan;
        this.mode = false;
    }
    public InteractiveInput(Class writing_cls,Class reading_cls)
    {
        try {
            writing_method = writing_cls.getMethod("write", AppData.TransferData.class);
            reading_method = reading_cls.getMethod("read");
            mode=true;
        }
        catch (NoSuchMethodException ex)
        {
            System.out.println("ProgrammerBigMistakeException: " + ex.getLocalizedMessage());
        }
    }
    public void write(AppData.TransferData data)
    {
        try {
            writing_method.invoke(null, data);
        }
        catch (IllegalAccessException | InvocationTargetException ex)
        {
            System.out.println("Something went wrong,programmer mistake.... " + ex.getLocalizedMessage());
        }
    }
    public AppData.TransferData read()
    {
        try {
            return (AppData.TransferData)reading_method.invoke(null,null);
        }
        catch (IllegalAccessException | InvocationTargetException ex)
        {
            System.out.println("Something went wrong,programmer mistake.... " + ex.getLocalizedMessage());
            return null;
        }
    }
    public String type_interactive()
    {
        if(mode)
        {
            write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"",10,null));
            var resp = read();
            return resp.body();
        }
        println_supress.accept("Введите какое поле вы желаете изменить, иначе End для выхода");
        println_supress.accept("Варианты ввода: Name, Sex, Quote, Opinions, AdditionalNames, Height, Weight, Age, Health, IsAnimeCharacter, Popularity, Description");
        return scan.nextLine().trim().toLowerCase();
    }
    public String name_interactive() {
        if(mode)
        {
            write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"",11,null));
            var resp = read();
            if (resp.body()==null)return null;
            return resp.body();
        }
        println_supress.accept("Введите имя персонажа");
        String name = null;
        boolean match = false;
        while (!match) {
            name = scan.nextLine();
            if (name.matches("\\s*")) {
                println_supress.accept("Воспринято как <null>");
                break;
            }
            match = name.matches("\\s*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*");
            if (!match) {
                println_supress.accept("Введенное значение не может быть именем, попробуйте ещё раз");
            }
        }
        return name;
    }

    public Sex sex_interactive() {
        if(mode)
        {
            write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"",12,null));
            var resp = read();
            if (resp.body()==null)return null;
            return Sex.valueOf(resp.body());
        }
        println_supress.accept("Введите пол персонажа");
        print_supress.accept("Возможные варианты ввода: ");
        for (int i = 0; i < Sex.values().length; i++) {
            if (i != Sex.values().length - 1) print_supress.accept(Sex.values()[i] + ", ");
            else println_supress.accept(Sex.values()[i]);
        }
        Sex sex = null;
        boolean match = false;
        while (!match) {
            String data = scan.nextLine();
            if (data.matches("\\s*")) {
                println_supress.accept("Воспринято как <null>");
                break;
            }
            try {
                data = data.trim();
                sex = Sex.valueOf(data.substring(0, 1).toUpperCase() + data.substring(1).toLowerCase(Locale.ROOT));
                match = true;
            } catch (IllegalArgumentException ex) {
                println_supress.accept("Введенное значение не может быть полом, попробуйте ещё раз");
            }
        }
        return sex;
    }

    public String quote_interactive() {
        if(mode)
        {
            write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"",13,null));
            var resp = read();
            if (resp.body()==null)return null;
            return resp.body();
        }
        println_supress.accept("Введите цитату персонажа");
        String quote = null;
        boolean match = false;
        while (!match) {
            quote = scan.nextLine();
            if (quote.matches("\\s*")) {
                println_supress.accept("Воспринято как <null>");
                break;
            }
            match = quote.matches("(?:\\s*[a-zA-Zа-яА-Яё,Ё_.?\"'`!#@$0-9]+\\s*)+");
            if (!match) {
                println_supress.accept("Введенное значение не может быть цитатой, попробуйте ещё раз");
            }
        }
        return quote;
    }

    public Double height_interactive() {
        if(mode)
        {
            write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"",14,null));
            var resp = read();
            if (resp.body()==null)return null;
            return Double.parseDouble(resp.body());
        }
        println_supress.accept("Введите рост персонажа");
        Double height = null;
        boolean match = false;
        while (!match) {
            String data = scan.nextLine();
            if (data.matches("\\s*")) {
                println_supress.accept("Воспринято как <null>");
                break;
            }
            try {
                data = data.trim();
                height = Double.valueOf(data);
                match = true;
            } catch (NumberFormatException ex) {
                println_supress.accept("Введенное значение не может быть ростом, попробуйте ещё раз");
            }
        }
        return height;
    }

    public Double weight_interactive() {
        if(mode)
        {
            write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"",15,null));
            var resp = read();
            if (resp.body()==null)return null;
            return Double.parseDouble(resp.body());
        }
        println_supress.accept("Введите вес персонажа");
        Double weight = null;
        boolean match = false;
        while (!match) {
            String data = scan.nextLine();
            if (data.matches("\\s*")) {
                println_supress.accept("Воспринято как <null>");
                break;
            }
            try {
                data = data.trim();
                weight = Double.valueOf(data);
                match = true;
            } catch (NumberFormatException ex) {
                println_supress.accept("Введенное значение не может быть весом, попробуйте ещё раз");
            }
        }
        return weight;
    }

    public Popularity popularity_interactive() {
        if(mode)
        {
            write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"",16,null));
            var resp = read();
            if (resp.body()==null)return null;
            return Popularity.valueOf(resp.body());
        }
        println_supress.accept("Введите популярность персонажа");
        print_supress.accept("Возможные варианты ввода: ");
        for (int i = 0; i < Popularity.values().length; i++) {
            if (i != Popularity.values().length - 1) print_supress.accept(Popularity.values()[i] + ", ");
            else println_supress.accept(Popularity.values()[i]);
        }
        Popularity popul = null;
        boolean match = false;
        while (!match) {
            String data = scan.nextLine();
            if (data.matches("\\s*")) {
                println_supress.accept("Воспринято как <null>");
                break;
            }
            try {
                data = data.trim();
                popul = Popularity.valueOf(data.substring(0, 1).toUpperCase() + data.substring(1).toLowerCase(Locale.ROOT));
                match = true;
            } catch (IllegalArgumentException ex) {
                println_supress.accept("Введенное значение не может быть популярностью, попробуйте ещё раз");
            }
        }
        return popul;
    }

    public Double age_interactive() {
        if(mode)
        {
            write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"",17,null));
            var resp = read();
            if (resp.body()==null)return null;
            return Double.parseDouble(resp.body());
        }
        println_supress.accept("Введите возраст персонажа");
        Double age = null;
        boolean match = false;
        while (!match) {
            String data = scan.nextLine();
            if (data.matches("\\s*")) {
                println_supress.accept("Воспринято как <null>");
                break;
            }
            try {
                data = data.trim();
                age = Double.valueOf(data);
                match = true;
            } catch (NumberFormatException ex) {
                println_supress.accept("Введенное значение не может быть возрастом, попробуйте ещё раз");
            }
        }
        return age;
    }

    public String description_interactive() {
        if(mode)
        {
            write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"",18,null));
            var resp = read();
            if (resp.body()==null)return null;
            return resp.body();
        }
        println_supress.accept("Введите краткое описание персонажа (одна строка)");
        String description = null;
        boolean match = false;
        while (!match) {
            description = scan.nextLine();
            if (description.matches("\\s*")) {
                println_supress.accept("Воспринято как <null>");
                break;
            }
            match = description.matches("(?:\\s*[\\-a-zA-Zа-яА-ЯёЁ_,.?\"'`!#@$0-9]+\\s*)+");
            if (!match) {
                println_supress.accept("Введенное значение не может быть описанием, попробуйте ещё раз");
            }
        }
        return description;
    }

    public Integer health_interactive() {
        if(mode)
        {
            write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"",19,null));
            var resp = read();
            if (resp.body()==null)return null;
            return Integer.parseInt(resp.body());
        }
        println_supress.accept("Введите здоровье персонажа (целое число)");
        Integer health = null;
        boolean match = false;
        while (!match) {
            String data = scan.nextLine();
            if (data.matches("\\s*")) {
                println_supress.accept("Воспринято как <null>");
                break;
            }
            try {
                data = data.trim();
                health = Integer.valueOf(data);
                match = true;
            } catch (NumberFormatException ex) {
                println_supress.accept("Введенное значение не может быть здоровьем, попробуйте ещё раз");
            }
        }
        return health;
    }

    public Boolean isAnimeCharacter_interactive() {
        if(mode)
        {
            write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"",20,null));
            var resp = read();
            if (resp.body()==null)return null;
            return Boolean.parseBoolean(resp.body());
        }
        println_supress.accept("Введите является ли данный персонажем аниме  (Да/Нет/Yes/No/はい/いいえ)");
        Boolean isAnimeCharacter = null;
        boolean match = false;
        while (!match) {
            String data = scan.nextLine();
            if (data.matches("\\s*")) {
                println_supress.accept("Воспринято как <null>");
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
                println_supress.accept("Введенное значение не входит в Да/Нет/Yes/No/はい/いいえ, попробуйте ещё раз");
            }
        }
        return isAnimeCharacter;
    }

    public List<String> additionalnames_interactive() {
        if(mode)
        {
            write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"",21,null));
            var resp = read();
            if (resp.body()==null)return null;
            return new ArrayList<>(Arrays.stream(resp.body().trim().split("\\s*,\\s*")).toList());
        }
        println_supress.accept("Введите дополнительные имена персонажа в строке через запятую");
        ArrayList<String> additionalNames = null;
        boolean match = false;
        while (!match) {
            String data = scan.nextLine();
            if (data.matches("\\s*")) {
                println_supress.accept("Воспринято как <null>");
                break;
            }
            match = data.matches("(?:\\s*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*,\\s*)*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*");
            if (!match) {
                println_supress.accept("Введенное значение не не соответствует формату ввода, попробуйте ещё раз");
                continue;
            }
            additionalNames = new ArrayList<>(Arrays.stream(data.trim().split("\\s*,\\s*")).toList());
        }
        return additionalNames;
    }

    public HashMap<String, Opinion> opinions_interactive() {
        if(mode)
        {
            write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"",22,null));
            var resp = read();
            System.out.println(resp);
            if (resp.body()==null)return null;
            var pre_data = resp.body().trim().split("\\s*,\\s*");
            HashMap<String,Opinion> opinions = new HashMap<>();
            for (var el : pre_data) {
                var separated = el.split("\\s*:\\s*");
                opinions.put(separated[0], Opinion.valueOf(separated[1].substring(0, 1).toUpperCase() + separated[1].substring(1).toLowerCase(Locale.ROOT)));
            }
            return opinions;
        }
        println_supress.accept("Введите мнения персонажа о других персонажах(не обязательно находящихся в коллекции), в формате: <имя>:<отношение>,<имя2>:<отношение2>...");
        print_supress.accept("Возможные варианты ввода <отношение>: ");
        for (int i = 0; i < Opinion.values().length; i++) {
            if (i != Opinion.values().length - 1) print_supress.accept(Opinion.values()[i] + ", ");
            else println_supress.accept(Opinion.values()[i]);
        }
        HashMap<String, Opinion> opinions = null;
        boolean match = false;
        while (!match) {
            String data = scan.nextLine();
            if (data.matches("\\s*")) {
                println_supress.accept("Воспринято как <null>");
                break;
            }
            match = data.matches("(?:\\s*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*:\\s*[a-zA-Z]+\\s*,\\s*)*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*:\\s*[a-zA-Z]+\\s*");
            if (!match) {
                println_supress.accept("Введенное значение не не соответствует формату ввода, попробуйте ещё раз");
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
                println_supress.accept("Введенное значение не не соответствует формату ввода (неверное значение отношения), попробуйте ещё раз");
                match = false;
            }
        }
        return opinions;
    }
    public String id_interactive()
    {
        if(mode)
        {
            write(new AppData.TransferData(AppData.TransferPurpose.Cmd,"",23,null));
            var resp = read();
            if (resp.body()==null)return null;
            return resp.body();
        }
        println_supress.accept("Введите id персонажа");
        String id = null;
        Boolean match = false;
        while (!match) {
            id = scan.nextLine();
            match = id.matches("\\s*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*");
            if (!match) {
                println_supress.accept("Введенное значение не может быть id, попробуйте ещё раз");
                continue;
            }
            return id;
        }
        return null;
    }
}
