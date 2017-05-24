package seng302.team18.message;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum for the different types of race status for the AC35 streaming protocol and their associated codes.
 */
public enum RaceStatus {
    NOT_ACTIVE(0), WARNING(1), PREPARATORY(2), STARTED(3), FINISHED(4), RETIRED(5),
    ABANDONED(6), POSTPONED(7), TERMINATED(8), RACE_START_NOT_SET(9), PRESTART(10);

    private int code;

    RaceStatus(int code) {
        this.code = code;
    }

    /**
     * Gets the codes used in the pre-race.
     *
     * @return a list of codes used in the pre-race.
     */
    public static List<Integer> preRaceCodes() {
        List<Integer> codes = new ArrayList<>();
        codes.add(WARNING.getCode());
        codes.add(PRESTART.getCode());
        return codes;
    }

    /**
     * Getter for the code of the race status type.
     *
     * @return the code of the race status type.
     */
    public int getCode() {
        return this.code;
    }
}
