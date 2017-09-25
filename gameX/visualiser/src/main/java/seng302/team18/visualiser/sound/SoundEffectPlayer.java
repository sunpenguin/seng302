package seng302.team18.visualiser.sound;

import javafx.scene.media.AudioClip;

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
                .collect(Collectors.toMap(effect -> effect, effect -> new AudioClip(effect.getUrl())));
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
}
