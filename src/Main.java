/*
"Если меня не кормят, я уже не я" -- так он однажды выразился. Фрекен Бок мечтала,
 чтобы Карлсон не проводил у них время, но тщетно, потому что Малыш и дядя Юлиус были
 на его стороне. Фрекен Бок всегда ворчала, если он появлялся как раз в тот момент,
 когда надо садиться за стол, но сделать она ничего не могла, и Карлсон ел вместе со всеми.
 */

import Abstracts.DefaultCartoonPersonCharacter;
import Characters.FrekenBock;
import Characters.Karlson;
import Describers.GeneralDescriber;
import Describers.PersonCharactersDescriber;
import Enums.Opinion;
import Enums.Sex;
import GeneralObjects.Scene;
import Interaces.INameable;
import Interaces.IPersonCharacter;

public class Main {
    public static void main(String[] args) throws NoSuchFieldException {
        Scene scene = Scene.getInstance();
        scene.addCharacter(new Karlson());
        scene.addCharacter(new FrekenBock());
        scene.addCharacter(new DefaultCartoonPersonCharacter("Малыш", Sex.Male) {
            {
                this.setOpinion("Карлсон", Opinion.Positive);
            }
        });
        scene.addCharacter(new DefaultCartoonPersonCharacter("дядя Юлиус", Sex.Male) {
            {
                this.setOpinion("Карлсон", Opinion.Positive);
            }
        });
        //System.out.println(scene.getCharacterByName("Фрекен Бок").toGrumble());
        GeneralDescriber.describe(PersonCharactersDescriber.getDescribedQuote(scene.getCharacterByName("Карлсон")));
        GeneralDescriber.describe(new String[]{
                PersonCharactersDescriber.getDescribedDream(scene.getCharacterByName("Фрекен Бок")),
                GeneralDescriber.describeButWasInVain(),
                GeneralDescriber.describeBecause() + " " + PersonCharactersDescriber.describeOpiniontoKarlson(new IPersonCharacter[]{scene.getCharacterByName("Малыш"), scene.getCharacterByName("дядя Юлиус")})
        });
        GeneralDescriber.describe(PersonCharactersDescriber.getDescribedGrumble(scene.getCharacterByName("Фрекен Бок"), GeneralDescriber.TimesAndPlaces.Always));



        Karlson kar = new Karlson();
        Karlson.KarlsonFriend friend = kar.new KarlsonFriend("123",Sex.Male);



        /*Meal.Ingredient ingr = new Meal.Ingredient("123",234);
        Meal.Ingredient ingr2 = Meal.describeIngredient(ingr,"the descripion");
        IDescribable des = (IDescribable) ingr2;
        System.out.println(des.describe());*/

    }


}