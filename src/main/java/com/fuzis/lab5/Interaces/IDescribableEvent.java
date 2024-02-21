package com.fuzis.lab5.Interaces;

/**
 * Описываемый ивент, например можно описать как прошло его начало
 */
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
