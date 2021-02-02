package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class EndScreenController {
    @FXML private Label endMessageLabel;

    private Stage stage;

    public void setup(Stage stage, String endMessage) {
        this.stage = stage;
        endMessageLabel.setText(endMessage);
    }

    @FXML
    private void initialize() {

    }

    @FXML
    private void rollCredits() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "layouts/creditsScene_layout.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            ((CreditsController) loader.getController()).setup(stage);
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println("oops, this is broked");
        }
    }
}
