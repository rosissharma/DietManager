package DietManager.Controller;

import DietManager.Model.Entry.*;
import DietManager.Model.Exercise.Exercise;
import DietManager.Model.Food.Food;
import DietManager.View.FXComponents.*;
import DietManager.View.JavaFXUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 *Handle actions from the "Entry" menu items.
 *Handles user input and controls the view for crud actions on entry objects.
 */
public class EntryController extends Stage implements EventHandler<ActionEvent> {

    private JavaFXUI ui;

    /**
     * Class constructor. Setup the entrycontroller with the active view
     *
     * @param ui the active JaavaFXUI
     */
    public EntryController(JavaFXUI ui){
        this.ui = ui;
        this.initModality(Modality.APPLICATION_MODAL);
    }

    /**
    * Set the stage to be used by the modal window
    *
    * @param scene the scene to be added to the modal window
    * @param title the title to be added to the modal window
    */
    public void setStage(Scene scene, String title) {
        this.setTitle(title);
        this.setScene(scene);
        this.sizeToScene();
        this.showAndWait();
    }

    /**
     * Delete a entry object. Controls user input and Delete Entry view
     */
    public void selectOperation(Entry entry){
        ObservableList<String> options = FXCollections.observableArrayList();
        options.addAll("View","Update","Delete");
        ComboModal selectOperation = new ComboModal("Select Option","Select an Option",null,options);
        int[] returnValue = selectOperation.init();
        if(returnValue[0] == 0){
            viewEntry(entry);
        }else if(returnValue[0] == 1){
            updateEntry(entry);
        }else if(returnValue[0] == 2){
            deleteEntry(entry);
        }
    }

    /**
     *Determines which command was passed  in and switches accordingly
     *
     * @param e the ActionEvent fired
     */
    @Override
    public void handle(ActionEvent e) {
        if(e.getSource() == ui.getDeleteEntry()){
            deleteEntry(null);
        }else if(e.getSource() == ui.getUpdateEntry()){
            updateEntry(null);
        }else if(e.getSource() == ui.getAddEntry()){
            createEntry();
        }
    }

    /**
     * Display information for an Entry
     *
     */
    public void viewEntry(Entry tableEntry){
        Entry entry = null;
        if(tableEntry != null) {
            entry = tableEntry;
        }else{
            entry = selectEntry();
        }
        Entry selectedEntry = entry;
        Label createLabel = new Label("Entry for "+entry.getDateString());
        VBox layout = new VBox(10);
        createLabel.setFont(new Font("Arial", 20));
        Label caloriesLabel = new Label("Calorie Goal: ");
        caloriesLabel.setFont(new Font("Arial", 15));
        Label caloriesInput = new Label();
        caloriesInput.setText(String.valueOf(entry.getCalorieEntry().getCalories()) + " calories");
        Label weightLabel = new Label("Weight: ");
        weightLabel.setFont(new Font("Arial", 15));
        Label weightInput = new Label();
        weightInput.setText(String.valueOf(entry.getWeightEntry().getWeight())+" lbs");
        layout.getChildren().addAll(createLabel,caloriesLabel,caloriesInput,weightLabel,weightInput);
        ArrayList<FoodEntry> foodEntries = new ArrayList<>(entry.getAllFoodEntries());
        ComboBox<String> foodsConsumed = new ComboBox<>();
        if(foodEntries.size() > 0) {
            Label foodLabel = new Label("Food Entries");
            foodLabel.setFont(new Font("Arial", 15));
            for (FoodEntry foodEntry : foodEntries) {
                foodsConsumed.getItems().add(foodEntry.getServings() + " servings of " + foodEntry.getFood().getName());
            }
            layout.getChildren().addAll(foodLabel, foodsConsumed);
            foodsConsumed.getSelectionModel().selectFirst();
        }

        ArrayList<ExerciseEntry> exerciseEntries = new ArrayList<>(entry.getAllExerciseEntries());
        ComboBox<String> exercisesPerformed = new ComboBox<>();
        if(exerciseEntries.size() >0) {
            Label foodLabel = new Label("Food Entries");
            foodLabel.setFont(new Font("Arial", 15));
            for (ExerciseEntry exerciseEntry : exerciseEntries) {
                exercisesPerformed.getItems().add(exerciseEntry.getExercise().getName() + " for " + exerciseEntry.getDuration() + " minutes");
            }
            layout.getChildren().addAll(foodLabel, exercisesPerformed);
            exercisesPerformed.getSelectionModel().selectFirst();
        }

        Button submit = new Button("Close");
        submit.setOnAction(e->{
            this.close();
        });
        Button goalCheck = new Button("Goal Check");
        goalCheck.setOnAction(e->{
            double goal = ui.getEntryHandler().goalCheck(selectedEntry.getDate());
            if(goal ==  0){
                AlertModal alert = new AlertModal("Successful Goal", "The Calorie Goal of " + selectedEntry.getCalorieEntry().getCalories() + " was reached!");
            }else if(goal > 0){
                AlertModal alert = new AlertModal("Successful Goal", "The Calorie Goal of " + selectedEntry.getCalorieEntry().getCalories()+ " was reached with " + goal + " Calories to spare!");
            }else if(goal < 0){
                AlertModal alert = new AlertModal("Goal Failed", "The Calorie Goal of " + selectedEntry.getCalorieEntry().getCalories()+ " was exceeded by " + goal * -1);
            }
            this.close();
        });
        layout.getChildren().addAll(submit,goalCheck);
        layout.setPadding(new Insets(5, 5,5,5));
        Scene scene = new Scene(layout);
        setStage(scene,"View Entry");

    }

