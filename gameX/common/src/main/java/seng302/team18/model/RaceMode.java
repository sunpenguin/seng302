package seng302.team18.model;

import seng302.team18.message.RequestType;

/**
 * Enum for the different modes.
 */
public enum RaceMode {
    SPECTATION(RequestType.VIEWING.code()),
    RACE(RequestType.RACING.code()),
    CONTROLS_TUTORIAL(RequestType.CONTROLS_TUTORIAL.code()),
    GHOST(RequestType.GHOST.code()),
    RACE_TUTORIAL(4),
    START_TUTORIAL(5),
    ARCADE(6),
    BUMPER_BOATS(7),
    CHALLENGE_MODE(8);


    private int code;


    RaceMode(int code) {
        this.code = code;
    }


    /**
     * Getter for the code of the race mode.
     *
     * @return the code of the race mode.
     */
    public int getCode() {
        return this.code;
    }
}
