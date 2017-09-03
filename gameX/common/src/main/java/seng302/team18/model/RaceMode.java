package seng302.team18.model;

import seng302.team18.message.RequestType;

/**
 * Enum for the different modes.
 */
public enum RaceMode {
    SPECTATION(RequestType.VIEWING.getCode()),
    RACE(RequestType.RACING.getCode()),
    CONTROLS_TUTORIAL(RequestType.CONTROLS_TUTORIAL.getCode()),
    GHOST(RequestType.GHOST.getCode()),
    RACE_TUTORIAL(4),
    START_TUTORIAL(5);


    private int code;


    RaceMode(int code) {
        this.code = code;
    }


    /**
     * Getter for the getCode of the race mode.
     *
     * @return the getCode of the race mode.
     */
    public int getCode() {
        return this.code;
    }
}
