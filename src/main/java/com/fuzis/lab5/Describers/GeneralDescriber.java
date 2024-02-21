package com.fuzis.lab5.Describers;

/**
 * Просто если надо что-то описать от лица стороннего наблюдателя, тут набор констант и "приёмов"-методов
 */
public class GeneralDescriber {
    /**
     * Просто енам с миллиардом текстовых констант, которые вроде нужны, а приписать их к какому-нибудь персонажу - сложная задачка. Так что это просто: "свалка слов", которую вы не видели ))
     * Конкретно этот описывает Время и место
     */
    public enum TimesAndPlaces {
        OneDay("в один день"),
        AtTheTable("за стол"),
        OnThatExactMoment("как раз в тот момент"),
        Always("всегда"),
        Sometimes("иногда"),
        OneTime("однажды"),
        Time("время"),
        Somewhere("где-то"),
        AtOurHome("в нашем доме"),
        AtOurs("у нас"),
        AtTheir("у них"),
        TogetherWithAll("вместе со всеми");

        TimesAndPlaces(String string_equivalent) {
            _string_equivalent = string_equivalent;
        }

        private final String _string_equivalent;

        @Override
        public String toString() {
            return _string_equivalent;
        }
    }

    public static String describeWere() {
        return "были";
    }

    public static String describeNot() {
        return "не";
    }

    public static String describeNothing() {
        return "ничего";
    }

    public static String describeNo() {
        return "нет";
    }

    public static String describeInSuchWay() {
        return "так";
    }

    public static String describeBecause() {
        return "потому что";
    }

    public static String describeButWasInVain() {
        return "но тщетно";
    }

    public static String describeWhen() {
        return "когда";
    }

    public static String describeBut() {
        return "но";
    }
    public static String describeOn(){return "на";}

    /**
     * Просто пишет в консоль и ставит точку после этого, а вы что ожидали?!
     * @param what что вывести в консоль
     */
    public static void describe(String what) {
        System.out.println(what + ".");
    }

    /**
     * Эффективно сделать "описание" массива, то есть вывести его через sep и поставить точку в конце
     * @param what а что вывести то надо собственно
     * @param sep чем отделять элементы массива
     */
    public static void describe(String[] what, String sep) {
        if (what.length == 0) return;
        else if (what.length == 1) {
            describe(what[0]);
        } else {
            StringBuilder out = new StringBuilder();
            for (int i = 0; i < what.length - 1; i++) {
                out.append(what[i]).append(sep);
            }
            out.append(what[what.length - 1]);
            System.out.println(out + ".");
        }
    }

    /**
     * Эффективно сделать "описание" массива, то есть вывести его через запятую с пробелом и поставить точку в конце
     * @param what а что вывести то надо собственно
     */
    public static void describe(String[] what) {
        describe(what, ", ");
    }
}