    /**
     * Create a new entry object. Controls user input and Add Entry view
     *
     */
    public void createEntry(){

        Entry entry = new Entry();
        DatePicker datePicker =  new DatePicker();
        Label createLabel = new Label("Create Entry");
        VBox layout = new VBox(10);
        createLabel.setFont(new Font("Arial", 20));
        Label caloriesLabel = new Label("Calorie Goal: ");
        caloriesLabel.setFont(new Font("Arial", 15));
        TextField caloriesInput = new TextField();
        Label weightLabel = new Label("Weight: ");
        weightLabel.setFont(new Font("Arial", 15));
        TextField weightInput = new TextField();
        layout.getChildren().addAll(createLabel,datePicker,caloriesLabel,caloriesInput,weightLabel,weightInput);
        ArrayList<FoodEntry> foodEntries = new ArrayList<>(entry.getAllFoodEntries());
        ComboBox<String> foodsConsumed = new ComboBox<>();
        Label foodLabel = new Label("Exercise Entries");
        foodLabel.setFont(new Font("Arial", 15));
        for (FoodEntry foodEntry : foodEntries) {
            foodsConsumed.getItems().add(foodEntry.getFood().getName());
        }
        layout.getChildren().addAll(foodLabel,foodsConsumed);
        foodsConsumed.getSelectionModel().selectFirst();


        ArrayList<ExerciseEntry> exerciseEntries = new ArrayList<>(entry.getAllExerciseEntries());
        ComboBox<String> exercisesPerformed = new ComboBox<>();
        Label exerciseLabel = new Label("Exercise Entries");
        exerciseLabel.setFont(new Font("Arial", 15));
        for (ExerciseEntry exerciseEntry : exerciseEntries) {
            exercisesPerformed.getItems().add(exerciseEntry.getExercise().getName());
        }
        layout.getChildren().addAll(exerciseLabel,exercisesPerformed);
        exercisesPerformed.getSelectionModel().selectFirst();

        Button addFood = createAddFoodButton(foodEntries,foodsConsumed);
        Button removeFood = createRemoveFoodButton(foodEntries,foodsConsumed);
        Button addExercise = createAddExerciseButton(exerciseEntries,exercisesPerformed);
        Button removeExercise = createRemoveExerciseButton(exerciseEntries,exercisesPerformed);
        Button submit = new Button("Submit");
        HBox hbox = new HBox();
        hbox.getChildren().addAll(addFood,removeFood,addExercise,removeExercise,submit);
        hbox.setPadding(new Insets(5, 5, 5, 5));
        hbox.setSpacing(20);
        submit.setOnAction(e->{
            try {
                LocalDate localDate = datePicker.getValue();
                Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
                Date date = Date.from(instant);
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                entry.setDate(calendar);
                entry.setCalorieEntry(new CalorieEntry(Double.parseDouble(caloriesInput.getText())));
                entry.setWeightEntry(new WeightEntry(Double.parseDouble(weightInput.getText())));
                entry.setFoodEntries(foodEntries);
                entry.setExerciseEntries(exerciseEntries);
                if(ui.getEntryHandler().addIfNotPresent(entry)){
                    AlertModal alert = new AlertModal("Entry Added","Entry Successfully Created!");
                    this.close();
                }else{
                    AlertModal alert = new AlertModal("Entry Error","An Error Occured While Creating the Entry.");
                }
            }catch(NumberFormatException nfe){
                AlertModal alert = new AlertModal("Input Error","Please Make Sure To Enter a Date,Calorie Goal and Weight.");
            }
        });

        layout.getChildren().addAll(hbox);
        layout.setPadding(new Insets(5, 5,5,5));
        Scene scene = new Scene(layout);
        setStage(scene,"Create Entry");
    }

