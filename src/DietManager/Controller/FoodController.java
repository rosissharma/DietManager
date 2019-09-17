package DietManager.Controller;

import DietManager.Model.Food.Food;
import DietManager.Model.FoodHandler;
import DietManager.View.FXComponents.*;
import DietManager.View.JavaFXUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.*;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * Handle actions from the "Food" menu items.
 * Handles user input and controls the view for crud actions on food objects.
 */
public class FoodController extends Stage implements EventHandler<ActionEvent> {

    private JavaFXUI ui;
    private FoodHandler foodHandler;
    private String command;
    /**
     * Constructor for the FoodController.
     *
     * @param foodHandler the food handler needed to access the model
     * @param ui the active JavaFXUI
     * @param command passed in from the view, dictates which action carry out
     */
    public FoodController(FoodHandler foodHandler, JavaFXUI ui, String command){
        this.ui = ui;
        this.foodHandler = foodHandler;
        this.command = command;
        this.initModality(Modality.APPLICATION_MODAL);
    }

    /**
     * Sets the stage for window.
     * @param scene The scene to have in the stage.
     * @param title The title of the window.
     */
    private void setStage(Scene scene, String title) {
        this.setTitle(title);
        this.setMinWidth(400);
        this.setScene(scene);
        this.showAndWait();
    }

    /**
     * Determines which command was passed  in and switches accordingly
     *
     * @param e the ActionEvent fired
     */
    @Override
    public void handle(ActionEvent e) {
        switch(command){

            case "add":
                this.setStage(this.createFood(), "Add Food");
                break;

            case "update":
                this.setStage(this.updateFood(), "Update Food");
                break;

            case "delete":
                this.setStage(this.deleteFood(), "Delete Food");
                break;
        }
    }

