package seng302.team18.visualiser.sound;

import java.net.URL;

/**
 * 'Fire-and-forget' sound effects
 */
public enum SoundEffect {
    COLLISION("audio/collision.wav"),
    FIRE_BULLET("audio/fire_bullet.wav"),
    BUTTON_MOUSE_ENTER("audio/button_hover.wav"),
    BUTTON_MOUSE_CLICK("audio/button_click.wav"),
    PLAYER_DISQUALIFIED("audio/fail.wav"),
    LOSE_LIFE("audio/lose_life.wav"),
    RACE_START("audio/button_click.wav"), // TODO create sound effect
    ACTIVATE_SPEED_BOOST("audio/button_click.wav"), // TODO create sound effect
    CROSS_FINISH_LINE("audio/button_click.wav"), // TODO create sound effect
    PICK_UP_POWER_UP("audio/button_click.wav"); // TODO create sound effect


    private final String url;


    SoundEffect(String url) {
        URL resource = getClass().getClassLoader().getResource(url);
        this.url = (resource == null) ? "" : resource.toString();
    }


    public String getUrl() {
        return url;
    }
}
