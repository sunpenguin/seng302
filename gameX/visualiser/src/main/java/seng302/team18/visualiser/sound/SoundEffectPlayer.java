package seng302.team18.visualiser.sound;

import javafx.scene.media.AudioClip;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Manages the playback of audio effects
 */
public class SoundEffectPlayer {

    private final Map<SoundEffect, AudioClip> effects;


    /**
     * Constructs a new instance of SoundPlayer
     */
    public SoundEffectPlayer() {
        effects = Arrays.stream(SoundEffect.values())
                .collect(Collectors.toMap(effect -> effect, effect -> new AudioClip(effect.getUrl().toString())));
    }


    /**
     * Plays the specified sound effect. Calling while the sound is still playing will result in two instances of the
     * sound playing and audible at once.
     *
     * @param effect the sound effect to play.
     */
    public void playEffect(SoundEffect effect) {
        effects.get(effect).play();
    }


    public long getDuration(SoundEffect effect) {
//        System.out.println(effect.getUrl());
        URL url = effect.getUrl();
//        System.out.println("url = " + url);
        URI uri = null;
        try {
            uri = url.toURI();
        } catch (URISyntaxException | NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
        File file = new File(uri);
        AudioInputStream audioInputStream;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(url);
        } catch (UnsupportedAudioFileException | IOException e) {
            System.out.println(effect.getUrl());
            e.printStackTrace();
            return 0;
        }
        AudioFormat format = audioInputStream.getFormat();
        long audioFileLength = file.length();
        int frameSize = format.getFrameSize();
        float frameRate = format.getFrameRate();
        Float durationInSeconds = (audioFileLength / (frameSize * frameRate));
        Float durationInMillis = durationInSeconds * 1000;
        return durationInMillis.longValue();
    }
}
