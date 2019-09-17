package DietManager.Controller;

import DietManager.Model.Exercise.Exercise;
import DietManager.Model.ExerciseHandler;
import DietManager.View.FXComponents.*;
import DietManager.View.JavaFXUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 *Handle actions from the "Exercise" menu items.
 *Handles user input and controlls the view for crud actions on Exercise objects.
 */
public class ExerciseController extends Stage implements EventHandler<ActionEvent> {

    private JavaFXUI ui;
    private ExerciseHandler exerciseHandler;
    private String command;

    /**
     * This will instantiate a new controller that will be used to CRUD exerciese in the model.
     * @param exerciseHandler The exercise handler we are going to crud against.
     * @param ui The ui that we are going to bind to.
     * @param command The command sent to us from the main ui.
     */
    public ExerciseController(ExerciseHandler exerciseHandler, JavaFXUI ui, String command){
        this.ui = ui;
        this.exerciseHandler = exerciseHandler;
        this.command = command;
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
     * Determines which command was passed in and switches accordingly
     * @param e the ActionEvent fired
     */
    @Override
    public void handle(ActionEvent e) {
        switch(command){

            case "add":
                this.setStage(this.createExercise(), "Add Exercise");
                break;

            case "update":
                this.setStage(this.updateExercise(), "Update Exercise");
                break;

            case "delete":
                this.setStage(this.deleteExercise(), "Delete Exercise");
                break;
        }
    }

    /**
     * Create a new Exercise object. Controls user input and Add Exercise view
     * @return A scene for creating an exercise.
     */
    private Scene createExercise(){
        this.setHeight(250);
        VBox layout = new VBox(10);
        StringBuilder exerciseString = new StringBuilder("e,");

        Label lblName = new Label("Exercise Name");
        TextField tfName = new TextField();
        tfName.setMaxWidth(200);

        Label lblCals = new Label("Calorie Burn (per 100lbs person for 1 hour)");
        TextField tfCals = new TextField();
        tfCals.setMaxWidth(100);

        Button btnAddExercise = new Button("Add Exercise");
        btnAddExercise.setOnAction( e -> {
            try{
                exerciseString.append(tfName.getText());
                exerciseString.append(",");
                if(validateNumber(tfCals.getText())) {
                    exerciseString.append(tfCals.getText());
                } else {
                    exerciseString.setLength(0);
                }

                if(exerciseString.length() > 0) { // NO 0 LENGTH STINGS!
                    if (!this.exerciseHandler.add(exerciseString.toString())) {
                        new AlertModal("Error", "The exercise is invalid.");
                    } else {
                        new AlertModal("Success", "Exercise added.");
                        this.close();
                    }
                }
            } catch (NullPointerException npe) {
                //This is left blank intentionally. There are other errors that will pop up for this case.
            }
        });

        layout.getChildren().addAll(lblName,tfName,lblCals,tfCals,btnAddExercise);
        layout.setAlignment(Pos.CENTER);
        return new Scene(layout);
    }

    /**
     * Update Exercise object, controls user input and Update Exercise view
     * @return A scene that will update exercises.
     */
    private Scene updateExercise() {

        //Build the initial ui.
        this.setHeight(200);
        VBox layout = new VBox(10);
        Label lbl = new Label("Which exercise to update:");
        ComboBox<String> cmb = new ComboBox<>();

        //add things to the cbm box to igv ehte user and option
        for (Exercise item : exerciseHandler.getAll()) {
            cmb.getItems().add(item.getName());
        }

        Button btn = new Button("Update Selected");
        btn.setOnAction(e -> {
            this.setHeight(300);
            //get the value and remove the items.
            Exercise exercise = exerciseHandler.get(cmb.getValue());
            layout.getChildren().removeAll(lbl, cmb, btn);

            Label lblName = new Label("Exercise Name");
            TextField tfName = new TextField();
            tfName.setMaxWidth(200);
            tfName.setText(exercise.getName());

            Label lblCals = new Label("Calorie Burn (per 100lbs person for 1 hour)");
            TextField tfCals = new TextField();
            tfCals.setMaxWidth(100);
            tfCals.setText("" + exercise.getCalories());

            Button btnAddExercise = new Button("Update Exercise");
            btnAddExercise.setOnAction(e2 -> {
                try {
                    Exercise theNewOne = new Exercise("BADDC0FFEE",0Xff);
                    theNewOne.setName(tfName.getText());

                    if (validateNumber(tfCals.getText())) {
                        theNewOne.setCalories(Double.parseDouble(tfCals.getText()));
                    }

                    if ((exerciseHandler.delete(exercise) && exerciseHandler.add(theNewOne))) {
                        new AlertModal("Success", "Exercise updated.");
                    } else {
                        new AlertModal("Error", "Exercise was NOT updated.");
                    }
                } catch (Exception E) {
                    new AlertModal("Error", "Exercise contained malformed data.");
                }
                this.close();
            });
            layout.getChildren().addAll(lblName,tfName,lblCals,tfCals,btnAddExercise);
        });

        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(lbl,cmb,btn);
        return new Scene(layout);
    }

    /**
     * Delete a Exercise object. Controls user input and Delete Exercise view
     * @return A scene that will allow the user to delete exercies.
     */
    private Scene deleteExercise() {
        this.setHeight(250);
        VBox layout = new VBox(10);
        ComboBox<String> cmb = new ComboBox<>();

        for(Exercise item : exerciseHandler.getAll()) {
            cmb.getItems().add(item.getName());
        }

        Label lbl = new Label("Which exercise do you want to delete?");
        Button btn = new Button("Delete Selected");
        btn.setOnAction( e -> {
            exerciseHandler.delete(cmb.getValue());
            if(exerciseHandler.hasExercise(cmb.getValue())) {
                new AlertModal("Error","Exercise was not deleted.");
            } else {
                cmb.getItems().remove(cmb.getValue());
                new AlertModal("Success","Exercise was deleted.");
                this.close();
            }
        });

        layout.getChildren().addAll(cmb,lbl,btn);
        layout.setAlignment(Pos.CENTER);
        return new Scene(layout);
    }

    /**
     * Use this to validate strings are in fact numbers.
     * @param input The string that might be a number
     * @return True if the string can be parsed as a double.
     */
    private boolean validateNumber(String input){
        boolean isValid = false;
        try{
            Double.parseDouble(input);
            isValid = true;
        } catch (NumberFormatException npe) {
            // We know it wasn't valid.
        }
        return isValid;
    }
}
