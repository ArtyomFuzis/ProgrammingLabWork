package com.fuzis.proglab.Characters;

import com.fuzis.proglab.DefaultCartoonPersonCharacter;
import com.fuzis.proglab.Enums.Sex;

/**
 * Карлсон (крышный, не Такер) очень простой персонаж, поэтому его класс не имеет ничего, кроме его друзей
 */
public class Karlson extends DefaultCartoonPersonCharacter {
    /**
     * <s>Собутыльник</s> Соваренник Карлсона тоже простой человек, не думаю что у него много функционала :(
     */
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
