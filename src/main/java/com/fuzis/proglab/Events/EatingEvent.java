package com.fuzis.proglab.Events;

import com.fuzis.proglab.Abstracts.Meal;
import com.fuzis.proglab.Describers.GeneralDescriber;
import com.fuzis.proglab.Interaces.ICharacter;
import com.fuzis.proglab.Interaces.IDescribableEvent;
import com.fuzis.proglab.Interaces.IPersonCharacter;

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
