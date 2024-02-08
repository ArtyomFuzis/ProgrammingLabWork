package Events;

import Abstracts.Meal;
import Describers.GeneralDescriber;
import Interaces.ICharacter;
import Interaces.IDescribableEvent;
import Interaces.IPersonCharacter;

public class EatingEvent implements IDescribableEvent {
    public IPersonCharacter[] members;
    public Meal[] food;
    public String toStartAnonymous()
    {
        return getNeed() + " " + getToSit() + " " + GeneralDescriber.TimesAndPlaces.AtThetable;
    }
    public String toStart()
    {
        return "...";
    }

    @Override
    public String toTakePartInEvent(ICharacter s) {
        return s.getName() + " " + s.getAte();
    }
}
