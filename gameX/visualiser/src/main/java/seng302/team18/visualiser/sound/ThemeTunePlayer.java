package seng302.team18.visualiser.sound;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by cslaven on 25/09/17.
 */
public class ThemeTunePlayer {

    private static boolean playing = false;
    private static MediaPlayer mediaPlayer;
    private String path = "audio/theme.wav";

    public void playTrack(){
        if (ThemeTunePlayer.playing){
            return;
        }
        else {
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

    public static void stopTrack(){
        mediaPlayer.stop();
        playing = false;
    }

    public static boolean isPlaying() {
        return playing;
    }

    public static void setPlaying(boolean playing) {
        ThemeTunePlayer.playing = playing;
    }
}
