package DietManager.View.FXComponents;

import DietManager.Controller.EntryController;
import DietManager.Model.Entry.Entry;
import DietManager.Model.EntryHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;

import java.util.Observable;
import java.util.Observer;

/**
 * Displays all log/entry dates to the user via the JavaFXUI
 */
public class LogTable extends Pane implements Observer {

    private ObservableList<Entry> data;
    private TableView<Entry>table;
    private EntryHandler entryHandler;
    private EntryController entryController;
    private TableColumn dateColumn;
    private Label label;
    private VBox vbox;

    /**
     * Constructor for the log table
     *
     * @param entryHandler the entry handler needed to get all entry/log entries
     * @param entryController the controller used when selecting entries
     */
    public LogTable(EntryHandler entryHandler, EntryController entryController){
        this.entryHandler = entryHandler;
        this.entryHandler.addObserver(this);
        this.entryController = entryController;
        entryHandler.addObserver(this);

        table = new TableView<>();
        label = new Label("Daily Logs");
        label.setFont(new Font("Arial", 20));
        dateColumn = new TableColumn("Date");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        dateColumn.setCellValueFactory(new PropertyValueFactory<Entry,String>("dateString"));
        dateColumn.setCellFactory(callback());
        update(entryHandler,entryHandler);
        table.getColumns().add(dateColumn);

        vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(5, 10, 0, 135));
        vbox.getChildren().addAll(label, table);
        this.getChildren().add(vbox);
        update(entryHandler,entryHandler);
    }

    /**
     * Implement the Observer update method. Calls all observable objects to let them know an update has been made
     *
     * @param obs the obeservable object
     * @param o object that observable wil get updated about/with
     *
     */
    public void update(Observable obs,Object o){
        data = FXCollections.observableArrayList(entryHandler.getAll());
        table.setItems(data);
    }

    /**
     * Enables the rows in the table to be clicked as a means of updating/viewing them
     *
     * @return the table column and contained cell
     */
    public Callback<TableColumn, TableCell> callback(){
        Callback<TableColumn, TableCell> cellFactory =
                new Callback<TableColumn, TableCell>() {
                    public TableCell call(TableColumn p) {
                        TableCell cell = new TableCell<Object, String>() {
                            /**
                             * Update the selected item
                             *
                             * @param item a string value of the selected item
                             * @param empty is the row empty
                             */
                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                setText(empty ? null : getString());
                                setGraphic(null);
                            }

                            /**
                             * Get the string value of the selected item
                             *
                             * @return the string value of the selcted item
                             */
                            private String getString() {
                                return getItem() == null ? "" : getItem();
                            }
                        };
                        cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

                            /**
                             * Override handle method
                             *
                             * @param event the mouse event fired
                             */
                            @Override
                            public void handle(MouseEvent event) {
                                if (event.getClickCount() > 1) {
                                    TableCell c = (TableCell) event.getSource();
                                    Object item = c.getTableRow().getItem();
                                        entryController.selectOperation((Entry)item);
                                }
                            }

                        });
                        return cell;
                    }
                };
        return cellFactory;
    }

    /**
     * Return all log data
     *
     * @return an ObservableList of all log data
     */
    public ObservableList<Entry> getData() {
        return data;
    }

}
