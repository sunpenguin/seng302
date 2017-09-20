package seng302.team18.visualiser.sound;

/**
 * 'Fire-and-forget' sound effects
 */
public enum SoundEffect {
    COLLISION("audio/collision.wav"),
    FIRE_BULLET("audio/fire_bullet.wav"),
    BUTTON_MOUSE_ENTER("audio/button_hover.wav"),
    BUTTON_MOUSE_CLICK("audio/button_click.wav");


    private final String url;


    SoundEffect(String url) {
        this.url = getClass().getClassLoader().getResource(url).toString();
    }


    public String getUrl() {
        return url;
    }
}
