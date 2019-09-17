package DietManager.Model;

import DietManager.Model.Exercise.Exercise;
import DietManager.Model.Util.IOHandler;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Observable;

/**
 * This class handles all interaction with the dal for exercise objects.
 * Exercises can be loaded, updated, deleted, or added here.
 */
public class ExerciseHandler extends Observable {

    private File file;
    private IOHandler io;
    private ExerciseFactory factory;
    private HashMap<String, Exercise> exerciseStorage;

    /**
     * Creates and object that can be used to interact with the exercises that are in the dal.
     * @param io An IOHandler that can be use dto read and write food to various csv files.
     */
    public ExerciseHandler(IOHandler io){
        this.io = io;
        this.factory = new ExerciseFactory();
        exerciseStorage = new HashMap<>();
    }

    /**
     * Loads exercise object from a provided csv formatted file into the dal.
     * @param file The file that will be read and used to create our exercise objects.
     * @throws InvalidParameterException This is thrown if the provided log file contains errors.
     * @throws IOException This is thrown if the provided file cannot be read from.
     */
    public void load(File file) throws InvalidParameterException, IOException {
        try{
            ArrayList<String> rawExercises = io.readFromFile(file);
            for(String rawExercise : rawExercises) {
                try {
                    Exercise exercise = factory.makeExercise(rawExercise);
                    if(exercise != null) {
                        add(exercise);
                    }
                } catch (InvalidParameterException ipe){
                    throw ipe;
                }
            }
            if (file != null) {
                this.file = file;
            }
        } catch (IOException ioe) {
            throw ioe;
        }
    }

    /**
     * Saves exercise objects from the exercise dal to a specified file.
     * @param file The file that the exercise items will be written to.
     * @throws IOException Thrown in the instance that the file privided cannot be written to.
     */
    public void save(File file) throws IOException {
        try {
            file.delete();
            io.writeToFile(getAllCSVAble(),file);
        } catch (IOException ioe) {
            throw ioe;
        }
    }

    /**
     * Saves exercise objects from the exercise dal to a specified file.
     * @throws IOException Thrown in the instance that the file privided cannot be written to.
     */
    public void save() throws IOException {
        if (this.file == null) {
            return;
        }
        try {
            this.file.delete();
            io.writeToFile(getAllCSVAble(),this.file);
        } catch (IOException ioe) {
            throw ioe;
        }
    }

    /**
     * Returns if the file has been set.
     * @return True if the file has been set.
     */
    public boolean hasFile(){
        return this.file == null;
    }

    /**
     * Adds a exercise object to the dal.
     * @param exercise A exercise object to add.
     * @return True if the operation was successful.
     */
    public boolean add(Exercise exercise) {
        exerciseStorage.putIfAbsent(exercise.getName().trim(),exercise);
        this.massNotify();
        return hasExercise(exercise);
    }

    /**
     * Adds a exercise object to the dal.
     * @param rawCSV a exercise CSV string to be added
     * @return True if the operation was successful.
     */
    public boolean add(String rawCSV) {
        Exercise exercise = factory.makeExercise(rawCSV);
        exerciseStorage.putIfAbsent(exercise.getName().trim(),exercise);
        this.massNotify();
        return hasExercise(exercise);
    }

    /**
     * Returns an unordered collection of all of the exercises that are present in the dal.
     * This does not remove exercises from the model. Data duplication is possible, be smart here.
     * @return An unordered collection of all of the exercises that are present in the dal.
     */
    public Collection<Exercise> getAll() {
        return exerciseStorage.values();
    }

    /**
     * Returns an unordered collection of all of the exercises that are present in the dal as ICSVAble items.
     * This does not remove exercises from the model. Data duplication is possible, be smart here.
     * @return An unordered collection of all of the exercises that are present in the dal.
     */
    Collection<ICSVable> getAllCSVAble(){
        return new ArrayList<>(exerciseStorage.values());
    }

