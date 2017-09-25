package seng302.team18.visualiser.display;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;


/**
 * Created by cslaven on 25/09/17.
 */
public class Sound {

    private static boolean playing = false;


    public void playTrack(String soundFile){
        if (Sound.playing){
            return;
        }
        else {
            Media song = new Media(new File(soundFile).toURI().toString());
            MediaPlayer player = new MediaPlayer(song);
            player.play();
            player.setCycleCount(player.INDEFINITE);
            playing = true;
        }

    }

    public static boolean isPlaying() {
        return playing;
    }

    public static void setPlaying(boolean playing) {
        Sound.playing = playing;
    }
}
