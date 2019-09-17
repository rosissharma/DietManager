package DietManager.View;

import DietManager.Model.EntryHandler;
import DietManager.Model.ExerciseHandler;
import DietManager.Model.Food.Food;
import DietManager.Model.FoodHandler;
import DietManager.View.FXComponents.*;
import DietManager.Controller.EntryController;
import DietManager.Controller.ExerciseController;
import DietManager.Controller.FoodController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;


import java.io.File;
import java.io.IOException;

/**
* The main view for the DietManager application.
* Creates initial view with a ViewButtonPane and Menu bar.
*/
public class JavaFXUI extends Application {

    private static FoodHandler foodHandler;
    private static ExerciseHandler exerciseHandler;
    private static EntryHandler entryHandler;
    private static FoodTable foodTable;
    private static LogTable logTable;
    private static ExerciseTable exerciseTable;
    private static EntryController entryController;
    private static Menu entryMenu;
    private static MenuItem addEntry;
    private static MenuItem deleteEntry;
    private static MenuItem updateEntry;
    private static BorderPane main;
    private static ViewButtonPane buttonPane;
    private static Graph graph;

    /**
     * Constructor for the JavaFXUI
     *
     * @param foodHandler the food handler used throughout the program
     * @param exerciseHandler the exercise handler used throughout the program
     * @param entryHandler the entry handler used throughout the program
     */
    public JavaFXUI(FoodHandler foodHandler,ExerciseHandler exerciseHandler,EntryHandler entryHandler){
        this.foodHandler = foodHandler;
        this.exerciseHandler = exerciseHandler;
        this.entryHandler = entryHandler;
    }

    // DO NOT DELETE THIS!!!
    // The default application constructor contains a special init, that javafx needs.
    public JavaFXUI(){}

    /**
     * Inherited method from application that starts the UI
     */
    public void start(Stage stage) {
        main = new BorderPane();
        entryController = new EntryController(this);
        foodTable = new FoodTable(foodHandler);
        logTable = new LogTable(entryHandler,entryController);
        exerciseTable = new ExerciseTable(exerciseHandler);
        buttonPane = new ViewButtonPane(this);
        graph = new Graph(entryHandler);

        /*Menu Bar setup*/
        MenuBar menuBar = new MenuBar();

        /*File/Save menu*/
        Menu menuFile = new Menu("File");
        MenuItem save = new MenuItem("Save");
        save.setOnAction(e -> {
            try{
                if (foodHandler.hasFile()) {
                    foodHandler.save();
                }
                if (exerciseHandler.hasFile()) {
                    exerciseHandler.save();
                }
                if (entryHandler.hasFile()) {
                    entryHandler.save();
                }
                new AlertModal("Success", "Save successful!");
            } catch(IOException ex){
                new AlertModal("Error", "Could not save!");
            }
        });
        menuFile.getItems().addAll(save);

        /*Food menu - create menu items and set actions*/
        Menu menuFood = new Menu("Food");
        MenuItem addFood = new MenuItem("Add Food");
        addFood.setOnAction(new FoodController(foodHandler, this, "add"));
        MenuItem updateFood = new MenuItem("Update Food");
        updateFood.setOnAction(new FoodController(foodHandler, this, "update"));
        MenuItem deleteFood = new MenuItem("Delete Food");
        deleteFood.setOnAction(new FoodController(foodHandler, this, "delete"));
        menuFood.getItems().addAll(addFood, updateFood, deleteFood);

        /*Entry menu -  create menu items and set actions*/
        entryMenu = new Menu("Entries");
        addEntry = new MenuItem("Add Entry");
        addEntry.setOnAction(entryController);
        updateEntry = new MenuItem("Update Entry");
        updateEntry.setOnAction(entryController);
        deleteEntry = new MenuItem("Delete Entry");
        deleteEntry.setOnAction(entryController);
        entryMenu.getItems().addAll(addEntry,updateEntry,deleteEntry);

        /*Exercise menu -  create menu items and set actions*/
        Menu menuExercise = new Menu("Exercise");
        MenuItem addExer = new MenuItem("Add Exercise");
        addExer.setOnAction(new ExerciseController(exerciseHandler, this, "add"));
        MenuItem updateExer = new MenuItem("Update Exercise");
        updateExer.setOnAction(new ExerciseController(exerciseHandler, this, "update"));
        MenuItem deleteExer = new MenuItem("Delete Exercise");
        deleteExer.setOnAction(new ExerciseController(exerciseHandler, this, "delete"));
        menuExercise.getItems().addAll(addExer,updateExer,deleteExer);
        menuBar.getMenus().addAll(menuFile,menuFood, entryMenu, menuExercise);//add all menus

        /*Append to the display*/
        VBox vbox = new VBox(menuBar);
        main.setTop(menuBar);
        main.setCenter(graph);
        main.setBottom(buttonPane);
        Scene scene = new Scene(main);
        stage.setTitle("Diet Manager Bits & Bytes");
        stage.setWidth(518);
        stage.setHeight(550);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Launches the JavaFXUI
     */
    public void launchApplication(){
        launch();
    }

    /**
     * Returns the entry handler
     *
     * @return the entry handler
     */
    public EntryHandler getEntryHandler() {
        return entryHandler;
    }

    /**
     * Returns the main border pane
     *
     * @return the main border pane
     */
    public BorderPane getMain() {
        return main;
    }

    /**
     * Returns the food table
     *
     * @return the food table
     */
    public FoodTable getFoodTable() { return foodTable; }

    /**
     * Returns the log table
     *
     * @return the log table
     */
    public LogTable getLogTable() { return logTable; }

    /**
     * Returns the exercise table
     *
     * @return the exercise table
     */
    public ExerciseTable getExerciseTable() { return exerciseTable; }

    /**
     * Returns the add entry menu item
     *
     * @return the add entry menu item
     */
    public MenuItem getAddEntry() {
        return addEntry;
    }

    /**
     * Returns the delete entry menu item
     *
     * @return the delete entry menu item
     */
    public MenuItem getDeleteEntry() {
        return deleteEntry;
    }

    /**
     * Returns the update entry menu item
     *
     * @return the update entry menu item
     */
    public MenuItem getUpdateEntry() {
        return updateEntry;
    }

    public static Graph getGraph() {
        return graph;
    }
}
