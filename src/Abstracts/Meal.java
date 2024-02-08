package Abstracts;

import Interaces.IDescribable;
import Interaces.INameable;

import java.util.HashMap;

public abstract class Meal implements INameable {
    public static HashMap<String, Ingredient> general_ingredients = new HashMap<>();

    public static class Ingredient implements INameable {
        public final int calorie;
        protected String name;

        public Ingredient(String name, int calorie) {
            this.calorie = calorie;
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public Meal(Ingredient[] ingredients, String name) {
        this.ingredients = ingredients;
        this.name = name;
        for (var el : this.ingredients) {
            this.calorie += el.calorie;
        }
    }

    public final Ingredient[] ingredients;
    private int calorie;

    public int getCalorie() {
        return this.calorie;
    }

    protected String name;

    @Override
    public String getName() {
        return name;
    }

    public static Ingredient describeIngredient(Ingredient ingredient, String description) {
        class DescribedIngredient extends Ingredient implements IDescribable {
            public DescribedIngredient(Ingredient ingredient1, String description) {
                super(ingredient1.name,ingredient1.calorie);
                this.description = description;
            }
            public final String description;

            @Override
            public String describe() {
                return description;
            }
        }
        return new DescribedIngredient(ingredient, description);
    }
}
