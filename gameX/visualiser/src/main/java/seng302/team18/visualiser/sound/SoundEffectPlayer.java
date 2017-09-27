package seng302.team18.visualiser.sound;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Manages the playback of audio effects
 */
public class SoundEffectPlayer {

    private final Map<SoundEffect, AudioClip> effects;
    private final Map<SoundEffect, MediaPlayer> mediaPlayers;


    /**
     * Constructs a new instance of SoundPlayer
     */
    public SoundEffectPlayer() {
        mediaPlayers = Arrays.stream(SoundEffect.values())
                .collect(Collectors.toMap(effect -> effect, effect -> new MediaPlayer(new Media(effect.getUrl().toString()))));
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


    /**
     * Gets the duration of the specified sound effect. Returns {@link Duration#UNKNOWN Duration.UNKOWN}
     * if the media player is not loaded yet
     *
     * @param effect the effect
     * @return the duration of the effect
     */
    public Duration getDuration(SoundEffect effect) {
        if (!mediaPlayers.get(effect).getStatus().equals(MediaPlayer.Status.READY)) {
            return Duration.UNKNOWN;
        }

        return mediaPlayers.get(effect).getMedia().getDuration();
    }


    /**
     * Creates a new MediaPlayer for a given effect.
     *
     * @param effect the effect
     * @return a new instance of MediaPlayer loaded with this effect
     */
    public MediaPlayer getEffectPlayer(SoundEffect effect) {
        // Uses the one from the map so that it is more likely to be in a ready state.
        MediaPlayer player = mediaPlayers.get(effect);
        mediaPlayers.put(effect, new MediaPlayer(new Media(effect.getUrl().toString())));
        return player;
    }
}
