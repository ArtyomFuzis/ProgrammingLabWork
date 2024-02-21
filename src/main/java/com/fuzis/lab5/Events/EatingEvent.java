package com.fuzis.lab5.Events;

import com.fuzis.lab5.Abstracts.Meal;
import com.fuzis.lab5.Describers.GeneralDescriber;
import com.fuzis.lab5.Interaces.ICharacter;
import com.fuzis.lab5.Interaces.IDescribableEvent;
import com.fuzis.lab5.Interaces.IPersonCharacter;

/**
 * Класс специально для того, чтобы описать самое главное ивент "ЕДА!!!"
 * <br>
 * <img width= "300" src="https://i.pinimg.com/originals/44/60/37/44603720c08e034dbf78d2f7bf9f1de0.jpg">
 */
public class EatingEvent implements IDescribableEvent {
    public IPersonCharacter[] members;
    public Meal[] food;

    /**
     * Описать что все "сели за стол", не использует имен персонажей
     * @return полученное описание
     */
    public String toStartAnonymous()
    {
        return getNeed() + " " + getToSit() + " " + GeneralDescriber.TimesAndPlaces.AtTheTable;
    }

    /**
     * NOT IMPLEMENTED YET
     * @return "..."
     */
    public String toStart()
    {
        return "...";
    }

    /**
     * Описать что кто-то просто "ел")
     * @param s кто это был
     * @return строчку с описанием
     */
    @Override
    public String toTakePartInEvent(ICharacter s) {
        return s.getName() + " " + s.getAte();
    }
}
