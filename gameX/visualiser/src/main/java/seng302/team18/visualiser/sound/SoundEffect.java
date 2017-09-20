package seng302.team18.visualiser.sound;

import javafx.scene.media.AudioClip;

/**
 * 'Fire-and-forget' sound effects
 */
public enum SoundEffect {
    COLLISION("audio/collision.wav"),
    FIRE_BULLET("audio/fire_bullet.wav"),
    BUTTON_MOUSE_ENTER("audio/button_hover.wav"),
    BUTTON_MOUSE_CLICK("audio/button_click.wav");


    private final AudioClip audioClip;


    SoundEffect(String url) {
        this.audioClip = new AudioClip(getClass().getClassLoader().getResource(url).toString());
    }


    /**
     * Plays the sound effect
     */
    public void play() {
        audioClip.play();
    }
}