    /**
     * Create a new food object. Controls user input and Add Food view
     *
     * @return the scene to be presented to the user
     */
    public Scene createFood(){
        this.setHeight(200);//reset height

        VBox layout = new VBox(10);

        StringBuilder food = new StringBuilder();

        Label recipeOrFoodLabel = new Label("Select Type:");
        ComboBox<String> recipeOrFood = new ComboBox<>();
        recipeOrFood.getItems().addAll("Recipe", "Basic Food");
        recipeOrFood.setMaxWidth(200);
        Button recipeOrBasic = new Button("Select");

        recipeOrBasic.setOnAction(e ->{
            try{
                String rOrB = recipeOrFood.getValue();
                if(rOrB.equals("Recipe")){

                    /*Recipe creation*/
                    food.append("r");
                    layout.getChildren().removeAll(recipeOrFoodLabel, recipeOrFood, recipeOrBasic);
                    this.setHeight(300);

                    Label nameLabel = new Label("Recipe Name:");
                    TextField name = new TextField();
                    name.setMaxWidth(200);
                    Button submit = new Button("Submit");
                    Button addIngredient = new Button("Add another ingredient");
                    Label firstIng = new Label("Ingredient:");
                    TextField ingName = new TextField();
                    ingName.setMaxWidth(200);
                    Label serveLabel = new Label("Servings:");
                    TextField servings = new TextField();
                    servings.setMaxWidth(50);

                    /*add another ingredient*/
                    addIngredient.setOnAction(f -> {
                        Label ing = new Label("Ingredient:");
                        TextField nameField = new TextField();
                        nameField.setMaxWidth(200);
                        Label sLabel = new Label("Servings:");
                        TextField newS = new TextField();
                        newS.setMaxWidth(50);
                        this.setHeight(this.getHeight()+125);
                        layout.getChildren().add(layout.getChildren().size()-2, ing);
                        layout.getChildren().add(layout.getChildren().size()-2, nameField);
                        layout.getChildren().add(layout.getChildren().size()-2, sLabel);
                        layout.getChildren().add(layout.getChildren().size()-2, newS);
                    });

                    /*submit the new ingredient*/
                    submit.setOnAction(g -> {
                        /*validation*/
                        String rName = name.getText();
                        boolean valid = true;
                        if(!validate(true, rName)){
                            valid = false;
                        } else {
                            food.append(",").append(rName);
                            int j = 1;//counter for textfields
                            for (Node node : layout.getChildren()) {
                                boolean isString = false;
                                /*Skip name field as it has already been validated, validate all other fields*/
                                if (node instanceof TextField && !rName.equals(((TextField) node).getText())) {
                                    /*Check to see if we need to find a string or not*/
                                    if((j % 2) != 0){
                                        isString = true;
                                    }
                                    /*check to make sure its valid*/
                                    if (!validate(isString, ((TextField) node).getText())) {
                                        valid = false;
                                        food.setLength(0);
                                        break;
                                    }
                                    food.append(",").append(((TextField) node).getText());
                                    j++;
                                }
                            }
                        }

                        /*If everything is valid, build the food.*/
                        if (valid) {
                            try {
                                foodHandler.add(food.toString());
                                new AlertModal("Success", "Your food has been created successfully");
                                this.close();//close window after adding food
                            } catch (InvalidParameterException ex) {
                                /*reset the stringbuilder and make it a basic recipe again*/
                                food.setLength(0);
                                food.append("r");
                                new AlertModal("Error", ex.toString());
                            }
                        } else {
                            new AlertModal("Invalid entry","Invalid values. Please verify all attributes.");
                            food.setLength(0);
                        }
                    });
                    /*Append basic layout*/
                    layout.getChildren().addAll(nameLabel, name, firstIng, ingName, serveLabel, servings, addIngredient, submit);


                } else {
                    /*Basic food creation*/
                    food.append("b");

                    this.setHeight(600);
                    layout.getChildren().removeAll(recipeOrFoodLabel, recipeOrFood, recipeOrBasic);

                    Label foodName = new Label("Food Name:");
                    TextField nameField = new TextField();
                    nameField.setMaxWidth(200);
                    Label foodCal = new Label("Calories per serving:");
                    TextField calField = new TextField();
                    calField.setMaxWidth(200);
                    Label foodFat = new Label("Fat per serving:");
                    TextField fatField = new TextField();
                    fatField.setMaxWidth(200);
                    Label foodCarb = new Label("Carbs per serving:");
                    TextField carbField = new TextField();
                    carbField.setMaxWidth(200);
                    Label foodPro = new Label("Protein per serving:");
                    TextField proField = new TextField();
                    proField.setMaxWidth(200);

                    Button add = new Button("Add to Foods");
                    add.setOnAction(action -> {
                        /*This is where we build our new food from the text provided and create a new food as well*/

                        String name = nameField.getText();
                        String calories = calField.getText();
                        String fat = fatField.getText();
                        String carbs = carbField.getText();
                        String protein = proField.getText();
                        ArrayList<String> items = new ArrayList<>(Arrays.asList(calories, fat, carbs, protein));

                        /*Validate remaining items*/
                        boolean valid = true;
                        if(!validate(true, name)){
                            valid = false;
                        } else {
                            food.append(",").append(name);
                            /*If the name is valid continue to check the rest of the items.*/
                            for (String val : items) {
                                if (!validate(false, val)) {
                                    valid = false;
                                    food.setLength(0);
                                    break;
                                }
                                food.append(",").append(val);
                            }
                        }

                        /*If everything is valid, build the food.*/
                        if (valid) {
                            try {
                                foodHandler.add(food.toString());
                                new AlertModal("Success", "Your food has been created successfully");
                                this.close();//close window after adding food
                            } catch (InvalidParameterException ex) {
                                /*reset the stringbuilder and make it a basic recipe again*/
                                food.setLength(0);
                                food.append("b");
                                new AlertModal("Error", ex.toString());
                            }
                        } else {
                            new AlertModal("Invalid entry","Invalid values. Please verify all attributes.");
                            food.setLength(0);
                        }
                    });
                    /*Append basic food items to layout*/
                    layout.getChildren().addAll(foodName, nameField, foodCal, calField, foodFat, fatField, foodCarb, carbField, foodPro, proField, add);
                }
            } catch (NullPointerException ex) {
                //this is empty as an error will be thrown elsewhere that will handle this
            }
        });

        layout.getChildren().addAll(recipeOrFoodLabel, recipeOrFood, recipeOrBasic);
        layout.setAlignment(Pos.CENTER);

        return new Scene(layout);

    }