    /**
     * Update entry object, controls user input and Update Entry view
     *
     */
    public void updateEntry(Entry tableEntry){

        Entry selectedEntry = null;
        if(tableEntry != null) {
            selectedEntry = tableEntry;
        }else{
            selectedEntry = selectEntry();
        }
        Entry entry = selectedEntry;
        VBox layout = new VBox(10);
        Label dateLabel = new Label("Entry for " + selectedEntry.getDateString());
        dateLabel.setFont(new Font("Arial", 20));
        Label caloriesLabel = new Label("Calorie Goal: ");
        caloriesLabel.setFont(new Font("Arial", 15));
        caloriesLabel.setAlignment(Pos.BASELINE_LEFT);
        TextField calories = new TextField();
        calories.setText(String.valueOf((int)selectedEntry.getCalorieEntry().getCalories()));
        calories.setAlignment(Pos.BASELINE_LEFT);
        Label weightLabel = new Label("Weight: ");
        weightLabel.setFont(new Font("Arial", 15));
        weightLabel.setAlignment(Pos.BASELINE_LEFT);
        TextField weight = new TextField();
        weight.setText(String.valueOf((int)selectedEntry.getWeightEntry().getWeight())+ " lbs");
        weight.setAlignment(Pos.BASELINE_LEFT);
        layout.getChildren().addAll(dateLabel,caloriesLabel,calories,weightLabel,weight);

        ArrayList<FoodEntry> foodEntries = new ArrayList<>(selectedEntry.getAllFoodEntries());
        ComboBox<String> foodsConsumed = new ComboBox<>();

        for (FoodEntry foodEntry : foodEntries) {
            foodsConsumed.getItems().add(foodEntry.getFood().getName());
        }
        layout.getChildren().addAll(new Label("Food Entries"),foodsConsumed);
        foodsConsumed.getSelectionModel().selectFirst();


        ArrayList<ExerciseEntry> exerciseEntries = new ArrayList<>(selectedEntry.getAllExerciseEntries());
        ComboBox<String> exercisesPerformed = new ComboBox<>();
        for (ExerciseEntry exerciseEntry : exerciseEntries) {
            exercisesPerformed.getItems().add(exerciseEntry.getExercise().getName());
        }
        layout.getChildren().addAll(new Label("Exercise Entries"),exercisesPerformed);
        exercisesPerformed.getSelectionModel().selectFirst();

        Button addFood = createAddFoodButton(foodEntries,foodsConsumed);
        Button removeFood = createRemoveFoodButton(foodEntries,foodsConsumed);
        Button addExercise = createAddExerciseButton(exerciseEntries,exercisesPerformed);
        Button removeExercise = createRemoveExerciseButton(exerciseEntries,exercisesPerformed);
        Button submit = new Button("Submit");
        HBox hbox = new HBox();
        hbox.getChildren().addAll(addFood,removeFood,addExercise,removeExercise,submit);
        hbox.setPadding(new Insets(5, 5, 5, 5));
        hbox.setSpacing(20);
        submit.setOnAction(e->{
            if(ui.getEntryHandler().update(entry.getDate(),entry)){
                AlertModal alert = new AlertModal("Entry Updated","Entry was succesffully updated!");
                this.close();
            }
            else{
                AlertModal alert = new AlertModal("Update Error","There was an error updating the entry.");
            }
        });

        layout.getChildren().addAll(hbox);
        layout.setPadding(new Insets(5, 5,5,5));
        Scene scene = new Scene(layout);
        setStage(scene,"Update Entry");
    }

    /**
     * Delete a entry object. Controls user input and Delete Entry view
     *
     */
    public void deleteEntry(Entry tableEntry){

        Entry entry = null;
        if(tableEntry != null) {
            entry = tableEntry;
        }else{
            entry = selectEntry();
        }
        AlertModal alert;
        if(ui.getEntryHandler().delete(entry.getDate())){
            alert = new AlertModal("Delete Entry", "Entry Successfully Deleted!");
        }
        else{
            alert = new AlertModal("Delete Entry", "There was an error deleting the Entry.");
        }

    }

    /**
    * A universal modal to be used when selecting entry dates.
    *
    * @return the entry from the date selected
    */
    public Entry selectEntry(){

        ObservableList<Entry> entries = ui.getLogTable().getData();
        ObservableList<String> stringEntries = FXCollections.observableArrayList();
        for(Entry entry : entries){
            stringEntries.add(entry.getDateString());
        }
        ComboModal selectDate = new ComboModal("Date Select","Select a Date:",null,stringEntries);
        return entries.get(selectDate.init()[0]);
    }

