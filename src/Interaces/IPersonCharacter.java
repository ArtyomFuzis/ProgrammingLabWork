package Interaces;

import Enums.Opinion;
import Exception.UndefinedOpinionException;
public interface IPersonCharacter extends ICharacter, IQuoteable {
    String toDream();

    String toGrumble();
    Opinion getOpinion(String ch) throws UndefinedOpinionException;
    void setOpinion (String ch, Opinion op);
}
