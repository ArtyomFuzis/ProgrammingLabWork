package com.fuzis.lab5;
import com.fuzis.lab5.Enums.Opinion;
import com.fuzis.lab5.Enums.Popularity;
import com.fuzis.lab5.Enums.Sex;
import com.fuzis.lab5.GeneralObjects.DefaultCartoonPersonCharacter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

/**
 * Наша класс-обертка для коллекции (синглтон), хранящей <s>аниме девочек</s> персонажей советских мультиков, также данный класс реализует сохранение/загрузку данных из json файла.
 * Статичное поле fileName - отвечает за файл хранения данных, по умолчанию "character.json"
 * <br>
 * <img width="700" height="400" src="https://img.goodfon.ru/original/1920x1080/d/bd/devushki-maple-coconut-azuki-neko-para-vanilla-cinnamon-art.jpg">
 */
public class CharacterCollection
{
    /**
     * Служебный метод на тот случай если <s>у кого-то сгорит</s> потребуется много раз переводить что-то из неизвестности (нас самом деле являющейся Long) в Integer
     * Очень не рекомендуется применять на что-то кроме enum, автор сам понятие не имеет какой там except ^___^
     * @param l любой объект, который Long и который должен стать Integer :)
     * @return null если поданный объект - null, или значение приведенное к Integer
     */
    static Integer castInteger(Object l)
    {
        if (l == null)return null;
        return ((Long) l).intValue();
    }
    public static String fileName = "character.json";

    private static CharacterCollection _instance;

    /**
     * Выдает объект класса, если он ранее не запрашивался, то создает его с конструктором по умолчанию
     * @return объект класса CharacterCollection по умолчанию
     */
    public static CharacterCollection getInstance()
    {
        if(_instance == null)_instance = new CharacterCollection();
        return _instance;
    }
    private final HashMap<String, DefaultCartoonPersonCharacter> characters;

    /**
     * Возврат нашей коллекции в её виде <code>{@literal HashMap<String, DefaultCartoonPersonCharacter>}</code>
     * @return всю коллекцию персонажей
     */
    public HashMap<String, DefaultCartoonPersonCharacter> getCharacters() {
        return characters;
    }

    /**
     * Добавить персонажа в семью (коллекцию), в качестве идентификатора использует имя персонажа
     * @param charac персонаж, которого надо добавить
     */
    public void add(DefaultCartoonPersonCharacter charac)
    {
        characters.put(charac.getName(),charac);
    }

    /**
     * Добавить персонажа в семью (коллекцию), в качестве идентификатора использует значение id
     * @param id идентификатор для хранения нового персонажа в коллекции
     * @param charac персонаж, которого надо добавить
     */
    public void add(String id, DefaultCartoonPersonCharacter charac)
    {
        characters.put(id,charac);
    }

    /**
     * Загрузить в коллекцию данные из указанного в fileName json файла. Если указанный файл не найден или недоступен, то пишется warning в консоль "Data file not found or IO error", если файл не соответствует формату хранения коллекции, то выводится warning "Data file parse error"
     */
    public void load()
    {
        Scanner scan=null;
        try {
            File f = new File(fileName);
            scan = new Scanner(f);
            StringBuilder sb = new StringBuilder();
            while(scan.hasNext()){sb.append(scan.nextLine());}
            JSONObject parsed_obj = (JSONObject) new JSONParser().parse(sb.toString());
            JSONObject arr = (JSONObject)parsed_obj.get("characters");
            for(var key : arr.keySet())
            {
                JSONObject el = (JSONObject) arr.get(key);
                String name = (String)el.get("name");
                Sex sex = null;
                if(el.get("sex") != null) sex = Sex.values()[castInteger(el.get("sex"))];
                HashMap<String, Integer> opinions_int = (HashMap<String, Integer>) el.get("opinions");
                HashMap<String,Opinion> opinions = new HashMap<>();
                for(var el2 : opinions_int.keySet())opinions.put(el2,Opinion.values()[castInteger(opinions_int.get(el2))]);
                String quote = (String)el.get("quote");
                ArrayList<String> additional_names = (ArrayList<String>)el.get("additional_names");
                String description = (String)el.get("description");
                Double height = (Double)el.get("height");
                Double weight = (Double)el.get("weight");
                Double age = (Double)el.get("age");
                Integer health = castInteger(el.get("health"));
                Boolean anime_character = (Boolean) el.get("is_anime_character");
                Popularity popularity = null;
                if(el.get("popularity") != null) popularity = Popularity.values()[castInteger(el.get("popularity"))];
                DefaultCartoonPersonCharacter charac = new DefaultCartoonPersonCharacter(name,sex,quote,opinions, additional_names, height, weight, anime_character, popularity, description,age,health);
                this.characters.put((String) key,charac);
            }

        } catch (FileNotFoundException e) {
            Interactive.warn("Data file not found or IO error");
        } catch ( ParseException  | ClassCastException | NullPointerException e ) {
            Interactive.warn("Data file parse error");
        }
        finally {
            if(scan != null)scan.close();
        }
    }

