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
    LOSE_LIFE("audio/lose_life.wav");


    private final String url;


    SoundEffect(String url) {
        URL resource = getClass().getClassLoader().getResource(url);
        this.url = (resource == null) ? "" : resource.toString();
    }


    public String getUrl() {
        return url;
    }
}
