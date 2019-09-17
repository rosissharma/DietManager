package DietManager.Default;


import DietManager.Controller.TextEntryController;
import DietManager.Controller.TextExerciseController;
import DietManager.Controller.TextFoodController;
import DietManager.Model.Entry.Entry;
import DietManager.Model.EntryHandler;
import DietManager.Model.Exercise.Exercise;
import DietManager.Model.ExerciseHandler;
import DietManager.Model.Food.Food;
import DietManager.Model.FoodHandler;
import DietManager.Model.Util.IOHandler;
import DietManager.View.*;

import java.io.File;
import java.io.IOException;

public class Main{

    // RUN AN END-TO-END INTEGRATION TEST OF THE MODEL.
    private static boolean E2E_MODEL_TEST = false;

    // RUN THE TEXT UI
    private static boolean RUN_TEXT_UI = false;

    private static IOHandler io;
    private static FoodHandler foodHandler;
    private static ExerciseHandler exerciseHandler;
    private static EntryHandler entryHandler;

    // File paths to be used in an IDE.
    private static String PATH = "./";
    // Log files
    private static String logIn = PATH+"log.csv";
    private static String logOut = PATH+"log.csv";
    // Food files
    private static String foodsIn = PATH+"foods.csv";
    private static String foodsOut = PATH+"foods.csv";
    // Exercise files
    private static String exerciseIn = PATH+"exercise.csv";
    private static String exerciseOut = PATH+"exercise.csv";


    public static void main(String[] args){

        for(String arg: args) {
            if (arg.equals("--e2e")) {
                E2E_MODEL_TEST = true;
            }
            if (arg.equals("--textui")) {
                RUN_TEXT_UI = true;
            }
            if (arg.equals("--dev-files")) {
                PATH = "./src/DietManager/Assets/TestAssets/";
                logIn = PATH+"log.csv";
                logOut = PATH+"logTRASH.csv";
                foodsIn = PATH+"foods.csv";
                foodsOut = PATH+"foodsTRASH.csv";
                exerciseIn = PATH+"exercise.csv";
                exerciseOut = PATH+"exerciseTRASH.csv";

            }
        }

        //INITIALIZE ALL THE THINGS!
        io = new IOHandler();
        foodHandler = new FoodHandler(io);
        exerciseHandler = new ExerciseHandler(io);
        entryHandler = new EntryHandler(io,foodHandler,exerciseHandler);

        // Add shutdown hook to program so that the data is written when the program exits.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                foodHandler.save(new File(foodsOut));
                exerciseHandler.save(new File(exerciseOut));
                entryHandler.save(new File(logOut));
            } catch (Exception E) {
                System.out.println("Could not save files. Reason: "+E.getMessage());
            }
        }));

        if(!E2E_MODEL_TEST) {
            try {
                foodHandler.load(new File(foodsIn));
                exerciseHandler.load(new File(exerciseIn));
                entryHandler.load(new File(logIn));
            }catch(Exception e){
                System.out.println(e.getMessage());
            }

            if(RUN_TEXT_UI) {
                TextFoodController textFoodController = new TextFoodController(foodHandler);
                TextExerciseController textExerciseController = new TextExerciseController(exerciseHandler);
                TextEntryController textEntryController = new TextEntryController(foodHandler, exerciseHandler, entryHandler);
                TextUI textUI = new TextUI(textFoodController, textExerciseController, textEntryController);
                new Thread(textUI).start();
            }

            JavaFXUI ui = new JavaFXUI(foodHandler,exerciseHandler,entryHandler);
            ui.launchApplication();
        } else {
            printSection();

            // Everything loads.
            loadFoods(foodsIn);
            loadExercises(exerciseIn);
            loadEntries(logIn);

            printSection();

            // Everything prints.
            printAllFoods();
            printAllEntries();
            printAllExercises();
            printCompositeRecipe();

            printSection();

            // Everything writes.
            writeAllFoods(foodsOut);
            writeAllEntries(logOut);
            writeAllExercises(exerciseOut);

            printSection();

            // Query the Model.
            printCaloriesConsumedOnDay(2016, 4, 9);
            printIfOverGoalOnDay(2016, 4, 9);
            printCaloriesConsumedOnDay(2016, 4, 10);
            printIfOverGoalOnDay(2016, 4, 10);
        }
    }


