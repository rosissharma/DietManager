package DietManager.Controller;

import DietManager.Model.Entry.*;
import DietManager.Model.EntryHandler;
import DietManager.Model.Exercise.Exercise;
import DietManager.Model.ExerciseHandler;
import DietManager.Model.Food.Food;
import DietManager.Model.FoodHandler;

import java.security.InvalidParameterException;
import java.util.*;

public class TextEntryController {
    private Scanner scan = new Scanner(System.in);
    private FoodHandler foodHandler;
    private ExerciseHandler exerciseHandler;
    private EntryHandler entryHandler;

    /**
     * Constructor for TextEntryController. Setup food, exercise, and entry handlers
     *
     * @param foodHandler the food handler needed to access foods in the model
     * @param exerciseHandler the exercise handler needed to access exercises in the model
     * @param entryHandler the entry handler needed to access entries in the model
     */
    public TextEntryController(FoodHandler foodHandler, ExerciseHandler exerciseHandler, EntryHandler entryHandler){
        this.foodHandler = foodHandler;
        this.exerciseHandler = exerciseHandler;
        this.entryHandler = entryHandler;
    }

    /**
     * Create a new entry with calorie and weight goal
     */
    public void create(){
        String date = null;
        double calorieGoal = 0;
        double weight = 0;
        HashMap<String,Double> foods = new HashMap<>();
        HashMap<String,Double> exercises = new HashMap<>();
        System.out.println("\nPlease Enter a Date(mm-dd-yyyy):");
        date = scan.nextLine();
        System.out.println("Please Enter a Calorie Goal:");
        calorieGoal = Double.parseDouble(scan.nextLine());
        System.out.println("Please Enter Your Weight For " + date + ":" );
        weight = Double.parseDouble(scan.nextLine());
        Entry entry = processEntry(date,calorieGoal,weight);
        handleFood(foods);
        entry = processFoods(entry,foods);
        handleExercise(exercises);
        processExercises(entry,exercises);
        if(entryHandler.addIfNotPresent(entry)){
            System.out.println("Entry Successfully Added!");
        }else{
            System.out.println("There was an Error Creating your Entry. Entry was not added");
        }

    }

    /**
     * Retrieve an entry from a specific date and view it
     */
    public void view(){

        System.out.println("\nEnter a Date to Search(mm-dd-yyyy):");
        String date = scan.next();
        String[] splitDate = date.split("-");
        int yyyy = Integer.parseInt(splitDate[2]);
        int mm = Integer.parseInt(splitDate[1]);
        int dd = Integer.parseInt(splitDate[0]);
        Entry entry = entryHandler.get(entryHandler.getCal(yyyy,mm,dd));
        if(entry != null){
            System.out.println("Entry Date: " + entry.getDateString());
            System.out.println("Calorie Goal: " + entry.getCalorieEntry().getCalories());
            System.out.println("Weight: " + entry.getWeightEntry().getWeight());
            System.out.println("\nFoods Consumed:");
            Collection<FoodEntry> foods = entry.getAllFoodEntries();
            for(FoodEntry food: foods){
                System.out.println(food.getFood().getName() + " " + food.getServings() + " Servings");
            }
            Collection<ExerciseEntry> exercises = entry.getAllExerciseEntries();
            for(ExerciseEntry exercise: exercises){
                System.out.println(exercise.getExercise().getName() + " " + exercise.getDuration() + " Minutes");
            }
        }else{
            System.out.println("Entry on " + date + " Was Not Found. Would You Like to Try Again?(y/n):");
            switch(scan.next()){
                case"y":
                    view();
                    break;
                default:
                    prompt();
                    break;
            }
        }
    }

