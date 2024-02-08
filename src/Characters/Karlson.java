package Characters;

import Abstracts.DefaultCartoonPersonCharacter;
import Enums.Sex;

public class Karlson extends DefaultCartoonPersonCharacter {
    public class KarlsonFriend extends DefaultCartoonPersonCharacter
    {
        public KarlsonFriend(String name,Sex sex)
        {
            super(name,sex);
            this.quote = Karlson.this.quote;
        }
    }
    public Karlson() {
        super("Карлсон", Sex.Male);
        this.quote = "Если меня не кормят, я уже не я";
    }
}
