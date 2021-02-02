package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CreditsController {
    private Stage stage;

    public void setup(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void fromTheTop() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "layouts/startScene_layout.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            ((PregameController) loader.getController()).setEnvironment(stage);
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println("oops, this is broked");
        }
    }
}
