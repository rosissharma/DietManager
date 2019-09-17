package DietManager.View.FXComponents;

import DietManager.Model.Entry.Entry;
import DietManager.Model.Entry.FoodEntry;
import DietManager.Model.EntryHandler;
import DietManager.Model.ExerciseHandler;
import DietManager.Model.FoodHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

public class Graph extends Pane implements Observer {

    private EntryHandler entryHandler;

    public Graph(EntryHandler entryHandler){

        this.entryHandler = entryHandler;
        this.entryHandler.addObserver(this);
        update(entryHandler,entryHandler);
    }

    @Override
    public void update(Observable observable, Object o) {
        Collection<Entry> entries = entryHandler.getAll();
        double maxValue = getMaxValue(entries);
        ObservableList<String>nutrientTypes = FXCollections.observableArrayList();
        nutrientTypes.addAll("Fat","Protein","Carbs");
        //Defining the x axis
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(nutrientTypes);
        //Defining the y axis
        NumberAxis yAxis = new NumberAxis(0,maxValue,20);
        yAxis.setLabel("Grams");
        //Creating the Bar chart
        BarChart<String,Number>barChart = new BarChart<>(xAxis,yAxis);
        barChart.setTitle("Nutrition by Entry Date");
        barChart.setStyle("-fx-font-size:" + 15 + "px;");
        //Prepare XYChart.Series objects by setting data
        for(Entry entry : entries){
            XYChart.Series<String,Number> series = new XYChart.Series<>();
            series.setName(entry.getDateString());
            Collection<FoodEntry> foodEntries = entry.getAllFoodEntries();
            double fatTotal = 0;
            double proteinTotal = 0;
            double carbTotal = 0;
            for(FoodEntry foodEntry : foodEntries){
                fatTotal += foodEntry.getTotalFat();
                proteinTotal += foodEntry.getTotalProtein();
                carbTotal  += foodEntry.getTotalCarbs();
            }
            Number fatNumber = fatTotal;
            series.getData().add(new XYChart.Data<>("Fat",fatNumber));
            Number proteinNumber = proteinTotal;
            series.getData().add(new XYChart.Data<>("Protein",proteinNumber));
            Number carbNumber = carbTotal;
            series.getData().add(new XYChart.Data<>("Carbs",carbNumber));
            barChart.getData().add(series);
        }
        Group root = new Group(barChart);
        this.getChildren().clear();
        this.getChildren().add(root);
    }
    private double getMaxValue(Collection<Entry> entries){
        double maxValue = 0;
        for(Entry entry : entries) {
            Collection<FoodEntry> foodEntries = entry.getAllFoodEntries();
            double fatTotal = 0;
            double proteinTotal = 0;
            double carbTotal = 0;
            for (FoodEntry foodEntry : foodEntries) {
                fatTotal += foodEntry.getTotalFat();
                proteinTotal += foodEntry.getTotalProtein();
                carbTotal += foodEntry.getTotalCarbs();
            }
            if (fatTotal > maxValue) {
                maxValue = fatTotal;
            }
            if (proteinTotal > maxValue) {
                maxValue = proteinTotal;
            }
            if (carbTotal > maxValue) {
                maxValue = carbTotal;
            }
        }
        return maxValue;
    }
}
