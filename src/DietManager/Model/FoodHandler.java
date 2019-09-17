package DietManager.Model;

import DietManager.Model.Food.BasicFood;
import DietManager.Model.Food.Food;
import DietManager.Model.Food.Recipe;
import DietManager.Model.Util.IOHandler;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Observable;


/**
 * This class handles all interaction with the dal for food objects.
 * Food can be loaded, updated, deleted or added here.
 */
public class FoodHandler extends Observable {

    private File file;
    private IOHandler io;
    private FoodFactory factory;
    private HashMap<String, Food> foodStorage;

    /**
     * Creates an object that can be used to interact with the food that is in the dal.
     * @param io An IOHandler that can be used to read and write food to various CSV files.
     */
    public FoodHandler(IOHandler io) {
        this.io = io;
        this.factory = new FoodFactory();
        this.foodStorage = new HashMap<>();
    }

    /**
     * Loads food objects from a provided csv formatted file.
     * @param file The file that will be read and used to create our food objects.
     * @throws InvalidParameterException This is thrown if the provided log file contains errors.
     * @throws IOException This is thrown if the provided file cannot be read from.
     */
    public void load(File file) throws InvalidParameterException, IOException {
        try {
            ArrayList<String> rawFoods = io.readFromFile(file);
            for(String rawFood : rawFoods) {
                try {
                    Food food = factory.makeFood(rawFood);
                    if( food != null) {
                        add(food);
                    }
                } catch (InvalidParameterException ipe) {
                    throw ipe;
                }
            }
            if (file != null) {
                this.file = file;
            }
        } catch (IOException io) {
            throw io;
        }
    }

    /**
     * Saves food objects to a specified file.
     * @param file The file that the food items will be written to.
     * @throws IOException Thrown in the instance that the file provided cannot be written to.
     */
    public void save(File file) throws IOException {
        try {
            file.delete();
            io.writeToFile(getAllCSVAble(), file);
        } catch (IOException ioe) {
            throw ioe;
        }
    }

