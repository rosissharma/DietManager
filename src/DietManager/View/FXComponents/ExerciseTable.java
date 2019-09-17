package DietManager.View.FXComponents;

import DietManager.Model.EntryHandler;
import DietManager.Model.Exercise.Exercise;
import DietManager.Model.ExerciseHandler;
import DietManager.Model.Food.Food;
import DietManager.Model.FoodHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.Observable;
import java.util.Observer;

/**
 * Displays all exercise entries to the user via the JavaFXUI
 */
public class ExerciseTable extends Pane implements Observer {

    private ObservableList<Exercise> data;
    private TableView<Exercise>table;
    private ExerciseHandler exerciseHandler;
    private Label label;
    private VBox vbox;

    /**
     * Constructor for the exercise table
     *
     * @param exerciseHandler the exercise handler needed to get all exercise entries
     */
    public ExerciseTable(ExerciseHandler exerciseHandler){

        this.exerciseHandler = exerciseHandler;
        this.exerciseHandler.addObserver(this);
        table = new TableView<>();
        label = new Label("Exercise List");
        label.setFont(new Font("Arial", 20));

        TableColumn exerciseName = new TableColumn("Exercise");
        exerciseName.setCellValueFactory(new PropertyValueFactory<Exercise,String>("name"));
        exerciseName.setMinWidth(100);

        TableColumn calorieBurn = new TableColumn("Calories Per Hour");
        calorieBurn.setCellValueFactory(new PropertyValueFactory<Exercise,String>("calories"));
        calorieBurn.setMinWidth(100);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        update(exerciseHandler,exerciseHandler);
        table.getColumns().addAll(exerciseName,calorieBurn);

        vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(5, 10, 0, 135));
        vbox.getChildren().addAll(label, table);
        this.getChildren().add(vbox);
    }

    /**
     * Implement the Observer update method. Calls all observable objects to let them know an update has been made
     *
     * @param obs the obeservable object
     * @param o object that observable wil get updated about/with
     *
     */
    public void update(Observable obs, Object o){
        data = FXCollections.observableArrayList(exerciseHandler.getAll());
        table.setItems(data);
    }

    /**
     * Return all exercise data
     *
     * @return an ObservableList of all exercise data
     */
    public ObservableList<Exercise> getData() {
        return data;
    }

    /**
     * Set the exercise data
     *
     * @param data the exercise data to be set
     */
    public void setData(ObservableList<Exercise> data) {
        this.data = data;
    }
}