    /**
     * Present the user a list of entries from a certain day and update the one selected by them.
     *
     */
    public void updateEntry(){
        String response = null;

        viewAll();
        System.out.println("Please select the date you want to update:");
        response = scan.nextLine();
        Collection<Entry> allEntries= entryHandler.getAll();

        int index = Integer.parseInt(response) - 1;
        ArrayList<Entry> entryList = new ArrayList<>(allEntries);
        Entry userEntryToDelete = entryList.get(index);

        String[] expandedEntry = userEntryToDelete.getCSV().split(",");
        String year = expandedEntry[0];
        String month = expandedEntry[2];
        String day = expandedEntry[1];
        GregorianCalendar dateOfEntry = new GregorianCalendar();
        dateOfEntry.clear();
        try{
            dateOfEntry.set(Calendar.YEAR, Integer.parseInt(year));
            dateOfEntry.set(Calendar.MONTH, Integer.parseInt(month));
            dateOfEntry.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        } catch(NumberFormatException ex){
            System.out.println("There was an issue with your date selected: " + ex);
        }

        StringBuilder weightEntry = new StringBuilder();
        System.out.println("Please add a weight goal for this day: ");
        response = scan.nextLine();
        weightEntry.append(year).append(",").append(month).append(",").append(day).append(",").append("w").append(",").append(response);

        StringBuilder calorieEntry = new StringBuilder();
        System.out.println("Please add a calorie goal for this day:");
        response = scan.nextLine();
        calorieEntry.append(year).append(",").append(month).append(",").append(day).append(",").append("c").append(",").append(response);


        try {
            entryHandler.delete(dateOfEntry);
            entryHandler.addIfNotPresent(calorieEntry.toString());
            entryHandler.addIfNotPresent(weightEntry.toString());
            System.out.println("Entry updated succesfully!");
        } catch (InvalidParameterException ex){
            System.out.println("Oops, something went wrong while updating your entry.");
        }

        boolean addFood = true;
        while (addFood) {
            System.out.println("Add a food? (y/n)");
            response = scan.nextLine();
            while (!response.toLowerCase().equals("y") && !response.toLowerCase().equals("n")) {
                System.out.println("Please enter a valid option.");
                response = scan.nextLine();
            }

            /*build a food entry*/
            if (response.equals("y")) {
                StringBuilder food = new StringBuilder();
                food.append(year).append(",").append(month).append(",").append(day).append(",").append("f").append(",");
                System.out.println("Please enter the food you would like to add:");
                response = scan.nextLine();
                food.append(response).append(",");
                System.out.println("How Many servings?");
                Double servings = null;
                try {
                    servings = Double.parseDouble(scan.nextLine());
                } catch (NumberFormatException ex){
                    System.out.println("Bad number");
                }
                food.append(servings).toString();

                try {
                    entryHandler.addIfNotPresent(food.toString());
                    System.out.println("Food added successfully");
                } catch (NullPointerException ex) {
                    System.out.println("Sorry, this food does not exist.");
                }
            } else {
                addFood = false;
            }
        }


        boolean addExercise = true;
        while (addExercise) {
            System.out.println("Add an Exercise? (y/n)");
            response = scan.nextLine();
            while (!response.toLowerCase().equals("y") && !response.toLowerCase().equals("n")) {
                System.out.println("Please enter a valid option.");
                response = scan.nextLine();
            }

            /*build an exercise entry*/
            if (response.equals("y")) {
                StringBuilder exercise = new StringBuilder();
                exercise.append(year).append(",").append(month).append(",").append(day).append(",").append("e").append(",");
                System.out.println("Please enter the exercise you would like to add:");
                response = scan.nextLine();
                exercise.append(response).append(",");
                Integer mins = null;
                System.out.println("How many minutes?");
                try {
                    mins = Integer.parseInt(scan.nextLine());
                } catch (NumberFormatException ex){
                    System.out.println("Invalid time.");
                }

                exercise.append(mins.toString());

                try {
                    entryHandler.addIfNotPresent(exercise.toString());
                    System.out.println("Exercise added successfully");
                } catch (NullPointerException ex) {
                    System.out.println("Sorry, this exercise does not exist.");
                }
            } else {
                addExercise = false;
            }
        }
    }

    /**
     * Delete the entry on a date specified by the user
     */
    public void delete(){
        ArrayList<Entry> entries = new ArrayList<>(entryHandler.getAll());
        viewAll();
        System.out.println("\nPlease Select an Entry to Delete:");
        Entry entry = entries.get(scan.nextInt()-1);
        if(entryHandler.delete(entry.getDate())){
            System.out.println("Entry Successfully Deleted!");
        }else{
            System.out.println("There was an Error Deleting your Entry. Entry was not deleted");
        }
    }

    /**
     * Prompt the user with the default options for entries
     */
    public void prompt(){
        System.out.println("\n1. View All Entry Dates");
        System.out.println("2. Add Entry");
        System.out.println("3. Update Entry");
        System.out.println("4. Delete Entry");
        System.out.println("5. Entry Search");
        System.out.println("0. Back");
        System.out.println("Select an Operation:");
        select(scan.nextLine());
    }