    /**
     * Вернуть конкретный элемент коллекции
     * @param id идентификатор элемента, который необходимо получить
     * @return указанный элемент коллекции, если не найдет, то null
     */
    public DefaultCartoonPersonCharacter getCharacter(String id){return characters.get(id);}

    /**
     * Удалить персонажа с определенным id, если не найден - ничего не делает
     * @param id идентификатор персонажа для удаления
     */
    public void deleteCharacter(String id){characters.remove(id);}
    private final Date init_date;

    /**
     * Инициализирует <s>банку</s> коллекцию с персонажами и пытается считать данные из указанного в fileName json-файла, а так же устанавливает дату инициализации (для getInfo)
     */
    public CharacterCollection()
    {
        this.characters = new HashMap<>();
        init_date = new Date();
        load();
    }

    /**
     * <s color="red">БЕЗВОЗВРАТНО УНИЧТОЖИТЬ КОТИКОВ</s> Очистить коллекцию (нужно старое разрушить, чтобы построить новое)
     */
    public void clear()
    {
        characters.clear();
    }

    /**
     * Возвращает информацию о коллекции
     * @return информацию о том, какой тип данных используется для коллекции, кол-во элементов и дату инициализацию
     */
    public String getInfo()
    {
        return "Collection Type: " + characters.getClass() + "\n"
                + "Count of elements: " + characters.size() + "\n"
                + "Initialization date: " + init_date;

    }

    /**
     * Возвращает размер коллекции
     * @return characters.size()
     */
    public int size()
    {
        return characters.size();
    }

    /**
     * Сохраняет коллекцию в файл, если файл недоступен - пишет error "IO write error".
     * При удачном сохранении пишет в консоль info "Successful saving".
     * По умолчанию сохраняется в файл с именем fileName
     */
    public void save()
    {
        save(fileName);
    }

    /**
     * Сохраняет коллекцию в файл, если файл недоступен - пишет error "IO write error".
     * При удачном сохранении пишет в консоль info "Successful saving".
     * @param fileName имя файла для сохранения, по канонам windows, рекомендуется имя "*.json"
     */
    @SuppressWarnings("unchecked")
    public void save(String fileName) {
        JSONObject write_arr = new JSONObject();
        for(var el : this.getCharacters().keySet())
        {
            JSONObject write_obj = new JSONObject();
            DefaultCartoonPersonCharacter character = this.getCharacters().get(el);
            write_obj.put("name",character.getName());
            if(character.getSex() == null)write_obj.put("sex",null);
            else write_obj.put("sex",character.getSex().ordinal());

            HashMap<String,Integer> new_opinions = new HashMap<>();
            for(var el2 : character.getOpinions().keySet())
            {
                new_opinions.put(el2,character.getOpinions().get(el2).ordinal());
            }
            write_obj.put("opinions",new_opinions);
            write_obj.put("quote",character.getSimpleQuote());
            write_obj.put("additional_names",character.getAdditionalNames());
            write_obj.put("description", character.getDescription());
            write_obj.put("height",character.getHeight());
            write_obj.put("weight",character.getWeight());
            write_obj.put("health",character.getHealth());
            write_obj.put("age",character.getAge());
            write_obj.put("is_anime_character",character.getAnimeCharacter());
            if(character.getPopularity() == null) write_obj.put("popularity",null);
            else write_obj.put("popularity",character.getPopularity().ordinal());
            write_arr.put(el,write_obj);
        }
        var res_object = new JSONObject();
        res_object.put("characters",write_arr);
        try {
            OutputStreamWriter wr = new OutputStreamWriter(new FileOutputStream(fileName));
            wr.write(res_object.toJSONString());
            wr.close();
        }
        catch (IOException ex)
        {
            Interactive.error("IO write error");
            return;
        }


        Interactive.feedback("Successful saving");
    }
}
