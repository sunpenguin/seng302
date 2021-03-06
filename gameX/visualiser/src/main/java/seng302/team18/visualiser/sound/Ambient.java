package seng302.team18.visualiser.sound;

import java.net.URL;

/**
 * Long-running sounds
 */
public enum Ambient {
    WAVES_1("audio/wave_sounds.mp3");


    private final String url;


    Ambient(String url) {
        URL resource = getClass().getClassLoader().getResource(url);
        this.url = (resource == null) ? "" : resource.toString();
    }


    public String getUrl() {
        return url;
    }
}
