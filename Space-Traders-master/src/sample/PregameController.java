package sample;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class PregameController {
    private static boolean scenesCreated = false;
    private static Stage stage;
    private static Scene startScene;
    private static Scene setupScene;

    private static int totalSkillPoints = 20;
    private static Integer pointsLeft = 16;
    private static String mode = "Medium";
    private static int credits = 600;
    private static String name;
    private static Integer[] points = new Integer[4];

    @FXML private Pane confirmationSceneContainer;

    @FXML private TextField nameBox;
    @FXML private Spinner<Integer> pilotSpinner;
    @FXML private Spinner<Integer> fighterSpinner;
    @FXML private Spinner<Integer> merchantSpinner;
    @FXML private Spinner<Integer> engineerSpinner;
    @FXML private Label pointsLeftLabel;

    @FXML private Label confirmNameLabel;
    @FXML private Label confirmDifficultyLabel;
    @FXML private Label confirmPointsAllocation;
    @FXML private Label confirmStartingCredits;

    @FXML
    private void initialize() throws IOException {
        if (!scenesCreated) {
            scenesCreated = true;
            setupScene = new Scene(FXMLLoader.load(this.getClass().getResource(
                     "layouts/setupScene_layout.fxml")));
            startScene = new Scene(FXMLLoader.load(this.getClass().getResource(
                    "layouts/startScene_layout.fxml")));
        }

        if (confirmationSceneContainer != null) {
            populateConfirmationScene();
        }
    }

    void setEnvironment(Stage stage) {
        PregameController.stage = stage;
    }

    @FXML
    private void newGame() {
        stage.setScene(setupScene);
    }

    @FXML
    private void startScene() {
        stage.setScene(startScene);
    }

    @FXML
    private void nameChanged() {
        name = nameBox.getText().trim();
    }

    @FXML
    private void changeDifficulty(Event event) {
        mode = ((RadioButton) event.getTarget()).getText();
        if (mode.equals("Easy")) {
            totalSkillPoints = 24;
            credits = 800;
        } else if (mode.equals("Medium")) {
            totalSkillPoints = 20;
            credits = 600;
        } else if (mode.equals("Hard")) {
            totalSkillPoints = 16;
            credits = 400;
        }
        updatePointsLeft();

        //Decrement the spinner with the highest value until there are no negative points.
        Spinner[] spinners = new Spinner[]
            {pilotSpinner, fighterSpinner, merchantSpinner, engineerSpinner};
        while (pointsLeft < 0) {
            int highest = 0;
            for (int i = 0; i < points.length; i++) {
                highest = points[i] > points[highest] ? i : highest;
            }
            spinners[highest].decrement();
            updatePointsLeft();
        }
    }

    @FXML
    private void spinnerChanged(InputEvent event) {
        updatePointsLeft();
        if (pointsLeft < 0) {
            ((Spinner) event.getSource()).decrement();
            updatePointsLeft();
        }
    }

    @FXML
    private void confirmationPage(ActionEvent event) {
        name = nameBox.getText().trim();
        if (name.length() == 0) {
            Alert a = new Alert(Alert.AlertType.ERROR,
                    "Name is invalid. Please input a nonempty name.");
            a.showAndWait();
        } else if (mode == null) {
            Alert a = new Alert(Alert.AlertType.ERROR,
                    "Please select a difficulty mode.");
            a.showAndWait();
        } else if (pointsLeft > 0) {
            Alert a = new Alert(Alert.AlertType.ERROR,
                    "Please allocate all of your skill points.");
            a.showAndWait();
        } else {
            setScene("layouts/confirmationScene_layout.fxml");
        }
    }

    private void populateConfirmationScene() {
        confirmNameLabel.setText(String.format(confirmNameLabel.getText(), name));
        confirmDifficultyLabel.setText(String.format(confirmDifficultyLabel.getText(),
                mode));
        confirmPointsAllocation.setText(String.format(confirmPointsAllocation.getText(),
                points[0], points[1], points[2], points[3]));
        confirmStartingCredits.setText(String.format(confirmStartingCredits.getText(),
                credits));
    }

    private void updatePointsLeft() {
        points = new Integer[] {pilotSpinner.getValue(),
                fighterSpinner.getValue(),
                merchantSpinner.getValue(),
                engineerSpinner.getValue()};
        pointsLeft = totalSkillPoints;
        for (int i : points) {
            pointsLeft -= i;
        }

        pointsLeftLabel.setText(pointsLeft.toString());
    }

    private void setScene(String path) {
        try {
            stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource(path))));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @FXML
    private void launchGame() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "layouts/mainGameView_layout.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        ((GameController) loader.getController()).setup(stage,
                new Universe(30, Mode.valueOf(mode.toUpperCase()), points));
        stage.setScene(scene);
    }

    @FXML
    private void showCreditsScreen() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "layouts/creditsScene_layout.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        ((CreditsController) loader.getController()).setup(stage);
        stage.setScene(scene);
    }
}
