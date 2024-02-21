package com.fuzis.lab5.GeneralObjects;
import com.fuzis.lab5.Interaces.IPersonCharacter;
import java.util.HashMap;

/**
 * Сцена, вообще-то даже у простых персонажей должна быть сцена, где они должны сиять, а не только у звездных айдолов!
 * <br>
 * <img width="500" src="https://animotaku.fr/wp-content/uploads/2023/06/anime-Oshi-No-Ko-Saison-2-date-sortie-trailer-1536x864.jpeg"
 */
public class Scene {
    private static Scene _instance;

    private Scene() {
        person_characters = new HashMap<>();
    }

    /**
     * Сцена - синглтон, а вы ожидали что места хватит на всех?
     * @return Монополистскую сцену этого цирка
     */
    public static Scene getInstance() {
        if (_instance == null) {
            _instance = new Scene();
        }
        return _instance;
    }

    private final HashMap<String, IPersonCharacter> person_characters;

    /**
     * "ГДЕ ЧЕРТОВ КАРЛСОН?" - тот вопрос, на который без проблем ответит эта функция, либо даст всеми любимый null
     * @param s "КОГО НАДО?"
     * @return персонажа, имя которого указано в s
     */
    public IPersonCharacter getCharacterByName(String s)
    {
        return person_characters.get(s);
    }

    /**
     * Добавляет нового персонажа на сцену
     * @param ch персонаж, которой решил выйти на сцену
     */
    public void addCharacter(IPersonCharacter ch)
    {
        person_characters.put(ch.getName(),ch);
    }

}
