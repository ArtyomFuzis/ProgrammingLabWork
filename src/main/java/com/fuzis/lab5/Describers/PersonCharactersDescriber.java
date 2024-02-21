package com.fuzis.lab5.Describers;

import com.fuzis.lab5.GeneralObjects.Scene;
import com.fuzis.lab5.Interaces.IPersonCharacter;
import com.fuzis.lab5.Exception.UndefinedOpinionException;
import com.fuzis.lab5.Exception.KarlsonMissedRuntimeException;
import java.util.ArrayList;

/**
 * Ну общую информацию со стороны описали, но необходимо и разобраться с персами мультика
 */
public class PersonCharactersDescriber {
    /**
     * Описывает кто был на чьей стороне во время спора
     * @param people_on_side все люди
     * @param side_text что это за сторона конфликта, например "на его стороне"
     * @param begin_text текст, который необходимо добавить вначале этого разбирательства
     * @return описание стороны конфликта на споре
     */
    private static String describeOpinionKarlsonSides(ArrayList<IPersonCharacter> people_on_side, String side_text, String begin_text) {
        StringBuilder out_value = new StringBuilder(begin_text);
        if (people_on_side.isEmpty()) {
            return "";
        } else if (people_on_side.size() == 1) {
            IPersonCharacter character = people_on_side.get(0);
            out_value.append(character.getName()).append(" ").append(character.getWas()).append(" ").append(side_text);
        } else {
            for (int i = 0; i < people_on_side.size() - 2; i++) {
                out_value.append(people_on_side.get(i).getName()).append(", ");
            }
            out_value.append(people_on_side.get(people_on_side.size() - 2).getName()).append(" и ");
            out_value.append(people_on_side.get(people_on_side.size() - 1).getName());
            out_value.append(" ").append(GeneralDescriber.describeWere()).append(" ").append(side_text);
        }
        return out_value.toString();
    }

    /**
     * Если Карлсон существует на сцене, то описывает кто и как к нему относится, иначе бросает KarlsonMissedRuntimeException
     * @param characters массив персонажей чье мнение надо описать
     * @return строку с описанием
     */
    public static String describeOpiniontoKarlson(IPersonCharacter[] characters) {
        ArrayList<IPersonCharacter> characters_positive_opinions = new ArrayList<>();
        ArrayList<IPersonCharacter> characters_negative_opinions = new ArrayList<>();
        ArrayList<IPersonCharacter> characters_other_opinions = new ArrayList<>();
        try {
            for (var el : characters) {
                switch (el.getOpinion("Карлсон")) {
                    case Positive -> characters_positive_opinions.add(el);
                    case Negative -> characters_negative_opinions.add(el);
                    default -> characters_other_opinions.add(el);
                }
            }
        }
        catch (UndefinedOpinionException ex)
        {
            throw new KarlsonMissedRuntimeException();
        }
        String out_value = "";
        IPersonCharacter karlson = Scene.getInstance().getCharacterByName("Карлсон");
        out_value += describeOpinionKarlsonSides(characters_positive_opinions, GeneralDescriber.describeOn() + " " + karlson.getPossesivePronoun() + " стороне", "");
        if (out_value.isEmpty()) {
            out_value += describeOpinionKarlsonSides(characters_negative_opinions, "против " + karlson.getNPossesivePronoun(), "");
        } else {
            out_value += describeOpinionKarlsonSides(characters_negative_opinions, "против " + karlson.getNPossesivePronoun(), ", а ");
        }
        if (out_value.isEmpty()) {
            out_value += describeOpinionKarlsonSides(characters_other_opinions, "в стороне", "");
        } else {
            out_value += describeOpinionKarlsonSides(characters_other_opinions, "в стороне", ", в то время как ");
        }
        return out_value;
    }

    /**
     * Возвращает оформленную цитату персонажа
     * @param ch чья цитата должна быть оформлена
     * @return строку с описанием
     */
    public static String getDescribedQuote(IPersonCharacter ch) {
        return ch.getQuote() + " -- " + GeneralDescriber.describeInSuchWay() + " " + ch.getPronoun() + " " + GeneralDescriber.TimesAndPlaces.OneTime + " " + ch.getExpressed();
    }
    /**
     * Возвращает оформленную мечту персонажа
     * @param ch чья мечта должна быть оформлена
     * @return строку с описанием
     */
    public static String getDescribedDream(IPersonCharacter ch) {
        return ch.getName() + " " + ch.getDreamed() + ", " + ch.toDream();
    }
    /**
     * Возвращает оформленное ворчание персонажа
     * @param ch чьё ворчание должна быть оформлена
     * @param time когда это нехороший человек ворчал (или где)
     * @return строку с описанием
     */
    public static String getDescribedGrumble(IPersonCharacter ch, GeneralDescriber.TimesAndPlaces time) {
        return ch.getName() + " " + time + " " + ch.getGrumbled() + ", " + ch.toGrumble();
    }
    /**
     * Возвращает оформленное "ел" персонажа
     * @param ch тот кто ел, так и не позвав меня?!
     * @param place где это нехороший человек ел (или когда)
     * @return строку с описанием
     */
    public static String getDescribedEat(IPersonCharacter ch, GeneralDescriber.TimesAndPlaces place) {
        return ch.getName() + " " + ch.getAte() + " " + place;
    }
}
