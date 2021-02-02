package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;

public class LaunchScene extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        //Load the font into javaFX so it can be referenced later in stylesheets.
        String fontLocation = "src/sample/layouts/styles/fonts/Quantico-Regular.ttf";
        Font.loadFont(new FileInputStream(new File(fontLocation)), 72);

        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "layouts/startScene_layout.fxml"));
        Parent root = loader.load();
        //required to get a reference to the stage in the PregameController class
        Scene scene = new Scene(root);
        ((PregameController) loader.getController()).setEnvironment(stage);

        String musicFile = "src/sample/sounds/Interstellar_Space.wav";
        Media sound = new Media(new File(musicFile).toURI().toString());
        AudioClip mediaPlayer = new AudioClip(sound.getSource());
        mediaPlayer.setCycleCount(AudioClip.INDEFINITE);
        mediaPlayer.play(.2);


        stage.setTitle("Space Traders");
        stage.setScene(scene);
        //stage.setMaximized(true);
        stage.show();
    }
}
