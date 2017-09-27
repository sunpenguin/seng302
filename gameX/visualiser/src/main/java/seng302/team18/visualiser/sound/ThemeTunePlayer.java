package seng302.team18.visualiser.sound;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

/**
 * Class to handle playing th theme tune
 */
public class ThemeTunePlayer {

    private static boolean playing = false;
    private static MediaPlayer mediaPlayer;
    private String path = "audio/theme.mp3";


    /**
     * Constructor for a theme player
     *
     * @param path the path to the sound file to be played
     */
    public ThemeTunePlayer(String path) {
        this.path = path;
    }

    /**
     * Method the play the track
     */
    public void playTrack(){
        if (ThemeTunePlayer.playing){
            return;
        } else {
            URL resource = getClass().getClassLoader().getResource(path);
            String path = resource.toString();
            Media song = new Media(path);
            mediaPlayer = new MediaPlayer(song);
            mediaPlayer.setVolume(0.6);
            mediaPlayer.play();
            mediaPlayer.setCycleCount(mediaPlayer.INDEFINITE);
            playing = true;

        }
    }


    /**
     * Method to stop the track
     */
    public static void stopTrack(){
        mediaPlayer.stop();
        mediaPlayer.dispose();
        playing = false;
    }
}
