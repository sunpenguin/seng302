package seng302.team18.visualiser.sound;

import java.net.URL;

/**
 * 'Fire-and-forget' sound effects
 */
public enum SoundEffect {
    COLLISION("audio/collision.mp3"),
    FIRE_BULLET("audio/fire_bullet.mp3"),
    BUTTON_MOUSE_ENTER("audio/button_hover.mp3"),
    BUTTON_MOUSE_CLICK("audio/button_click.mp3"),
    PLAYER_DISQUALIFIED("audio/fail.mp3"),
    LOSE_LIFE("audio/lose_life.mp3"), //Check
    RACE_START_LEAD_IN("audio/start_lead_in.mp3"),
    RACE_START("audio/start_blip.mp3"),
    ACTIVATE_SPEED_BOOST("audio/activate_speed_boost.mp3"),
    CROSS_FINISH_LINE("audio/finish_race.mp3"), //Check
    PICK_UP_POWER_UP("audio/pickup.mp3"); //Implement


    private final URL url;


    SoundEffect(String url) {
        this.url = getClass().getClassLoader().getResource(url);
//        this.url = (resource == null) ? "" : resource.toString();
    }


    public URL getUrl() {
//        System.out.println(url.toString());
        return url;
    }
}
