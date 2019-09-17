package DietManager.Model.Food;

import javafx.util.Pair;

import java.lang.reflect.Array;
import java.security.InvalidParameterException;
import java.util.*;


/**
 * A representation of a Recipe. This class will be coposed of various food objects.
 * It will can be used to return the total values associated with the recipe.
 */
public class Recipe extends Food {

    // Data structure to store what composes this recipe.
    private ArrayList<Pair<Food,Double>> ingredients = new ArrayList<>();

    /**
     * Creates a new Recipe.
     * @param recipeName The name of the recipe.
     * @param ingredients The ingredients of the recipe.
     * @throws InvalidParameterException
     */
    public Recipe (String recipeName, ArrayList<Pair<Food,Double>> ingredients) throws InvalidParameterException {
        this.name = recipeName;
        this.ingredients = ingredients;
    }

    /**
     * Returns the total calories in the recipe as a function of the food calories per serving
     * calories and the servings of that food in this recipe.
     * @return The total calories.
     */
    @Override
    public double getCalories(){
        this.calories = 0;
        for (Pair<Food,Double> food : ingredients) {
            this.calories+= food.getKey().getCalories() * food.getValue();
        }
        return this.calories;
    }

    /**
     * Returns the total fat in the recipe as a function of the fat calories per serving
     * fat and the servings of that food in this recipe.
     * @return The total calories.
     */
    @Override
    public double getFat(){
        this.fat = 0;
        for (Pair<Food,Double> food : ingredients) {
            this.fat += food.getKey().getFat() * food.getValue();
        }
        return this.fat;
    }

    /**
     * Returns the total fat in the recipe as a function of the fat per serving
     * fat and the servings of that food in this recipe.
     * @return The total calories.
     */
    @Override
    public double getCarbs(){
        this.carbs = 0;
        for (Pair<Food,Double> food : ingredients) {
            this.carbs += food.getKey().getCarbs() * food.getValue();
        }
        return this.carbs;
    }

    /**
     * Returns the total protein in the recipe as a function of the protein per serving
     * protein and the servings of that food in this recipe.
     * @return The total calories.
     */
    @Override
    public double getProtein(){
        this.protein = 0;
        for (Pair<Food,Double> food : ingredients) {
            this.protein += food.getKey().getProtein() * food.getValue();
        }
        return this.protein;
    }
    /**
     * Returns a string that is a CSV representations of the object.
     *
     * @return A string that is a CSV representations of the object.
     */
    public String getCSV() {
        StringBuilder sb = new StringBuilder("r")
                .append(",")
                .append(name);

        for (Pair<Food,Double> food : ingredients ) {  // Append all of the ingredients and their servings.
            sb.append(",")
                    .append(food.getKey().getName())          // Food name.
                    .append(",")
                    .append(food.getValue());       // Food servings in recipe.
        }

        return sb.toString();
    }
}
