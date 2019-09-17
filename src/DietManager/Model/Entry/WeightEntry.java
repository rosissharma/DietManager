package DietManager.Model.Entry;

import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * WeightEntry
 * Class to provide the string for retriving your current weight
 */
public class WeightEntry {

    private double weight;

    /**
     * Create a Weight Entry.
     * @param weight The weight associated with the entry.
     * @throws InvalidParameterException Thrown in the case of null or invalid value.
     */
    public WeightEntry(double weight) throws InvalidParameterException {

        if (weight >= 0) {
            this.weight = weight;
        } else {
            throw new InvalidParameterException("Invalid weight in weight entry.");
        }
    }

    public WeightEntry(){}

    /**
     * Returns the weight associated with the entry.
     * @return The weight associated with the entry.
     */
    public double getWeight() {
        return this.weight;
    }

    /**
     * Sets the weight of the user for the entry.
     * @param weight The weight of the user.
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }
}