    /**
     * Update food object, controls user input and Update Food view
     *
     * @return the scene for updating a food
     */
    public Scene updateFood(){

        VBox layout = new VBox(10);
        this.setHeight(200);

        Label selectLabel = new Label("Select the food you would like to update:");
        ComboBox<String> foodsList = new ComboBox<>();

        /*Get all the foods that are saved and add them to a list*/
        Collection<Food> allFoods = foodHandler.getAll();
        for(Food food: allFoods){
            foodsList.getItems().add(food.getName());
        }

        Button select = new Button("Select");
        select.setOnAction(e -> {
            /*Update the selected food*/
            Food foodToUpdate = foodHandler.get(foodsList.getValue());
            /*Is it a recipe or basic food*/
            String[] rOrB = foodToUpdate.getCSV().split(",");

            if(rOrB[0].equals("b")){
                /*handle basic food first*/
                this.setHeight(500);
                layout.getChildren().removeAll(selectLabel, foodsList, select);
                Label nameLabel = new Label("Food Name:");
                TextField nameField = new TextField(foodToUpdate.getName());
                nameField.setMaxWidth(200);
                Label calLabel = new Label("Calories per Serving:");
                TextField calField = new TextField(String.valueOf(foodToUpdate.getCalories()));
                calField.setMaxWidth(200);
                Label fatLabel = new Label("Fat per Serving:");
                TextField fatField = new TextField(String.valueOf(foodToUpdate.getFat()));
                fatField.setMaxWidth(200);
                Label carbLabel = new Label("Carbs per Serving:");
                TextField carbField = new TextField(String.valueOf(foodToUpdate.getCarbs()));
                carbField.setMaxWidth(200);
                Label proLabel = new Label("Protein per Serving:");
                TextField proField = new TextField(String.valueOf(foodToUpdate.getProtein()));
                proField.setMaxWidth(200);

                Button update = new Button("Update Food");
                update.setOnAction(f -> {
                    StringBuilder food = new StringBuilder("b");
                    String name = nameField.getText();
                    String calories = calField.getText();
                    String fat = fatField.getText();
                    String carbs = carbField.getText();
                    String protein = proField.getText();
                    ArrayList<String> items = new ArrayList<>(Arrays.asList(calories, fat, carbs, protein));

                    boolean valid = true;
                    if(!validate(true, name)){
                        valid = false;
                    } else {
                        food.append(",").append(name);
                        for (String val : items) {
                            if (!validate(false, val)) {
                                valid = false;
                                food.setLength(0);
                                break;
                            }
                            food.append(",").append(val);
                        }
                    }

                    /*If everything is valid, build the food.*/
                    if (valid) {
                        try {
                            foodHandler.update(name, food.toString());
                            new AlertModal("Success", "Your food has been updated successfully");
                            this.close();//close window after adding food
                        } catch (InvalidParameterException ex) {
                            /*reset the stringbuilder and make it a basic recipe again*/
                            food.setLength(0);
                            food.append("b");
                            new AlertModal("Error", ex.toString());
                        }
                    } else {
                        new AlertModal("Invalid entry","Invalid values. Please verify all attributes.");
                        food.setLength(0);
                    }
                });

                layout.getChildren().addAll(nameLabel, nameField, calLabel, calField, fatLabel, fatField, carbLabel, carbField, proLabel, proField, update);
            } else {
                /*Update a recipe*/
                StringBuilder food = new StringBuilder("r");
                this.setHeight(700);
                layout.getChildren().removeAll(selectLabel, foodsList, select);
                Label nameLabel = new Label("Food Name:");
                TextField nameField = new TextField(foodToUpdate.getName());
                nameField.setMaxWidth(200);
                layout.getChildren().addAll(nameLabel, nameField);

                /*Append all the ingredients*/
                for(int i = 2; i < rOrB.length; i+=2){
                    Label ingLabel = new Label("Ingredient:");
                    TextField ingredient = new TextField(rOrB[i]);
                    ingredient.setMaxWidth(200);
                    Label serLabel = new Label("Servings:");
                    TextField servings = new TextField(rOrB[i+1]);
                    servings.setMaxWidth(200);
                    layout.getChildren().add(layout.getChildren().size(), ingLabel);
                    layout.getChildren().add(layout.getChildren().size(), ingredient);
                    layout.getChildren().add(layout.getChildren().size(), serLabel);
                    layout.getChildren().add(layout.getChildren().size(), servings);
                }

                Button update = new Button("Update Food");
                update.setOnAction(f -> {
                    /*Now we just need to validate*/
                    /*validation*/
                    String rName = nameField.getText();
                    boolean valid = true;
                    if(!validate(true, rName)){
                        valid = false;
                    } else {
                        food.append(",").append(rName);
                        int j = 1;//counter for textfields
                        for (Node node : layout.getChildren()) {
                            boolean isString = false;
                            /*Skip name field as it has already been validated, validate all other fields*/
                            if (node instanceof TextField && !rName.equals(((TextField) node).getText())) {
                                /*Check to see if we need to find a string or not*/
                                if((j % 2) != 0){
                                    isString = true;
                                }
                                /*check to make sure its valid*/
                                if (!validate(isString, ((TextField) node).getText())) {
                                    valid = false;
                                    food.setLength(0);
                                    break;
                                }
                                food.append(",").append(((TextField) node).getText());
                                j++;
                            }
                        }
                    }

                    /*If everything is valid, build the food.*/
                    if (valid) {
                        try {
                            foodHandler.update(rName, food.toString());
                            System.out.println(food.toString());
                            new AlertModal("Success", "Your food has been updated successfully");
                            this.close();//close window after adding food
                        } catch (InvalidParameterException ex) {
                            /*reset the stringbuilder and make it a basic recipe again*/
                            food.setLength(0);
                            food.append("r");
                            new AlertModal("Error", ex.toString());
                        }
                    } else {
                        new AlertModal("Invalid entry","Invalid values. Please verify all attributes.");
                        food.setLength(0);
                        food.append("r");
                    }
                });

                layout.getChildren().addAll(update);
            }

        });

        /*Setup layout*/
        layout.getChildren().addAll(selectLabel, foodsList, select);
        layout.setAlignment(Pos.CENTER);

        return new Scene(layout);
    }