    /**
     * Switch statement used to determine which methods to call based upon user in[put at the default prompt
     *
     * @param selection the command selected by the user
     */
    public void select(String selection){
        switch(selection){
            case "1":
                viewAll();
                prompt();
                break;
            case "2":
                create();
                break;
            case "3":
                updateEntry();
                break;
            case "4":
                delete();
                break;
            case "5":
                view();
                break;
            case "0":
                break;
            default:
                System.out.println("Invalid Selection. Try Again.");
                prompt();
                break;
        }
    }
    /**
     * View all entries
     */
    public void viewAll(){
        ArrayList<Entry> entries = new ArrayList<>(entryHandler.getAll());
        for(int i = 0; i < entries.size(); i++){
            Entry entry = entries.get(i);
            System.out.println(i + 1 + ". " + entry.getDateString());
        }
    }

    /**
     * Handle adding food interaction.
     *
     * @param foods a map of all foods to add the newly added one to
     */
    public void handleFood(HashMap<String,Double> foods){
        System.out.println("\nWould You Like to Add a Food Entry?(y/n):");
        String addFood = scan.nextLine();
        String food = null;
        double servings = 0;
        while(addFood.equalsIgnoreCase("y")){
            System.out.println("Please Enter the Name of the Food or Recipe:");
            food = scan.nextLine();
            System.out.println("Please Enter the Number of Servings:");
            servings = Double.parseDouble(scan.nextLine());
            foods.put(food,servings);
            System.out.println("Would You Like to Add Another Food or Recipe?(y/n):");
            addFood = scan.nextLine();
        }
    }

    /**
     * Handles adding exercise interactions
     *
     * @param exercises a map of all exercises to add the newley added one to
     */
    public void handleExercise(HashMap<String,Double> exercises){
        System.out.println("\nWould You Like to Add an Exercise Entry?(y/n):");
        String addExercise = scan.next();

        while(addExercise.equalsIgnoreCase("y")) {
            System.out.println("Please Enter the Name of an Exercise:");
            String name = scan.next();
            System.out.println("Please Enter the Duration in Minutes:");
            double duration = scan.nextDouble();
            exercises.put(name,duration);
            System.out.println("Would You Like to Add Another Exercise?(y/n):");
            addExercise = scan.next();

        }
    }

    /**
     * Ensures that the foods added by the user exist before entering them
     *
     * @param entry the entry to be added
     * @param foods the map of foods
     */
    public Entry processFoods(Entry entry,HashMap<String,Double> foods){

        Set<HashMap.Entry<String,Double>> entrySet = foods.entrySet();
        for(HashMap.Entry<String,Double> food : entrySet){
            Food foodConsumed = null;
            foodConsumed = foodHandler.get(food.getKey());
            if(foodConsumed != null) {
                FoodEntry newEntry = new FoodEntry(foodConsumed, food.getValue());
                entry.addFoodEntry(newEntry);
            }else{
                System.out.println("Food does not Exist Skipping");
            }
        }
        return entry;
    }

    /**
     * Ensures that the foods added by the user exist before entering them
     *
     * @param entry the entry to be added
     * @param exercises the map of exercises
     */
    public Entry processExercises(Entry entry,HashMap<String,Double> exercises){

        ArrayList<ExerciseEntry> foodEntries = new ArrayList<>();
        Set<HashMap.Entry<String,Double>> entrySet = exercises.entrySet();
        for(HashMap.Entry<String,Double> exercise : entrySet){
            Exercise exercisePerformed = null;
            exercisePerformed= exerciseHandler.get(exercise.getKey());
            if(exercisePerformed != null) {
                ExerciseEntry newEntry = new ExerciseEntry(exercisePerformed, exercise.getValue());
                entry.addExerciseEntry(newEntry);
            }else{
                System.out.println("Exercise does not Exist Skipping");
            }
        }
        return entry;
    }

    /**
     * Process the addition of a new entry
     *
     * @param date the date for a new entry
     * @param calorieGoal the calorie goal for a new entry
     * @param weight the weight goal for a new entry
     *
     * @return the newly added entry
     */
    public Entry processEntry(String date,double calorieGoal,double weight){

        Entry entry = null;

        String[] splitDate = date.split("-");
        int yyyy = Integer.parseInt(splitDate[2]);
        int dd = Integer.parseInt(splitDate[1]);
        int mm= Integer.parseInt(splitDate[0]);
        entry = new Entry(entryHandler.getCal(yyyy,mm,dd));
        CalorieEntry calorieEntry = new CalorieEntry(calorieGoal);
        entry.setCalorieEntry(calorieEntry);
        WeightEntry weightEntry = new WeightEntry(weight);
        entry.setWeightEntry(weightEntry);

        return entry;
    }
}
