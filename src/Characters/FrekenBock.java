package Characters;

import Abstracts.DefaultCartoonPersonCharacter;
import Describers.GeneralDescriber;
import Enums.Opinion;
import Enums.Sex;
import Events.EatingEvent;
import GeneralObjects.Scene;
import Interaces.IPersonCharacter;
import Exception.KarlsonMissedRuntimeException;

public class FrekenBock extends DefaultCartoonPersonCharacter {
    public FrekenBock() {
        super("Фрекен Бок", Sex.Female);
        this.setOpinion("Карлсон", Opinion.Negative);
    }

    @Override
    public String toDream() {   //Вот чего-чего а этой связности не избежать
        //Без Карлсона или если он был бы совсем другим - она бы не мечтала об этом
        //Мечта отвечает на вопрос "О чём?"
        IPersonCharacter karlson = Scene.getInstance().getCharacterByName("Карлсон");
        //Ну зависимость от глобальных истин русского языка...
        //Ну она же формирует мечты на каком-то языке используя описательные принципы, которые реализует этот класс)
        return "чтобы " + karlson.getName() + " " + GeneralDescriber.describeNot() + " " + karlson.getSpent() + " " + GeneralDescriber.TimesAndPlaces.AtTheir + " " + GeneralDescriber.TimesAndPlaces.Time;
    }

    public String toGrumble() {
        try {
            IPersonCharacter karlson = Scene.getInstance().getCharacterByName("Карлсон");
            EatingEvent ev = new EatingEvent();
            //Аналогично toDream
            return "если " + karlson.getPronoun() + " " + karlson.getAppearedTimes() + " " +
                    GeneralDescriber.TimesAndPlaces.OnThatExactMoment + ", " +
                    GeneralDescriber.describeWhen() +" "+ev.toStartAnonymous()+  ", " +
                    GeneralDescriber.describeBut() + " " + this.getDone() + " " + this.getPronoun() +
                    " " + GeneralDescriber.describeNothing() + " " + GeneralDescriber.describeNo() +
                    " " + this.getCan() + ", и " + ev.toTakePartInEvent(karlson) + " " + GeneralDescriber.TimesAndPlaces.TogetherWithAll;
        }
        catch(Exception ex)
        {
            throw new KarlsonMissedRuntimeException();
        }
    }
}
