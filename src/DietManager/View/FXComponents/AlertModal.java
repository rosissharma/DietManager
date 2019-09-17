package DietManager.View.FXComponents;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * A modal used to display alerts (warnings, prompts, notices, etc.) to users.
 * Contains a label for text and an OK button which closes the modal.
 */
public class AlertModal extends Stage {

    /**
     * Constructor for the alert modal
     *
     * @param title the title for the modal
     * @param message the message to be displayed to the user
     */
    public AlertModal(String title, String message){
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle(title);
        this.setMinWidth(400);

        Label label = new Label(message);
        label.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        Button ok = new Button("OK");
        ok.setOnAction(e -> this.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, ok);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        this.setScene(scene);
        this.showAndWait();
    }
}
