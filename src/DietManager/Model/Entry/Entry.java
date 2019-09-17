package DietManager.Model.Entry;

import DietManager.Model.Exercise.Exercise;
import DietManager.Model.Food.Food;
import DietManager.Model.ICSVable;

import java.util.*;

/**
 * This class is an representation of a day, which is composed of multiple entries types.
 */
public class Entry implements ICSVable {

    private GregorianCalendar date;
    private String dateString;
    private CalorieEntry calorieEntry;
    private WeightEntry weightEntry;
    private ArrayList<FoodEntry> foodEntries = new ArrayList<>();
    private ArrayList<ExerciseEntry> exerciseEntries = new ArrayList<>();

    /**
     * Instantiates a new entry.
     * @param date The date on which the entry occurs.
     */
    public Entry(GregorianCalendar date) {
        this.date = date;
    }
    public Entry(){}

    /**
     * Accessor for Date
     * @return Date of Entry
     */
    public GregorianCalendar getDate() {
        return this.date;
    }

    /**
     * This sets the calorie goal for the day.
     * @param calorieEntry The calorie goal for the day.
     */
    public void setCalorieEntry(CalorieEntry calorieEntry) {
        if (calorieEntry != null) {
            this.calorieEntry = calorieEntry;
        }
    }

    /**
     * Returns the calorie entry.
     * @return The calorie entry.
     */
    public CalorieEntry getCalorieEntry(){
        return this.calorieEntry;
    }

    /**
     * This sets the weight entry for the day.
     * @param weightEntry The weight entry for the day.
     */
    public void setWeightEntry(WeightEntry weightEntry) {
        if (weightEntry != null) {
            this.weightEntry = weightEntry;
        }
    }

    /**
     * Returns the weight entry for the day.
     * @return The weight entry for the day.
     */
    public WeightEntry getWeightEntry() {
        return this.weightEntry;
    }

    /**
     * Adds a food consumption entry to the day.
     * @param foodEntry The food consumption to be added to the day.
     * @return True if the operation was successful.
     */
    public boolean addFoodEntry(FoodEntry foodEntry) {
        return this.foodEntries.add(foodEntry);
    }

    /**
     * Returns a list of all food entries that match the name provided.
     * @param name The name of the food that is associated with the food entry.
     * @return All entries in on the day that contain the name of the food.
     * This can return a collection of 0 items if no food entries are present.
     */
    public Collection<FoodEntry> getFoodEntries(String name) {
        ArrayList<FoodEntry> retList = new ArrayList<>();
        for (FoodEntry entry : foodEntries) {
            if (entry.getFood().getName().equals(name)) {
                retList.add(entry);
            }
        }
        return retList;
    }

    /**
     * Returns all of the food entries associated with the entry.
     * @return All of the food entries associated with the entry.
     * Can return a list of 0 items if there are no food entries associated with the day.
     */
    public Collection<FoodEntry> getAllFoodEntries() {
        return this.foodEntries;
    }

    /**
     * Removes the food entry from the entry.
     * @param foodEntry The food entry to be removed.
     * @return True if the operation was successful.
     */
    public boolean removeFoodEntry(FoodEntry foodEntry) {
        return foodEntries.remove(foodEntry);
    }

    /**
     * Adds an exercise entry to the day.
     * @param exerciseEntry The exercise that occurred.
     * @return True if the operation was successful.
     */
    public boolean addExerciseEntry(ExerciseEntry exerciseEntry) {
        return this.exerciseEntries.add(exerciseEntry);
    }

    /**
     * Returns a list of all exercise entries that match the name provided.
     * @param name The name of the exercise that is associated with the exercise entry.
     * @return All entries in on the day that contain the name of the exercise.
     * This can return a collection of 0 items if no exercise entries are present.
     */
    public Collection<ExerciseEntry> getExerciseEntries(String name) {
        ArrayList<ExerciseEntry> retList = new ArrayList<>();
        for (ExerciseEntry entry : exerciseEntries) {
            if (entry.getExercise().getName().equals(name)) {
                retList.add(entry);
            }
        }
        return retList;
    }

    /**
     * Removes the exercise entry from the entry.
     * @param exerciseEntry The entry to be removed.
     * @return True if the operation was successful.
     */
    public boolean removeExerciseEntry(ExerciseEntry exerciseEntry) {
        return this.exerciseEntries.remove(exerciseEntry);
    }

