package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;

//This class will directly launch the main game view
//without loading music. Created for quickly testing layouts.
public class DirectGameLaunchTEST extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        //Load the font into javaFX so it can be referenced later in stylesheets.
        String fontLocation = "src/sample/layouts/styles/fonts/Quantico-Regular.ttf";
        Font.loadFont(new FileInputStream(new File(fontLocation)), 72);

        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "layouts/mainGameView_layout.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        ((GameController) loader.getController()).setup(stage,
                new Universe(30, Mode.EASY,
                        new Integer[] {10, 1, 1, 10}));

        stage.setResizable(false);
        stage.setTitle("Space Traders");
        stage.setScene(scene);
        //stage.setMaximized(true);
        stage.show();
    }
}
