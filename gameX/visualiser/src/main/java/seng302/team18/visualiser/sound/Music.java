package seng302.team18.visualiser.sound;

import java.net.URL;

/**
 * Long-running sounds
 */
public enum Music {
    THEME("audio/theme.mp3"),
    BEEPBOOP("audio/beepboop.mp3");


    private final String url;


    Music(String url) {
        URL resource = getClass().getClassLoader().getResource(url);
        this.url = (resource == null) ? "" : resource.toString();
    }


    public String getUrl() {
        return url;
    }
}
