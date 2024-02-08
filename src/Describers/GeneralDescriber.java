package Describers;

public class GeneralDescriber {
    public enum TimesAndPlaces {
        OneDay("в один день"),
        AtThetable("за стол"),
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

        private TimesAndPlaces(String string_equivalent) {
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

    public static void describe(String what) {
        System.out.println(what + ".");
    }

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

    public static void describe(String[] what) {
        describe(what, ", ");
    }
}
