package DietManager.Model.Entry;

import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * CalorieEntry
 * Class to provide the string to retrieve the calories
 * @version 03-23-2019
 */

public class CalorieEntry {

    private double calories;

    /**
     * Creates a new Calorie Entry.
     * @param calories The calories associate with the entry.
     * @throws InvalidParameterException Thrown in the case of a null or invalid value.
     */
    public CalorieEntry(double calories) throws InvalidParameterException {
        if (calories >= 0) {
            this.calories = calories;
        } else {
            throw new InvalidParameterException("Invalid calories in calorie entry.");
        }
    }
    public CalorieEntry(){}

    /**
     * Returns the calories associated with the entry.
     * @return The calories associated with the entry.
     */
    public double getCalories() {
        return this.calories;
    }

    /**
     * Sets the calories associated with the entry.
     * @param calories The calories to be associated with the entry.
     */
    public void setCalories(double calories) {
        if (calories >=0) {
            this.calories = calories;
        }
    }
}
