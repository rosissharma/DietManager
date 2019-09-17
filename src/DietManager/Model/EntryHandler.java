package DietManager.Model;

import DietManager.Model.Entry.*;
import DietManager.Model.Exercise.Exercise;
import DietManager.Model.Food.Food;
import DietManager.Model.Util.IOHandler;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;

/**
 * This class loads the contents of log.csv into a calorie entry, weight entry, and food entry into various data structures.
 * The models can be retrieved from the data structures by querying EntryHandler with their unique Date key.
 */
public class EntryHandler extends Observable {

    private File file;
    private IOHandler io;
    private EntryFactory factory;
    private FoodHandler foodHandler;
    private ExerciseHandler exerciseHandler;
    private TreeMap<GregorianCalendar, Entry> entryStorage;

    /**
     * Creates an EntryHandler which depends on an IOHandler, FoodHandler, and ExerciseHandler
     * @param io The IOHandler responsible for reading and writing a log file.
     * @param foodHandler The FoodHandler responsible for storing food and recipe items.
     * @param exerciseHandler The ExerciseHandler responsible for storing exercises.
     */
    public EntryHandler(IOHandler io,FoodHandler foodHandler,ExerciseHandler exerciseHandler) {
        this.io = io;
        this.factory = new EntryFactory();
        this.foodHandler = foodHandler;
        this.exerciseHandler = exerciseHandler;
        this.entryStorage = new TreeMap<>();
    }

