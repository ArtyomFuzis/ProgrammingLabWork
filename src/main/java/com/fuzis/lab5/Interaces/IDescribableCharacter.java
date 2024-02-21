package com.fuzis.lab5.Interaces;

/**
 * Самое сложное - это описать все это многообразие персонажей и этот класс пытается решить это проблему с помощью констант, зависящих только от пола <s>Осталось его определить<s/>
 * <br>
 * <img width="300" src="https://w.forfun.com/fetch/d9/d9002a384ae7e3a6ec9632a261c2a06d.jpeg">
 */
public interface IDescribableCharacter extends IGenderable {


    default String getPronoun() {
        return switch (this.getSex()) {
            case Male -> "он";
            case Female -> "она";
            default -> "оно"; //Я не рискну туда лезть)
        };
    }

    default String getPossesivePronoun() {
        return switch (this.getSex()) {
            case Female -> "её";
            default -> "его";
        };
    }

    default String getNPossesivePronoun() {
        return switch (this.getSex()) {
            case Female -> "неё";
            default -> "него";
        };
    }

    default String getDreamed() {
        return switch (this.getSex()) {
            case Male -> "мечтал";
            case Female -> "мечтала";
            default -> "мечтало";
        };
    }

    default String getSpent() {
        return switch (this.getSex()) {
            case Male -> "проводил";
            case Female -> "проводила";
            default -> "проводило";
        };
    }

    default String getExpressed() {
        return switch (this.getSex()) {
            case Male -> "выразился";
            case Female -> "выразилась";
            default -> "выразилось";
        };
    }

    default String getDone() {
        return "сделать";
    }

    default String getGrumbled() {
        return switch (this.getSex()) {
            case Male -> "ворчал";
            case Female -> "ворчала";
            default -> "ворчало";
        };
    }
    default String getAte()
    {
        return switch (this.getSex()) {
            case Male -> "ел";
            case Female -> "ела";
            default -> "ело";
        };
    }

    default String getAppeared() {
        return switch (this.getSex()) {
            case Male -> "появился";
            case Female -> "появилась";
            default -> "появилось";
        };
    }

    default String getAppearedTimes() {
        return switch (this.getSex()) {
            case Male -> "появлялся";
            case Female -> "появлялась";
            default -> "появлялось";
        };
    }
    default String getCan()
    {
        return switch (this.getSex()) {
            case Male -> "мог";
            case Female -> "могла";
            default -> "могло";
        };
    }

    default String getWas() {
        return switch (this.getSex()) {
            case Male -> "был";
            case Female -> "была";
            default -> "было";
        };
    }

    default String getToSit() {
        return "садиться";
    }

}
