package DietManager.Controller;

import DietManager.Model.Exercise.Exercise;
import DietManager.Model.ExerciseHandler;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is used to update control the text ui for exercises.
 */
public class TextExerciseController {

    private ExerciseHandler exerciseHandler;
    private Scanner scan = new Scanner(System.in);

    /**
     * Constructs a new Controler to be used for the text ui.
     * @param exerciseHandler The exercise handler that this controler will act upon.
     */
    public TextExerciseController(ExerciseHandler exerciseHandler){
        this.exerciseHandler = exerciseHandler;
    }

    public void prompt(){
        System.out.println("\n1. View All Exercises");
        System.out.println("2. Add Exercise");
        System.out.println("3. Update Exercise");
        System.out.println("4. Delete Exercise");
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
                update();
                break;

            case "4":
                delete();
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
     * Creates a new exercise from the input from the user.
     */
    public void create(){
        String exerciseName = null;
        double caloriesBurned = 0;
        System.out.println("\nWhat is the Name of the Exercise You'd Like to Add?:");
        exerciseName = scan.nextLine();
        System.out.println("How Many Calories Does it Burn Per Hour?:");
        caloriesBurned = Double.parseDouble(scan.nextLine());
        if(exerciseHandler.add(processExercise(exerciseName,caloriesBurned))){
            System.out.println("Exercise Successfully Created!");
        }else{
            System.out.println("There Was an Error Creating Your Exercise. Your Exercise Was Not Created.");
        }
    }

    /**
     * Prints out the exercises in a pretty print format.
     */
    public void view(){
        System.out.println("\nPlease Enter the Name of an Exercise:");
        String name = scan.nextLine();
        Exercise exercise = exerciseHandler.get(name);
        System.out.println("Name: " + exercise.getName());
        System.out.println("Calories Burned Per Hour: " + exercise.getCalories());
    }

    /**
     * Update an exercise
     */
    public void update(){
        String response = null;

        viewAll();
        System.out.println("Please select the food you want to update:");
        response = scan.nextLine();
        ArrayList<Exercise> foods = new ArrayList<>(exerciseHandler.getAll());

        int index = Integer.parseInt(response) - 1;
        Exercise exerciseToDelete = foods.get(index);

        try{
            exerciseHandler.delete(exerciseToDelete);
        } catch(Exception ex){
            System.out.println();
        }

        create();
    }

    /**
     * Deletes an exercise that is present in the handler.
     */
    public void delete(){
        ArrayList<Exercise> exercises = new ArrayList<>(exerciseHandler.getAll());
        viewAll();
        System.out.println("\nPlease Select an Exercise to Delete:");
        if(exerciseHandler.delete(exercises.get(Integer.parseInt(scan.nextLine())-1))){
            System.out.println("Exercise Successfully Deleted!");
        }else{
            System.out.println("There Was an Error Deleting Your Exercise. Your Exercise Was Not Deleted.");
        }
    }

    /**
     * This is used to get all the exercises and format each of them for the view() method.
     */
    private void viewAll(){
        ArrayList<Exercise> exercises = new ArrayList<>(exerciseHandler.getAll());
        for(int i = 0; i < exercises.size(); i++){
            Exercise exercise = exercises.get(i);
            System.out.println(i + 1 + ". " + exercise.getName());
        }
    }

    /**
     * This is a helper method to help create exercises.
     * @param exerciseName The name of the exercise.
     * @param caloriesBurned The calories burned from a 100lbs person performing the exercise for 1 hour.
     * @return The exercise.
     */
    private Exercise processExercise(String exerciseName,double caloriesBurned){
        return new Exercise(exerciseName,caloriesBurned);
    }
}