    /**
     * Returns a exercise item from the dal as determined by the exercise object.
     * @param exercise The exercise to return from the dal.
     * @return The exercise object in the dal. Returns null if not found.
     */
    public Exercise get(Exercise exercise) {
        return exerciseStorage.get(exercise.getName().trim());
    }

    /**
     * Returns a exercise item from the dal as determined by the exercise name.
     * @param exercise The exercise to return from the dal.
     * @return The exercise object in the dal. Returns null if not found.
     */
    public Exercise get(String exercise) {
        return exerciseStorage.get(exercise);
    }

    /**
     * Determine if we have the exercise in the dal already.
     * @param exercise The exercise to check.
     * @return True if the exercise is present in the dal.
     */
    public boolean hasExercise(Exercise exercise) {
        return exerciseStorage.containsKey(exercise.getName().trim());
    }

    /**
     * Determine if we have the exercise in the dal already.
     * @param exercise The exercise name. Must be precise.
     * @return True if the exercise is present in the dal.
     */
    public boolean hasExercise(String exercise) {
        return exerciseStorage.containsKey(exercise);
    }

    /**
     * Deletes the exercise from the dal.
     * @param exercise The exercise to be deleted from the dal.
     * @return True if the operation was successful.
     */
    public boolean delete(String exercise) {
        exerciseStorage.remove(exercise);
        this.massNotify();
        return !hasExercise(exercise);
    }

    /**
     * Deletes the exercise from the dal.
     * @param exercise The exercise to be deleted.
     * @return True if the operation was successful.
     */
    public boolean delete(Exercise exercise) {
        exerciseStorage.remove(exercise.getName());
        this.massNotify();
        return !hasExercise(exercise);
    }

    /**
     * Updates an exercise entry
     * @param name The name of the exercise to be updated
     * @param exerciseCSV updated exercise CSV string
     * @return Returns true if update is successful.
     */
    public boolean update(String name,String exerciseCSV){
        boolean success = this.delete(name) && this.add(exerciseCSV);
        this.massNotify();
        return success;
    }

    /**
     * Updates an exercise entry
     * @param exercise The exercise to be updated
     * @return Returns true if update is successful.
     */
    public boolean update(Exercise exercise){
        boolean success = delete(exercise) && add(exercise);
        this.massNotify();
        return success;
    }

    /**
     * This is a helper method to simplify updating all observers.
     */
    private void massNotify(){
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * This class is used to create exercise objects.
     * It is necessary to keep this logic separate from the handler itself, but within scope of the DataAccessLayer
     * It needs to be within scope of the dal so that it can query the dal to know if an Exercise object is in the
     * dal first before it can instantiate and add a Exercise object.
     */
    class ExerciseFactory {

        /**
         * This will take raw csv and make Exercise type objects.
         * @param csv Raw csc, most likely read in from a file.
         * @return A exercise object.
         * @throws InvalidParameterException Thrown in the event that an invalid value is found.
         */
        Exercise makeExercise(String csv) throws InvalidParameterException {
            Exercise exercise = null;
            String[] exerciseDetails = csv.split(",");

            // This switch is un-needed, but follows the pattern we have in other classes.
            switch (exerciseDetails[0]) {
                case "e":
                        try {
                            exercise = new Exercise(
                                    exerciseDetails[1],                     // Exercise name.
                                    Double.parseDouble(exerciseDetails[2])  // Calories per 100lbs/hr.
                            );
                        }catch (InvalidParameterException ipe) {
                            throw ipe;
                        } catch (ArrayIndexOutOfBoundsException aiob){
                            throw new InvalidParameterException("Exercise contained invalid length.");
                        } catch (NumberFormatException nfe) {
                            throw new InvalidParameterException("Invalid value found in exercise file");
                        }
                    break;
                default:
                    throw new InvalidParameterException("Exercise contained malformed data.");
            }
            return exercise;
        }
    }
}
