package DietManager.View.FXComponents;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * A modal used to display a combobox with options to the user
 * Eliminates the need to create a new modal for many options
 */
public class ComboModal extends Stage {

    private ComboBox<String> values;
    private TextField textField;
    private VBox layout;
    private String title;
    private String labelForCombo;
    private String labelForTextField;
    private ObservableList<String> comboValues;

    /**
     * Constructor for the combobox modal
     *
     * @param title the title of the modal
     * @param labelForCombo the label for the combobox
     * @param labelForTextField the label for the text field
     * @param comboValues the values to fill the combobox with
     */
    public ComboModal(String title, String labelForCombo,String labelForTextField,ObservableList<String> comboValues){
        this.title  = title;
        this.labelForCombo = labelForCombo;
        this.labelForTextField = labelForTextField;
        this.comboValues = comboValues;
    }
    public int[] init(){

        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle(title);
        this.setMinWidth(400);
        int[] returnValues = new int[2];

        Label comboLabel = new Label(labelForCombo);
        comboLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        Label textFieldLabel = null;
        if(!(labelForTextField == null)) {
            textFieldLabel = new Label(labelForTextField);
            textFieldLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        }
        TextField textField = new TextField();

        ComboBox<String> values = new ComboBox<>();
        values.setItems(comboValues);
        values.getSelectionModel().selectFirst();

        layout = new VBox(10);
        layout.getChildren().addAll(comboLabel,values);
        if(!(labelForTextField == null)){
            layout.getChildren().addAll(textFieldLabel,textField);
        }
        layout.setAlignment(Pos.CENTER);

        /*Add the submit button*/
        Button submit = new Button("Submit");
        submit.setOnAction(e -> {
            this.close();
            returnValues[0] = values.getSelectionModel().getSelectedIndex();
            if(labelForTextField != null){
                try {
                    returnValues[1] = Integer.parseInt(textField.getText());
                }catch(NumberFormatException nfe){
                    AlertModal alert = new AlertModal("Input Error","Please Enter a value for " + labelForTextField);
                }
            }
            return;
        });

        layout.getChildren().add(submit);
        Scene scene = new Scene(layout);
        this.setScene(scene);
        this.showAndWait();
        return returnValues;
    }

    public ComboBox<String> getValues() {
        return values;
    }

    public TextField getTextField() {
        return textField;
    }
}
