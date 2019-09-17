package DietManager.Model.Entry;
import DietManager.Model.Food.*;

import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * FoodEntry
 * Class to provide the string to retrieve a food entry
 */
public class FoodEntry {

    private Food food;
    private double servings;

    /**
     * Creates a new FoodEntry.
     * @param food The food that was consumed on the associated date.
     * @param servings The servings of food that was consumed.
     * @throws InvalidParameterException Thrown in the case of a null or invalid value.
     */
    public FoodEntry(Food food, double servings) throws InvalidParameterException {

        if (food != null) {
            this.food = food;
        } else {
            throw new InvalidParameterException("Invalid food for food entry.");
        }

        if (servings >= 0) {
            this.servings = servings;
        } else {
            throw new InvalidParameterException("Invalid servings value.");
        }
    }

    public FoodEntry(){}

    /**
     * Returns the food associated with this entry.
     * @return The food associated with this entry.
     */
    public Food getFood() {
        return this.food;
    }

    /**
     * Returns the servings of food associated with the entry.
     * @return The servings of food associated with the entry.
     */
    public double getServings() {
        return this.servings;
    }

    /**
     * Returns the total calories of the food associated with the entry.
     * @return The total calories of the food associated with the entry.
     */
    public double getTotalCalories(){
        return this.food.getCalories()*this.servings;
    }

    /**
     * Returns the total protein of the food associated with the entry.
     * @return The total protein of the food associated with the entry.
     */
    public double getTotalProtein(){
        return this.food.getProtein()*this.servings;
    }

    /**
     * Returns the total carbs of the food associated with the entry.
     * @return The total carbs of the food associated with the entry.
     */
    public double getTotalCarbs(){
        return this.food.getCarbs()*this.servings;
    }

    /**
     * Returns the total fat of the food associated with the entry.
     * @return The total fat of the food associated with the entry.
     */
    public double getTotalFat(){
        return this.food.getFat()*this.servings;
    }

    /**
     * Returns the calories of the food associated with the entry.
     * @return The calories of the food associated with the entry.
     */
    public double getCalories() {
        return this.food.getCalories();
    }

    /**
     * Sets teh food associated with the entry.
     * @param food The food associated with the entry.
     */
    public void setFood(Food food) {
        this.food = food;
    }

    /**
     * Sets the servings of the food.
     * @param servings Set how many servings of the food were consumed.
     */
    public void setServings(double servings) {
        this.servings = servings;
    }
}
