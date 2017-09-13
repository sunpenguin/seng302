package seng302.team18.model;

import seng302.team18.message.RequestType;

/**
 * Represents the different modes (race types) available
 */
public enum RaceMode {
    SPECTATION(RequestType.VIEWING.getCode()),
    RACE(RequestType.RACING.getCode()),
    CONTROLS_TUTORIAL(RequestType.CONTROLS_TUTORIAL.getCode()),
    GHOST(RequestType.GHOST.getCode()),
    ARCADE(RequestType.ARCADE.getCode()),
    BUMPER_BOATS(RequestType.BUMPER_BOATS.getCode()),
    CHALLENGE_MODE(RequestType.CHALLENGE_MODE.getCode()),
    RACE_TUTORIAL(7),
    START_TUTORIAL(8);

    private int code;


    RaceMode(int code) {
        this.code = code;
    }


    /**
     * @return the code associated with the race mode
     */
    public int getCode() {
        return this.code;
    }


    public boolean hasLives() {
        return this == BUMPER_BOATS;
    }
}
