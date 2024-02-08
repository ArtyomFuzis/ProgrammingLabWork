package Interaces;

public interface IDescribableEvent {
    default String getToEat()
    {
        return "есть";
    }
    default String getToSit()
    {
        return "садиться";
    }
    default String getNeed(){
        return "надо";
    }
    default String getAtTheTable()
    {
        return "за стол";
    }
    String toStartAnonymous();
    String toStart();
    String toTakePartInEvent(ICharacter s);
}
