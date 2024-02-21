package com.fuzis.lab5.Interaces;

import com.fuzis.lab5.Enums.Opinion;
import com.fuzis.lab5.Exception.UndefinedOpinionException;

/**
 * Все более "человечный" персонаж, тот у кого есть "личность"
 * То есть он может иметь культовую цитату, мечтать, ворчать и высказывать свое мнение
 * <br>
 * <img width="300" src="https://halvita.ru/wp-content/uploads/2021/03/32-12-2048x1152.jpg">
 */
public interface IPersonCharacter extends ICharacter, IQuoteable {
    String toDream();
    String toGrumble();
    Opinion getOpinion(String ch) throws UndefinedOpinionException;
    void setOpinion (String ch, Opinion op);
}