    /**
     * Returns all of the exercise entries associated with the entry.
     * @return All of the exercise entries associated with the entry.
     * Can return a list of 0 items if there are no exercise entries associated with the day.
     */
    public Collection<ExerciseEntry> getAllExerciseEntries() {
        return this.exerciseEntries;
    }

    /**
     * Returns the calories consumed on the day.
     * @return The calories consumed on the day.
     */
    public double getCaloriesConsumed() {
        double calsConsumed = 0.0;
        for (FoodEntry meal : foodEntries) {
            calsConsumed += meal.getFood().getCalories() * meal.getServings();
        }
        return calsConsumed;
    }

    /**
     * Returns the protein consumed on the day.
     * @return The protein consumed on the day.
     */
    public double getProtienConsumed() {
        double proteinConsumed = 0.0;
        for (FoodEntry meal : foodEntries) {
            proteinConsumed += meal.getFood().getProtein() * meal.getServings();
        }
        return proteinConsumed;
    }

    /**
     * Returns the fat consumed on the day.
     * @return The fat consumed on the day.
     */
    public double getFatConsumed() {
        double fatConsumed = 0.0;
        for (FoodEntry meal : foodEntries) {
            fatConsumed += meal.getFood().getFat() * meal.getServings();
        }
        return fatConsumed;
    }

    /**
     * Returns the carbs consumed on the day.
     * @return The carbs consumed on the day.
     */
    public double getCarbsConsumed() {
        double carbsConsumed = 0.0;
        for (FoodEntry meal : foodEntries) {
            carbsConsumed += meal.getFood().getCarbs() * meal.getServings();
        }
        return carbsConsumed;
    }

    /**
     * Returns All associated entries in this object as csv.
     * If an entry does not exist it will not be added.
     * @return All associated entries in this object as csv.
     */
    public String getCSV(){
        StringBuilder sb = new StringBuilder();

        if (weightEntry != null) {
            sb.append(this.getDateAsCSV())
                    .append("w")
                    .append(",")
                    .append(this.weightEntry.getWeight())
                    .append(System.lineSeparator());
        }

        if (calorieEntry != null) {
            sb.append(this.getDateAsCSV())
                    .append("c")
                    .append(",")
                    .append(this.getCalorieEntry().getCalories())
                    .append(System.lineSeparator());
        }

        for (FoodEntry entry : foodEntries) {
            sb.append(this.getDateAsCSV())
                    .append("f")
                    .append(",")
                    .append(entry.getFood().getName())
                    .append(",")
                    .append(entry.getServings())
                    .append(System.lineSeparator());
        }

        for (ExerciseEntry entry : exerciseEntries) {
            sb.append(this.getDateAsCSV())
                    .append("e")
                    .append(",")
                    .append(entry.getExercise().getName())
                    .append(",")
                    .append(entry.getDuration())
                    .append(System.lineSeparator());
        }
        return sb.toString().trim(); //need to remove trailing return character.
    }

    /**
     * A method to assist with getting the date as a string.
     * @return The date as csv with a trailing comma.
     */
    private String getDateAsCSV(){
        StringBuilder sb = new StringBuilder()
                .append(date.get(Calendar.YEAR))
                .append(",")
                .append(date.get(Calendar.MONTH))
                .append(",")
                .append(date.get(Calendar.DAY_OF_MONTH))
                .append(",");
        return sb.toString();
    }

    /**
     * Returns the date of the entry as a string.
     * @return The date of the entry as a string.
     */
    public String getDateString() {
        return date.get(Calendar.MONTH)+"-"+date.get(Calendar.DAY_OF_MONTH)+"-"+date.get(Calendar.YEAR);
    }

    /**
     * Sets the date of the entry.
     * @param date The date to associated with the entry.
     */
    public void setDate(GregorianCalendar date) {
        if (date != null) {
            this.date = date;
        }
    }

    /**
     * Sets the food entries.
     * @param foodEntries A list of food entries that will be contained in the class.
     */
    public void setFoodEntries(ArrayList<FoodEntry> foodEntries) {
        if (foodEntries != null) {
            this.foodEntries = foodEntries;
        }
    }

    /**
     * Sets the exercise entries.
     * @param exerciseEntries A list of exercise entries that will be contained in the class.
     */
    public void setExerciseEntries(ArrayList<ExerciseEntry> exerciseEntries) {
        if (exerciseEntries != null) {
            this.exerciseEntries = exerciseEntries;
        }
    }
}
