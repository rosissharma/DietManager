package DietManager.Controller;

import DietManager.View.JavaFXUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Handles the button interactions from the viewPane for View exercise, logs, and foods buttons.
 */

public class ButtonController implements EventHandler<ActionEvent> {

    private JavaFXUI ui;

    /**
     * Handles the button interactions from the viewPane for View exercise, logs, and foods buttons.
     *
     * @param ui the main JavaFXUI that is running
     */
    public ButtonController(JavaFXUI ui){
        this.ui = ui;
    }

    /**
     * handle the button presses
     *
     * @param e the action event from the source button.
     */
    @Override
    public void handle(ActionEvent e) {
         /**
         * Determine which button was pressed.
         */
        if(e.getSource().toString().contains("View Graph"))
        {
            ui.getMain().setCenter(ui.getGraph());
        }
        else if(e.getSource().toString().contains("View Exercises")){

            ui.getMain().setCenter(ui.getExerciseTable());

        } else if(e.getSource().toString().contains("View Daily Logs")) {

            ui.getMain().setCenter(ui.getLogTable());

        } else if(e.getSource().toString().contains("View Foods")) {

            ui.getMain().setCenter(ui.getFoodTable());

        } else {
            System.out.println("ERROR - interaction not handled.");
        }
    }

}
