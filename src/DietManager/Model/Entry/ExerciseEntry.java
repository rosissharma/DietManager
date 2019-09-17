package DietManager.Model.Entry;

import DietManager.Model.Exercise.Exercise;
import javafx.util.Pair;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * This class will represent an exercise that happens during a users entry.
 */
public class ExerciseEntry {

    private Exercise exercise;
    private double duration;

    /**
     * This will generate an exercise entry with the duration of the exercise.
     * @param exercise The exercise that is being preformed.
     * @param duration The duration of the exercise that is being preformed.
     * @throws InvalidParameterException Thrown in the event that the object was attempted to be instantiated with invalid values.
     */
    public ExerciseEntry(Exercise exercise, double duration) throws InvalidParameterException {

        if(exercise != null) {
            this.exercise = exercise;
        } else {
            throw new InvalidParameterException("Exercise is invalid in exercise entry.");
        }

        if (duration >= 0) {
            this.duration = duration;
        } else {
            throw new InvalidParameterException("Exercise duration in exercise entry was invalid.");
        }
    }
    public ExerciseEntry(){}

    /**
     * Returns the exercise.
     * @return The exercise.
     */
    public Exercise getExercise(){
        return this.exercise;
    }

    /**
     * Returns the duration of the exercise.
     * @return The duration of the exercise.
     */
    public double getDuration(){
        return this.duration;
    }

    /**
     * Returns The total number of calories that the exercise routine results in. This does not take into account the
     * weight of the user. Additional math will be needed to calculate that.
     * @return The total calories of the workout routine.
     */
    public double getCaloriesFromExercise() {
        return this.exercise.getCalories() * duration;
    }

    /**
     * Sets the exercise performed.
     * @param exercise The exercise being performed.
     */
    public void setExercise(Exercise exercise) {
        if (exercise != null) {
            this.exercise = exercise;
        }
    }

    /**
     * Sets the duration of the exercise.
     * @param duration The duration of the exercise.
     */
    public void setDuration(double duration) {
        if (duration >= 0) {
            this.duration = duration;
        }
    }
}
