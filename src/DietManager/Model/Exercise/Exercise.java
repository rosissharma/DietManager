package DietManager.Model.Exercise;

import DietManager.Model.ICSVable;

import java.security.InvalidParameterException;

/**
 * This class is a representation of a concrete exercise object.
 */
public class Exercise implements ICSVable {

    private String name;
    private double calories;

    /**
     * Constructs a Exercise object with the required parameters.
     *
     * @param name The name of the exercise.
     * @param calories The amount of calories burned by a 100lbs person in 1 hour.
     * @throws InvalidParameterException Thrown in the event that an invalid parameter is passed.
     */
    public Exercise(String name, double calories) throws InvalidParameterException {

        if ((name != null) && (name.length() > 0)) {
            this.name = name;
        } else {
            throw new InvalidParameterException("Invalid exercise name.");
        }

        if (calories >=0) {
            this.calories = calories;
        } else {
            throw new InvalidParameterException("Invalid exercise calorie value");
        }
    }

    /**
     * Returns the exercise name.
     *
     * @return The exercise name.
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the name of the exercise.
     * @param name The new name of the exercise.
     */
    public void setName(String name) {
        if ((name != null) && (name.length() > 0)) {
            this.name = name;
        }
    }

    /**
     * Returns the amount of calories the exercise burns by a 100lbs person in 1 hour.
     *
     * @return The amount of calories the exercise burns by a 100lbs person in 1 hour.
     */
    public double getCalories() {
        return calories;
    }

    /**
     * This will set the calories of the exercise. Calories is defined as the amount of
     * calories that a 100lbs person will burn in 1 hr.
     * @param calories The calories.
     */
    public void setCalories(double calories) {
        if (calories >=0) {
            this.calories = calories;
        }
    }

    /**
     * Returns a string that is a CSV representation of the object.
     *
     * @return A string that is a CSV representation of the object.
     */
    public String getCSV(){
        StringBuilder sb = new StringBuilder("e")
                .append(",")
                .append(this.name)
                .append(",")
                .append(this.calories);
        return sb.toString();
    }
}
