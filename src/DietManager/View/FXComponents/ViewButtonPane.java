package DietManager.View.FXComponents;

import DietManager.Controller.ButtonController;
import DietManager.View.JavaFXUI;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * Setup and add the buttons used to view different tables in the JavaFXUI
 */
public class ViewButtonPane extends Pane {

    /**
     * Constructor for view buttons
     * @param ui the active/main JavaFXUI that the buttons will be added to
     */
    private Button viewGraph;
    private Button viewFoods;
    private Button viewEntries;
    private Button viewExercises;
    private HBox hbox;
    private JavaFXUI ui;

    public ViewButtonPane(JavaFXUI ui){
        this.ui = ui;
        viewGraph = new Button("View Graph");
        viewGraph.setOnAction(new ButtonController(ui));
        viewFoods = new Button("View Foods");
        viewFoods.setOnAction(new ButtonController(ui));
        viewEntries = new Button("View Daily Logs");
        viewEntries.setOnAction(new ButtonController(ui));
        viewExercises = new Button("View Exercises");
        viewExercises.setOnAction(new ButtonController(ui));
        hbox = new HBox();
        hbox.getChildren().addAll(viewGraph,viewFoods,viewEntries,viewExercises);
        hbox.setPadding(new Insets(5, 0, 25, 50));
        hbox.setSpacing(20);
        this.getChildren().add(hbox);
    }
}
