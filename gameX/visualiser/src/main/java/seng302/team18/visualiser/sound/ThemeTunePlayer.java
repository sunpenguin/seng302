package seng302.team18.visualiser.sound;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

/**
 * Class to handle playing th theme tune
 */
public class ThemeTunePlayer {

    private MediaPlayer mediaPlayer;


    /**
     * Method the play the track
     *
     * @param path URL to the audio file
     * @param volume volume for the sound to be played at (<1 is preffered)
     */
    public void playSound(String path, double volume) {
        URL resource = getClass().getClassLoader().getResource(path);
        String songPath = resource.toString();
        Media song = new Media(songPath);
        mediaPlayer = new MediaPlayer(song);
        mediaPlayer.setVolume(volume);
        mediaPlayer.play();
        mediaPlayer.setCycleCount(mediaPlayer.INDEFINITE);
    }


    /**
     * Method to stop the track
     */
    public void stopTrack() {
        mediaPlayer.stop();
        mediaPlayer.dispose();
    }
}
