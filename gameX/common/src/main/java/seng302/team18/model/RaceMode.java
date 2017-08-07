package seng302.team18.model;

/**
 * Enum for the different modes.
 */
public enum RaceMode {
    RACE(0),
    CONTROLS_TUTORIAL(1),
    RACE_TUTORIAL(2),
    START_TUTORIAL(3);


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