    /**
     * Loads food Entry objects from a provided csv formatted file.
     * @param file The file that will be read and used to create our entry objects.
     * @throws InvalidParameterException This is thrown if the provided log file contains errors.
     * @throws IOException This is thrown if the provided file cannot be read from.
     */
    public void load(File file) throws InvalidParameterException, IOException {

        try {
            ArrayList<String> rawEntries = this.io.readFromFile(file);
            for (String rawEntry : rawEntries) {
                try {
                    Entry entry = this.factory.makeEntry(rawEntry);
                    if (entry != null) {
                        this.addIfNotPresent(entry);
                    }
                } catch (InvalidParameterException ipe) {
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
     * Saves entry objects to a specified file.
     * @param file The file that the food items will be written to.
     * @throws IOException Thrown in the instance that the file provided cannot be written to.
     */
    public void save(File file) throws IOException {
        try {
            file.delete();
            this.io.writeToFile(getAllCSVable(),file);
        } catch (IOException ioe) {
            throw ioe;
        }
    }

    /**
     * Saves entry objects to a specified file.
     * @throws IOException Thrown in the instance that the file provided cannot be written to.
     */
    public void save() throws IOException {
        if (this.file == null) {
           return;
        }

        try {
            this.file.delete();
            this.io.writeToFile(getAllCSVable(),this.file);
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
     * Adds the entry even if there is one present. This can cause data lose. This can be unsafe.
     * @param entry The entry to be added.
     * @return True if the operation was successful.
     */
    public boolean addOverwrite(Entry entry) {
        this.entryStorage.put(entry.getDate(),entry);
        this.massNotify();
        return this.hasEntry(entry);
    }

    /**
     * Adds the entry provided only if it would not overwrite an existing entry. This is considered safe.
     * @param entry The entry to add.
     * @return Returns true if the operation was successful. Returns false if the operation
     * was not successful, or if the entry provided would have overwritten an existing entry.
     */
    public boolean addIfNotPresent(Entry entry) {
        boolean retVal = false;
        if (!this.entryStorage.containsKey(entry.getDate())) {
            this.entryStorage.put(entry.getDate(),entry);
            retVal = true;
        }
        this.massNotify();
        return retVal;
    }

    /**
     * Adds the entry even if there is one present. This can cause data lose. This can be unsafe.
     * @param rawCSV An entry as a CSV string.
     * @return True if operation was successful
     */
    public boolean addOverwrite(String rawCSV){
        Entry entry = this.factory.makeEntry(rawCSV);
        this.entryStorage.put(entry.getDate(),entry);
        this.massNotify();
        return this.hasEntry(entry);
    }

    /**
     * Adds the entry provided only if it would not overwrite an existing entry. This is considered safe.
     * @param rawCSV An entry as a CSV string.
     * @return Returns true if the operation was successful. Returns false if the operation
     * was not successful, or if the entry provided would have overwritten an existing entry.
     */
    public boolean addIfNotPresent(String rawCSV) {
        boolean retVal = false;

        Entry entry = this.factory.makeEntry(rawCSV);

        if (!this.entryStorage.containsKey(entry.getDate())) {
            this.entryStorage.put(entry.getDate(),entry);
            retVal = true;
        }
        this.massNotify();
        return retVal;
    }

    /**
     * Returns a unordered collection all of the entries that is in the model.
     * This does not remove the entry from the model. Data duplication is possible, be smart here.
     * @return All of the entry objects that are in the entry storage.
     */
    public Collection<Entry> getAll() {
        return new ArrayList<>(this.entryStorage.values());
    }

    /**
     * Returns an unordered list of all of the entries.
     * @return an unordered list of all of the entries.
     */
    Collection<ICSVable> getAllCSVable() {
        return new ArrayList<>(this.entryStorage.values());
    }

    /**
     * Returns the entry from the model.
     * @param entry The entry to be returned.
     * @return The entry provided.
     */
    public Entry get(Entry entry) {
        return this.entryStorage.get(entry.getDate());
    }

    /**
     * Returns the entry from the model.
     * @param date The entry to be returned.
     * @return The entry provided.
     */
    public Entry get(GregorianCalendar date) {
        return this.entryStorage.get(date);
    }

    public Entry get(String date) {

        String[] splitDate = date.split("-");

        int day = Integer.parseInt(splitDate[0]);
        int month = Integer.parseInt(splitDate[1]);
        int year = Integer.parseInt(splitDate[2]);

        return entryStorage.get(getCal(year,month,day));

    }

    /**
     * Test if an entry is contained in the model.
     * @param entry The entry to test for existence.
     * @return True if the entry exists.
     */
    public boolean hasEntry(Entry entry) {
        return this.entryStorage.containsKey(entry.getDate());
    }

    /**
     * Test if an entry is contained in the model.
     * @param date The date to test for entry existence.
     * @return True if the entry exists.
     */
    public boolean hasEntry(GregorianCalendar date) {
        return this.entryStorage.containsKey(date);
    }

    /**
     * Removes a specific entry from the model.
     * @param date The specific date of the entry to be removed.
     * @return True if the operation was successful.
     */
    public boolean delete(GregorianCalendar date) {
        this.entryStorage.remove(date);
        this.massNotify();
        return !this.hasEntry(date);
    }

    /**
     * Updates an entire entry
     * @param date the entry date to remove.
     * @param newEntry the updated entry
     */
    public boolean update(GregorianCalendar date,Entry newEntry){
        this.entryStorage.remove(date);
        this.entryStorage.put(date,newEntry);
        this.massNotify();
        return this.hasEntry(newEntry);
    }

    /**
     * Removes all entries on a specific day.
     * @param date The date on which all entries will be removed.
     */
    public boolean deleteAllEntriesOnDay(GregorianCalendar date) {
        this.entryStorage.remove(date);
        this.massNotify();
        return hasEntry(date);
    }

    /**
     * Computes how many calories were consumed on a specific day.
     * @param year The year of the day to be queried.
     * @param month The month of the day to be queried.
     * @param day_of_month The day of the month of the day to be queried.
     * @return The calories consume on a specific day. Will return 0 if no calories were consumed.
     */
    public double caloriesConsumedOnDay(int year, int month, int day_of_month) {
        return this.caloriesConsumedOnDay(getCal(year,month,day_of_month));
    }

    /**
     * Computes how many calories were consumed on a specific day.
     * @param date The date to be queried.
     * @return The calories consume on a specific day. Will return 0 if no calories were consumed.
     */
    public double caloriesConsumedOnDay(GregorianCalendar date) {
        Collection<FoodEntry> foodEntries = entryStorage.get(date).getAllFoodEntries();
        double calories = 0.0;
        for (FoodEntry entry : foodEntries) {
            calories += entry.getTotalCalories();
        }
        return calories;
    }

    /**
     * Computes how much protein was consumed on a specific day.
     * @param year The year of the day to be queried.
     * @param month The month of the day to be queried.
     * @param day_of_month The day of the month of the day to be queried.
     * @return The protein consume on a specific day. Will return 0 if no calories were consumed.
     */
    public double proteinConsumedOnDay(int year, int month, int day_of_month) {
        return this.proteinConsumedOnDay(getCal(year,month,day_of_month));
    }

    /**
     * Computes how much protein was consumed on a specific day.
     * @param date The date to be queried.
     * @return The protein consume on a specific day. Will return 0 if no calories were consumed.
     */
    public double proteinConsumedOnDay(GregorianCalendar date) {
        Collection<FoodEntry> foodEntries = entryStorage.get(date).getAllFoodEntries();
        double protein = 0.0;
        for (FoodEntry entry : foodEntries) {
            protein += entry.getTotalProtein();
        }
        return protein;
    }

    /**
     * Computes how many carbs were consumed on a specific day.
     * @param year The year of the day to be queried.
     * @param month The month of the day to be queried.
     * @param day_of_month The day of the month of the day to be queried.
     * @return The carbs consumed on a specific day. Will return 0 if no calories were consumed.
     */
    public double carbsConsumedOnDay(int year, int month, int day_of_month) {
        return this.carbsConsumedOnDay(getCal(year,month,day_of_month));
    }

    /**
     * Computes how many carbs were consumed on a specific day.
     * @param date The date to be queried.
     * @return The carbs consumed on a specific day. Will return 0 if no calories were consumed.
     */
    public double carbsConsumedOnDay(GregorianCalendar date) {
        Collection<FoodEntry> foodEntries = entryStorage.get(date).getAllFoodEntries();
        double protein = 0.0;
        for (FoodEntry entry : foodEntries) {
            protein += entry.getTotalCarbs();
        }
        return protein;
    }

    /**
     * Computes how much fat consumed on a specific day.
     * @param year The year of the day to be queried.
     * @param month The month of the day to be queried.
     * @param day_of_month The day of the month of the day to be queried.
     * @return How much fat was consume on a specific day. Will return 0 if no calories were consumed.
     */
    public double fatConsumedOnDay(int year, int month, int day_of_month) {
        return this.fatConsumedOnDay(getCal(year,month,day_of_month));
    }

    /**
     * Computes how much fat consumed on a specific day.
     * @param date The date to be queried.
     * @return How much fat was consumed on a specific day. Will return 0 if no calories were consumed.
     */
    public double fatConsumedOnDay(GregorianCalendar date) {
        Collection<FoodEntry> foodEntries = entryStorage.get(date).getAllFoodEntries();
        double protein = 0.0;
        for (FoodEntry entry : foodEntries) {
            protein += entry.getTotalCarbs();
        }
        return protein;
    }

    /**
     * Checks if the a calorie goal was a achieved on a specific day
     * @param year The year of the day to be queried.
     * @param month The month of the day to be queried.
     * @param day_of_month The day of the month of the day to be queried.
     * @return overUnder the amount of calories that were over or under the calorie goal
     */
    public double goalCheck(int year, int month, int day_of_month) {
        return this.goalCheck(getCal(year,month,day_of_month));
    }

    /**
     * Checks if the a calorie goal was a achieved on a specific day
     * @param date The date on which all entries will be removed.
     * @return overUnder the amount of calories that were over or under the calorie goal
     */
    public double goalCheck(GregorianCalendar date){
        // The over/under of calories for calorie goal.
        double overUnder;
        // retrieves entry on given date.
        Entry entry = entryStorage.get(date);
        // The calorie goal listed in the Entry.
        double calorieGoal;
        // Accumulates calories.
        double calories = 0;

        // Check for that calorie entry exists.
        if (entry.getCalorieEntry()!= null && entry.getWeightEntry() != null) {
            calorieGoal = entry.getCalorieEntry().getCalories();

            // Foods consumed on given date
            Collection<FoodEntry> foodsConsumed = entry.getAllFoodEntries();
            // Exercises performed on given date
            Collection<ExerciseEntry> exerciseRoutines = entry.getAllExerciseEntries();

            // Totals calories for foods consumed on given date.
            for (FoodEntry foodEntry : foodsConsumed) {
                calories += foodEntry.getTotalCalories();
            }

            // Subtracts calories for each exercise based on exercise calorie burn.
            for (ExerciseEntry exerciseEntry : exerciseRoutines) {
                double caloriesPerHour = exerciseEntry.getCaloriesFromExercise();
                double fractionalWeight = entry.getWeightEntry().getWeight()/100;
                double fractionalHours = exerciseEntry.getDuration()/60;
                double calorieBurn = caloriesPerHour * fractionalWeight * fractionalHours;
                calories -= calorieBurn;
            }

            // Check for negative calories.
            if (calories < 0) {
                overUnder = calorieGoal += calories;
            } else {
                overUnder = calorieGoal -= calories;
            }
        } else {
            overUnder = 0;
        }
        return Math.round(overUnder);
    }

    /**
     * This is a helper method to update all observers.
     */
    private void massNotify(){
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * This is a helper method for this class. It can instantiate calendars for a given day.
     * @param year The year of the calendar.
     * @param month The month of the calendar.
     * @param day_of_month The day of the month for the calendar.
     * @return The calendar specified.
     */
    public GregorianCalendar getCal(int year, int month, int day_of_month) {
        GregorianCalendar calendar = new GregorianCalendar();

        //THIS IS VERY IMPORTANT!
        calendar.clear();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day_of_month);

        return calendar;
    }

    /**
     * This is used to instantiate Entry items of various types.
     * It is necessary to keep this logic separate from the the handler itself, but within scope of the DataAccessLayer.
     * It needs to be within scope of the dal so that it can query the dal to know if a BasicFood or Recipe
     * is in the dal first before it can instantiate a FoodEntry containing a BasicFood/Recipe.
     */
    class EntryFactory {

        /**
         * Create a new Entry object based on specified Type
         *
         * @param csv The raw csv of an entry;
         * @return Entry
         */
        Entry makeEntry(String csv) {
            Entry entry = null;

            String[] entryDetails = csv.split(",");

            switch (entryDetails[3]) {
                case "w":
                    try {
                        //Get the date.
                        int yyyy = Integer.parseInt(entryDetails[0]);
                        int MM = Integer.parseInt(entryDetails[1]);
                        int dd = Integer.parseInt(entryDetails[2]);
                        //Get the weight.
                        double weight = Double.parseDouble(entryDetails[4]);

                        GregorianCalendar date = getCal(yyyy,MM,dd);

                        if (hasEntry(date)) {
                            entry = get(date);
                            entry.setWeightEntry(new WeightEntry(weight));
                        } else {
                            entry = new Entry(date);
                            entry.setWeightEntry(new WeightEntry(weight));
                        }

                    } catch (NumberFormatException nfe) {
                        throw new NumberFormatException("Weight entry contained malformed data.");
                    }
                    break;

                case "c":
                    try {
                        //Get the date.
                        int yyyy = Integer.parseInt(entryDetails[0]);
                        int MM = Integer.parseInt(entryDetails[1]);
                        int dd = Integer.parseInt(entryDetails[2]);
                        //Get the calories.
                        double calories = Double.parseDouble(entryDetails[4]);

                        GregorianCalendar date = getCal(yyyy,MM,dd);

                        if (hasEntry(date)) {
                            entry = get(date);
                            entry.setCalorieEntry(new CalorieEntry(calories));
                        } else {
                            entry = new Entry(date);
                            entry.setCalorieEntry(new CalorieEntry(calories));
                        }

                    } catch (NumberFormatException nfe) {
                        throw new NumberFormatException("Weight entry contained malformed data.");
                    }
                    break;

                case "f":
                    try {
                        //Get the date.
                        int yyyy = Integer.parseInt(entryDetails[0]);
                        int MM = Integer.parseInt(entryDetails[1]);
                        int dd = Integer.parseInt(entryDetails[2]);

                        //Get the food and servings associated.
                        String rawFood = entryDetails[4];
                        double servings = Double.parseDouble(entryDetails[5]);

                        GregorianCalendar date = getCal(yyyy,MM,dd);

                        if (foodHandler.hasFood(rawFood)) {
                            if (hasEntry(date)) {
                                Food food = foodHandler.get(rawFood);
                                entry = get(date);
                                entry.addFoodEntry(new FoodEntry(food,servings));
                            } else {
                                Food food = foodHandler.get(rawFood);
                                entry = new Entry(date);
                                entry.addFoodEntry(new FoodEntry(food,servings));
                            }
                        }

                    } catch (NumberFormatException nfe) {
                        throw new NumberFormatException("Weight entry contained malformed data.");
                    }
                    break;

                case "e":
                    try {
                        //Get the date.
                        int yyyy = Integer.parseInt(entryDetails[0]);
                        int MM = Integer.parseInt(entryDetails[1]);
                        int dd = Integer.parseInt(entryDetails[2]);

                        //Get the food and servings associated.
                        String rawExercise = entryDetails[4];
                        double duration = Double.parseDouble(entryDetails[5]);

                        GregorianCalendar date = getCal(yyyy,MM,dd);

                        if (exerciseHandler.hasExercise(rawExercise)) {
                            if (hasEntry(date)) {
                                Exercise exercise = exerciseHandler.get(rawExercise);
                                entry = get(date);
                                entry.addExerciseEntry(new ExerciseEntry(exercise, duration));
                            } else {
                                Exercise exercise = exerciseHandler.get(rawExercise);
                                entry = new Entry(date);
                                entry.addExerciseEntry(new ExerciseEntry(exercise, duration));
                            }
                        }

                    }catch(Exception e) {
                        throw new InvalidParameterException("Entry contained malformed data.");
                    }
            }
            return entry;
        }
    }
}
