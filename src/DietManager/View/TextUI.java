package DietManager.View;

import DietManager.Controller.TextEntryController;
import DietManager.Controller.TextExerciseController;
import DietManager.Controller.TextFoodController;

import java.util.Scanner;

/**
 * The TextUI is used for our console interface. It accesses the same model as the JavaFXUI only through a different controller
 */
public class TextUI implements Runnable{

    private Scanner scan = new Scanner(System.in);
    private TextFoodController foodController;
    private TextExerciseController exerciseController;
    private TextEntryController entryController;

    /**
     * Constructor for the TextUI
     *
     * @param foodController the food controller for TextUI
     * @param exerciseController the exercise controller for TextUI
     * @param entryController the entry controller for textUI
     */
    public TextUI(TextFoodController foodController, TextExerciseController exerciseController, TextEntryController entryController){
        this.foodController = foodController;
        this.exerciseController = exerciseController;
        this.entryController = entryController;
    }

    /**
     * Starts the UEX
     * Greets the user and calls prompt after the completion of every task/command
     */
    public void run(){
        System.out.println("Welcome to Bits & Bytes Diet Manager!");
        while(true){
            prompt();
        }
    }

    /**
     * Prompt the user with menu items and options to select
     */
    public void prompt() {
        System.out.println("\n1. Entries");
        System.out.println("2. Foods");
        System.out.println("3. Exercises");
        System.out.println("0. Exit");
        System.out.println("Select a Category:");
        select(scan.next());
    }

    /**
     * Switch on the users selection to determine which methods to call
     *
     * @param selection the users selection/command
     */
    public void select(String selection){
        switch(selection){
            case"1":
                entryController.prompt();
                break;
            case"2":
                foodController.prompt();
                break;
            case"3":
                exerciseController.prompt();
                break;
            case"0":
                System.exit(0);
            default:
                System.out.println("Invalid Selection. Try Again.");
                prompt();
                break;
        }
    }
}