    /**
     * Delete a food object. Controls user input and Delete Food view
     *
     * @return the scene for the delete food modal.
     */
    public Scene deleteFood(){
        Label selectLabel = new Label("Select the food you would like to delete:");
        ComboBox<String> foods = new ComboBox<>();

        /*Get all the foods that are saved and add them to a list*/
        Collection<Food> allFoods = foodHandler.getAll();
        for(Food food: allFoods){
            foods.getItems().add(food.getName());
        }

        Button delete = new Button("Delete Food");
        /*Handle delete button press*/
        delete.setOnAction(e -> {
            try{
               String foodToDelete = foods.getValue();
                if(foodHandler.delete(foodToDelete)){
                    new AlertModal("Success", "Food was successfully deleted!");
                    this.close();//close window after deleting food
                }else{
                    new AlertModal("Error", "Food could not be deleted!");
                }
            } catch (NullPointerException ex) {
                new AlertModal("Error", "Please select a food to delete.");
            }

        });

        /*Setup layout*/
        VBox layout = new VBox(10);
        layout.getChildren().addAll(selectLabel, foods, delete);
        layout.setAlignment(Pos.CENTER);

        return new Scene(layout);
    }

    /**
    * Ensure the users data is valid. Checks to make sure the appropriate type has been entered.
    *
    * @param isString if this is true then check to see if a string. If false check to see if an int.
    * @param val the value to be validated.
    *
    * @return valid is the entered item valid or not
    */
    public boolean validate(boolean isString, String val){
        boolean valid = false;
        if (isString){
           if(!val.matches(".*\\d.*")){//checks to see if the string contains and numbers
               valid = true;
            }
        } else {
            /*See if the value can be parsed*/
            try{
                double i = Double.parseDouble(val);
                valid = true;
            } catch (NumberFormatException ex){
                valid = false;
            }
        }

        return valid;
    }

}