    /**
     * Creates a new food entry
     *
     * @return the FoodEntry that has been created
     */
    public FoodEntry createNewFoodEntry(){

        FoodEntry foodEntry = new FoodEntry();
        ObservableList<Food> foods = ui.getFoodTable().getData();
        ObservableList<String> foodStrings = FXCollections.observableArrayList();
        for(Food food : foods){
            foodStrings.add(food.getName());
        }
        ComboModal selectFood = new ComboModal("Food Select","Select a Food","Servings",foodStrings);
        int[] values = selectFood.init();
        foodEntry.setFood(foods.get(values[0]));
        foodEntry.setServings((double)values[1]);

        return foodEntry;
    }

    /**
     * Creates a new exercise entry
     *
     * @return the ExerciseEntry that has been created
     */
    public ExerciseEntry createNewExerciseEntry(){

        ExerciseEntry exerciseEntry = new ExerciseEntry();
        ObservableList<Exercise> exercises = ui.getExerciseTable().getData();
        ObservableList<String> exerciseStrings = FXCollections.observableArrayList();
        for(Exercise exercise : exercises){
            exerciseStrings.add(exercise.getName());
        }
        ComboModal selectExercise = new ComboModal("Exercise Select","Select an Exercise","Duration",exerciseStrings);
        int[] values = selectExercise.init();
        exerciseEntry.setExercise(exercises.get(values[0]));
        exerciseEntry.setDuration((double)values[1]);

        return exerciseEntry;
    }

    /**
     * Creates a button and handles the according events for adding an exercise. Helps with SOC.
     *
     * @return the button that has been created for adding an exercise
     */
    public Button createAddExerciseButton(ArrayList<ExerciseEntry> exerciseEntries,ComboBox<String> exercisesPerformed){
        Button addFood = new Button("Add Exercises");
        addFood.setOnAction(e-> {
            ExerciseEntry exerciseEntry = createNewExerciseEntry();
            exerciseEntries.add(exerciseEntry);
            exercisesPerformed.getItems().add(exerciseEntry.getExercise().getName());
            exercisesPerformed.getSelectionModel().selectFirst();
        });
        return addFood;
    }

    /**
     * Creates a button and handles the according events for adding a food. Helps with SOC.
     *
     * @return the button that has been created for adding a food
     */
    public Button createAddFoodButton(ArrayList<FoodEntry> foodEntries,ComboBox<String> foodConsumed){
        Button addFood = new Button("Add Food");
        addFood.setOnAction(e-> {
            FoodEntry foodEntry = createNewFoodEntry();
            foodEntries.add(foodEntry);
            foodConsumed.getItems().add(foodEntry.getFood().getName());
            foodConsumed.getSelectionModel().selectFirst();
        });
        return addFood;
    }

    /**
     * Creates a button and handles the according events for removing a food. Helps with SOC.
     *
     * @return the button that has been created for removing a food
     */
    public Button createRemoveFoodButton(ArrayList<FoodEntry> foodEntries,ComboBox<String> foodsConsumed){
        Button removeFood = new Button("Remove Food");
        removeFood.setOnAction(e-> {
            if(foodEntries.size() > 0) {
                int selectedIndex = foodsConsumed.getSelectionModel().getSelectedIndex();
                FoodEntry foodEntry = foodEntries.remove(selectedIndex);
                foodsConsumed.getItems().remove(selectedIndex);
                foodsConsumed.getSelectionModel().selectFirst();
                AlertModal alert = new AlertModal("Food Removed",foodEntry.getFood().getName()+" entry Successfully removed!");
            }else{
                AlertModal alert = new AlertModal("No Food", "There are no foods in this entry");
            }
        });
        return removeFood;
    }

    /**
     * Creates a button and handles the according events for removing aan exercise. Helps with SOC.
     *
     * @return the button that has been created for removing an exercise
     */
    public Button createRemoveExerciseButton(ArrayList<ExerciseEntry> exerciseEntries,ComboBox<String> exercisesPerformed){
        Button removeExercise = new Button("Remove Exercise");
        removeExercise.setOnAction(e-> {
            if(exerciseEntries.size() > 0) {
                int selectedIndex = exercisesPerformed.getSelectionModel().getSelectedIndex();
                ExerciseEntry exerciseEntry = exerciseEntries.remove(selectedIndex);
                exercisesPerformed.getItems().remove(selectedIndex);
                exercisesPerformed.getSelectionModel().selectFirst();
                AlertModal alert = new AlertModal("Exercise Removed",exerciseEntry.getExercise().getName()+" entry Successfully removed!");
            }else{
                AlertModal alert = new AlertModal("No Exercises", "There are no Exercises in this entry");
            }
        });
        return removeExercise;
    }
}