////////////////////////////////////////////////////////////////////////////////
// METHODS BELOW HERE ARE FOR INTERNAL TESTING PURPOSES ONLY!
////////////////////////////////////////////////////////////////////////////////

// LOAD ALL THE THINGS!
////////////////////////////////////////////////////////////////////////////////

    private static void loadFoods(String file) {
        //Load the foods.
        try {
            foodHandler.load(new File(file));
        } catch (Exception E) {
            System.out.println(E.getMessage());
        }
        System.out.println("Read foods: Success!");
    }

    private static void loadExercises(String file) {
        //Load the entries.
        try {
            exerciseHandler.load(new File(file));
        } catch (Exception E) {
            System.out.println(E.getMessage());
        }
        System.out.println("Read exercises: Success!");
    }

    private static void loadEntries(String file) {
        //Load the entries.
        try {
            entryHandler.load(new File(file));
        } catch (Exception E) {
            System.out.println(E.getMessage());
        }
        System.out.println("Read entries: Success!");
    }

// PRINT ALL THE THINGS!
////////////////////////////////////////////////////////////////////////////////

    private static void printAllFoods(){
        //Print the foods.
        System.out.println("\nPrint All Foods: ");
        for(Food food: foodHandler.getAll()){
            System.out.println("Food: "+food.getName());
            System.out.println("Food CSV: "+food.getCSV());
        }
    }

    private static void printAllEntries(){
        //Print the entries.
        System.out.println("\nPrint All Entries: ");
        for(Entry entry: entryHandler.getAll()){
            System.out.println("\nDate: " + entry.getDate().getTime().toString());
            System.out.println(entry.getCSV());
            //System.out.println(entryHandler.goalCheck(entry.getDate()));
        }
    }

    private static void printAllExercises(){
        //Print the foods.
        System.out.println("\nPrint All Exercises: ");
        for(Exercise exercise: exerciseHandler.getAll()){
            System.out.println("Exercise: "+exercise.getName());
            System.out.println("Exercise CSV: "+exercise.getCSV());
        }
    }

// PRINT "WEIRD" ITEMS!
////////////////////////////////////////////////////////////////////////////////

    private static void printCompositeRecipe(){
        //Print a composite recipe.
        System.out.println("\nBad Dinner Calories: " + foodHandler.get("Bad Dinner").getCalories());
    }

// WRITE ALL THE THINGS!
////////////////////////////////////////////////////////////////////////////////

    private static void writeAllFoods(String file){
        //Write the foods out.
        try {
            foodHandler.save(new File(file));
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        System.out.println("Write foods: Success!");
    }

    private static void writeAllEntries(String file){
        //Write the entries out.
        try {
            entryHandler.save(new File(file));
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        System.out.println("Write entries: Success!");
    }

    private static void writeAllExercises(String file){
        //Write the entries out.
        try {
            exerciseHandler.save(new File(file));
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        System.out.println("Write exercises: Success!");
    }

// ASK THE QUESTION!
////////////////////////////////////////////////////////////////////////////////

    private static void printCaloriesConsumedOnDay(int year, int month, int day) {
        //Print the total calories consumed on a single day.
        System.out.println();
        System.out.println("Calories on "+month+"/"+day+"/"+year+":" + entryHandler.caloriesConsumedOnDay(year,month,day));
    }

    private static void printIfOverGoalOnDay(int year, int month, int day){
        double overUnder = entryHandler.goalCheck(year,month,day);
        if (overUnder > 0) {
            System.out.println("User is under goal by: "+overUnder+" calories.");
        } else {
            System.out.println("User is over goal by: "+overUnder+" calories.");
        }
    }

    private static void printSection(){
        System.out.println("################################################################################");
    }
}