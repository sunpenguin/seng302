package seng302.team18.model;

import java.util.*;

/**
 * Enum for the different types of race status for the AC35 streaming protocol and their associated codes.
 */
public enum RaceStatus {
    NOT_ACTIVE(0), WARNING(1), PREPARATORY(2), STARTED(3), FINISHED(4), RETIRED(5),
    ABANDONED(6), POSTPONED(7), TERMINATED(8), RACE_START_NOT_SET(9), PRESTART(10);

    private int code;
    private final static Map<Integer, RaceStatus> CODE_MAP = Collections.unmodifiableMap(initializeMapping());

    RaceStatus(int code) {
        this.code = code;
    }

    /**
     * Gets the codes used in the pre-race.
     *
     * @return a list of codes used in the pre-race.
     */
    public static List<Integer> nonPreRaceCodes() {
        List<Integer> codes = Arrays.asList(
                NOT_ACTIVE.getCode(), PREPARATORY.getCode(), STARTED.getCode(), FINISHED.getCode(), RETIRED.getCode(),
                ABANDONED.getCode(), POSTPONED.getCode(), TERMINATED.getCode(), RACE_START_NOT_SET.getCode()
        );
        return codes;
    }

    /**
     * Getter for the getCode of the race status type.
     *
     * @return the getCode of the race status type.
     */
    public int getCode() {
        return this.code;
    }


    public static RaceStatus ofCode(int code) {
        return CODE_MAP.get(code);
    }


    private static Map<Integer, RaceStatus> initializeMapping() {
        Map<Integer, RaceStatus> statusMap = new HashMap<>();
        for (RaceStatus status : RaceStatus.values()) {
            statusMap.put(status.getCode(), status);
        }
        return statusMap;
    }
}
