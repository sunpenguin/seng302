package seng302.team18.model;

import seng302.team18.message.RequestType;

/**
 * Represents the different modes (race types) available
 */
public enum RaceMode {
    SPECTATION(RequestType.VIEWING.getCode(), "SPECTATE MODE"),
    RACE(RequestType.RACING.getCode(), "STANDARD RACE"),
    CONTROLS_TUTORIAL(RequestType.CONTROLS_TUTORIAL.getCode(), "CONTROLS TUTORIAL"),
    GHOST(RequestType.GHOST.getCode(), "CASPER THE FRIENDLY GHOST"),
    ARCADE(RequestType.ARCADE.getCode(), "ARCADE RACE"),
    BUMPER_BOATS(RequestType.BUMPER_BOATS.getCode(), "ARENA MODE"),
    CHALLENGE_MODE(RequestType.CHALLENGE_MODE.getCode(), "CHALLENGE MODE"),
    START_TUTORIAL(8, "START TUTORIAL");

    private int code;
    private String name;


    RaceMode(int code, String name) {
        this.code = code;
        this.name = name;
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


    @Override
    public String toString() {
        return name;
    }
}
