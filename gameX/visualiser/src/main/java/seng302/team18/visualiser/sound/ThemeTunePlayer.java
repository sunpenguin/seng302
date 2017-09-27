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


    /**
     * Method the play the track
     */
    public void playSound(String path){
        if (ThemeTunePlayer.playing){
            return;
        } else {
            URL resource = getClass().getClassLoader().getResource(path);
            String songPath = resource.toString();
            Media song = new Media(songPath);
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
