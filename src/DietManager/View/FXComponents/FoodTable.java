package DietManager.View.FXComponents;

import DietManager.Model.EntryHandler;
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

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

/**
 * Displays all food entries to the user via the JavaFXUI
 */
public class FoodTable extends Pane implements Observer {

    private ObservableList<Food> data;
    private TableView<Food>table;
    private FoodHandler foodHandler;
    private ExerciseHandler exerciseHandler;
    private EntryHandler entryHandler;
    private Label label;
    private VBox vbox;

    /**
     * Constructor for the food table
     *
     * @param foodHandler the exercise handler needed to get all food entries
     */
    public FoodTable(FoodHandler foodHandler){
        this.foodHandler = foodHandler;
        this.foodHandler.addObserver(this);
        table = new TableView<>();
        label = new Label("Food Pantry");
        label.setFont(new Font("Arial", 20));

        TableColumn foodName = new TableColumn("Food");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        foodName.setCellValueFactory(new PropertyValueFactory<Food,String>("name"));
        foodName.setMinWidth(100);

        TableColumn calorieCount = new TableColumn("Calories");
        calorieCount.setMinWidth(100);
        calorieCount.setCellValueFactory(new PropertyValueFactory<Food,String>("calories"));

        TableColumn fatCount = new TableColumn("Fat");
        fatCount.setMinWidth(100);
        fatCount.setCellValueFactory(new PropertyValueFactory<Food,String>("fat"));

        TableColumn proteinCount = new TableColumn("Protein");
        proteinCount.setMinWidth(100);
        proteinCount.setCellValueFactory(new PropertyValueFactory<Food,String>("protein"));

        TableColumn carbCount = new TableColumn("Carbs");
        carbCount.setMinWidth(100);
        carbCount.setCellValueFactory(new PropertyValueFactory<Food,String>("carbs"));
        update(foodHandler,foodHandler);
        table.getColumns().addAll(foodName,calorieCount,fatCount,proteinCount,carbCount);

        vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(5, 5, 5, 5));
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
        data = FXCollections.observableArrayList(foodHandler.getAll());
        table.setItems(data);
    }

    /**
     * Return all food data
     *
     * @return an ObservableList of all food data
     */
    public ObservableList<Food> getData() {
        return data;
    }

}
