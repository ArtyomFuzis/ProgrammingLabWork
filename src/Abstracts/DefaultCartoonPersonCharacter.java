package Abstracts;

import Enums.Opinion;
import Enums.Sex;
import Interaces.IPersonCharacter;
import Exception.UndefinedOpinionException;
import Exception.UndefinedOpinionRuntimeException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

abstract public class DefaultCartoonPersonCharacter implements IPersonCharacter {

    public DefaultCartoonPersonCharacter(String name, Sex sex) {
        this.name = name;
        this.sex = sex;
    }

    //Запретим менять имя и пол, в мультиках так не принято ;)
    //Но на всякий оставим это на решение классов-наследников
    protected String name;
    protected Sex sex;
    //Но изменение цитаты оставим на наследников
    protected String quote = "It's cool!";
    protected HashMap<String, Opinion> opinions = new HashMap<>();

    @Override
    public String getQuote() {
        return "\"" + this.quote + "\"";
    }

    @Override
    public Sex getSex() {
        return this.sex;
    }

    @Override
    public String getName() {
        return this.name;
    }


    @Override
    public String toDream() {
        return "у " + this.getNPossesivePronoun() + " нет мечты!!! ~_~";
    }

    @Override
    public String toGrumble() {
        return "Ворчать - плохо!!!";
    }

    @Override
    public Opinion getOpinion(String ch) throws UndefinedOpinionException {
        Opinion op = this.opinions.get(ch);
        if(op==null)throw new UndefinedOpinionException();
        else return op;
    }

    @Override
    public void setOpinion(String ch, Opinion op) {
        this.opinions.put(ch,op);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + sex.hashCode() + name.hashCode() + opinions.hashCode();
    }

    @Override
    public String toString() {
        return "DefaultCartoonPersonCharacter{" +
                "name='" + name + '\'' +
                ", sex=" + sex +
                ", quote='" + quote + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o){
        try {
            if (this == o) return true;
            if (!(o instanceof IPersonCharacter that)) return false;
            boolean pre_res = Objects.equals(getName(), that.getName()) && getSex() == that.getSex();
            for (String el : this.opinions.keySet()) {
                pre_res &= this.getOpinion(el) == that.getOpinion(el);
            }
            return pre_res;
        }
        catch (UndefinedOpinionException ex)
        {
            throw new UndefinedOpinionRuntimeException();
        }
    }
}
