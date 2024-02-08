package Describers;

import GeneralObjects.Scene;
import Interaces.IPersonCharacter;
import Exception.UndefinedOpinionException;
import Exception.KarlsonMissedRuntimeException;
import java.util.ArrayList;

public class PersonCharactersDescriber {
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

    public static String getDescribedQuote(IPersonCharacter ch) {
        return ch.getQuote() + " -- " + GeneralDescriber.describeInSuchWay() + " " + ch.getPronoun() + " " + GeneralDescriber.TimesAndPlaces.OneTime + " " + ch.getExpressed();
    }

    public static String getDescribedDream(IPersonCharacter ch) {
        return ch.getName() + " " + ch.getDreamed() + ", " + ch.toDream();
    }

    public static String getDescribedGrumble(IPersonCharacter ch, GeneralDescriber.TimesAndPlaces time) {
        return ch.getName() + " " + time + " " + ch.getGrumbled() + ", " + ch.toGrumble();
    }

    public static String getDescribedEat(IPersonCharacter ch, GeneralDescriber.TimesAndPlaces place) {
        return ch.getName() + " " + ch.getAte() + " " + place;
    }
}
