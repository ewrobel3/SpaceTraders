package sample;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

import java.io.File;

public class SoundsManager {

    private static String selectSoundFile = "src/sample/sounds/Select04.wav";
    private static Media selectSoundMedia = new Media(new File(selectSoundFile).toURI().toString());
    private static AudioClip selectSound = new AudioClip(selectSoundMedia.getSource());

    private static String warpSoundFile = "src/sample/sounds/Intercom01.wav";
    private static Media warpSoundMedia = new Media(new File(warpSoundFile).toURI().toString());
    private static AudioClip warpSound = new AudioClip(warpSoundMedia.getSource());

    public static void warpPlay(double vol) {
        warpSound.play(vol);
    }

    public static void selectPlay(double vol) {
        selectSound.play(vol);
    }

}
