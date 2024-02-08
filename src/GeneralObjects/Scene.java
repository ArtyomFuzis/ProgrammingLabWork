package GeneralObjects;
import Interaces.IPersonCharacter;
import java.util.HashMap;

public class Scene {
    private static Scene _instance;

    private Scene() {
        person_characters = new HashMap<>();
    }

    public static Scene getInstance() {
        if (_instance == null) {
            _instance = new Scene();
        }
        return _instance;
    }

    private final HashMap<String, IPersonCharacter> person_characters;
    public IPersonCharacter getCharacterByName(String s)
    {
        return person_characters.get(s);
    }
    public void addCharacter(IPersonCharacter ch)
    {
        person_characters.put(ch.getName(),ch);
    }

}