    /**
     * Saves food objects to a specified file.
     * @throws IOException Thrown in the instance that the file provided cannot be written to.
     */
    public void save() throws IOException {
        if (this.file == null) {
            return;
        }
        try {
            this.file.delete();
            io.writeToFile(getAllCSVAble(), this.file);
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
     * Adds a food object to the dal.
     * @param food A food object to add.
     * @return True if the operation was successful.
     */
    public boolean add(Food food) {
        foodStorage.putIfAbsent(food.getName(), food);
        this.massNotify();
        return hasFood(food);
    }

    /**
     * Adds a food object to the foodHandler
     * @param rawCSV a food CSV string
     * @return True if the operation was successful.
     */
    public boolean add(String rawCSV) {
        Food food = factory.makeFood(rawCSV);
        foodStorage.put(food.getName(),food);
        this.massNotify();
        return hasFood(food);
    }

    /**
     * Returns a unordered collection all of the food that is in the dal.
     * This does not remove the food from the model. Data duplication is possible, be smart here.
     * @return All of the food objects that are in the food storage.
     */
    public Collection<Food> getAll() {
        return foodStorage.values();
    }

    /**
     * Returns a ordered collection all of the food that is in the dal with the BasicFoods first and Recipes last.
     * This does not remove the food from the model. Data duplication is possible, be smart here.
     * @return All of the food objects that are in the food storage ordered by BasicFoods then Recipes.
     */
    Collection<ICSVable> getAllCSVAble() {
        ArrayList<Food> foods = new ArrayList<>(foodStorage.values());
        ArrayList<ICSVable> output = new ArrayList<>();

        //Put foods in first
        for(Food food: foods) {
            if (food instanceof BasicFood) {
                output.add(food);
            }
        }

        //Put recipes at the end.
        for(Food food: foods) {
            if (food instanceof Recipe) {
                output.add(food);
            }
        }
        return output;
    }

    /**
     * Returns a food from dal as determined by the food object.
     * @param food The food to return from the dal.
     * @return The food object in the dal. Returns null if not found.
     */
    public Food get(Food food) {
        return foodStorage.get(food.getName());
    }

    /**
     * Returns the food object by the name of the food.
     * @param food The name of the food.
     * @return The food object in the storage. Returns null if not found.
     */
    public Food get(String food) {
        return foodStorage.get(food);
    }

    /**
     * Determine if we have the food in the food model already.
     * @param food A food object to check.
     * @return True if the food is present in the dal.
     */
    public boolean hasFood(Food food) {
        return foodStorage.containsKey(food.getName());
    }

    /**
     * Determine if we have the food in the food model already.
     * @param food The food name. Must be precise.
     * @return Returns true if the food object was in the model.
     */
    public boolean hasFood(String food) {
        return foodStorage.containsKey(food);
    }

    /**
     * Deletes Food from the model.
     * @param food The food name that needs to be removed from the model.
     * @return True if the operation was successful.
     */
    public boolean delete(String food) {
        foodStorage.remove(food);
        this.massNotify();
        return !hasFood(food);
    }

    /**
     * Deletes Food from the model.
     * @param food The food name that needs to be removed from the model.
     * @return True if the operation was successful.
     */
    public boolean delete(Food food) {
        foodStorage.remove(food.getName());
        this.massNotify();
        return !hasFood(food);
    }

    /**
     * Updates a food
     * @param name name of the food to be updated
     * @param newFood updated food CSV string
     * @return success true if update is successful
     */
    public boolean update(String name,String newFood){
        boolean success = delete(name) && add(newFood);
        this.massNotify();
        return success;
    }

    /**
     * This is a helper method to update all observers.
     */
    private void massNotify(){
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * This is used to instantiate food items of various types.
     * It is necessary to keep this logic separate from the the handler itself, but within scope of the DataAccessLayer.
     * It needs to be within scope of the dal so that it can query the dal to know if a BasicFood is in the
     * dal first before it can instantiate a Recipe composed of the BasicFood.
     */
    class FoodFactory {

        /**
         * This will take raw csv and make Food type objects.
         * @param csv Raw csv, most likely read in from a file.
         * @return A Food object.
         * @throws InvalidParameterException Thrown in the event that an invalid value is found.
         */
        Food makeFood(String csv) throws InvalidParameterException {
            Food food = null;
            String[] foodDetails = csv.split(",");

            switch (foodDetails[0]) {
                case "r":
                    try {
                        ArrayList<Pair<Food,Double>> recipeItems = new ArrayList<>();

                        for (int i=2; i < foodDetails.length; i+=2) { //Start at one and get every pair.

                            if (hasFood(foodDetails[i])) {
                                Pair<Food, Double> recipeItem = new Pair<>(
                                        get(foodDetails[i]),
                                        Double.parseDouble(foodDetails[i + 1])    // Servings
                                );
                                recipeItems.add(recipeItem);
                            } else {
                               throw new InvalidParameterException("Food required for recipe does not exist.");
                            }
                        }
                        food = new Recipe(foodDetails[1], recipeItems);

                    } catch (InvalidParameterException ipe) {
                        throw ipe;
                    } catch (ArrayIndexOutOfBoundsException aiob) {
                        throw new InvalidParameterException("Recipe file contained invalid line.");
                    } catch (NumberFormatException nfe) {
                        throw new InvalidParameterException("Invalid value found in recipe file.");
                    }
                    break;

                case "b":
                    try {
                        food = new BasicFood(
                                foodDetails[1],                            // Food name.
                                Double.parseDouble(foodDetails[2]),        // calories.
                                Double.parseDouble(foodDetails[3]),        // fat.
                                Double.parseDouble(foodDetails[4]),        // carbs.
                                Double.parseDouble(foodDetails[5])         // protein.
                        );
                    } catch (InvalidParameterException ipe) {
                        throw ipe;
                    } catch (ArrayIndexOutOfBoundsException aiob) {
                        throw new InvalidParameterException("Basic foods file contained invalid line.");
                    } catch (NumberFormatException nfe) {
                        throw new InvalidParameterException("Invalid value found in food file.");
                    }
                    break;

                default:
                    throw new InvalidParameterException("Food file contained invalid line.");
            }
            return food;
        }
    }
}
