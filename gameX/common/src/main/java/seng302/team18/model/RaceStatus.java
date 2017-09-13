package seng302.team18.model;

import java.util.*;

/**
 * Enum for the different types of race status for the AC35 streaming protocol and their associated codes.
 */
public enum RaceStatus {
    NOT_ACTIVE(0, 0),
    WARNING(1, 2),
    PREPARATORY(2, 3),
    STARTED(3, 4),
    FINISHED(4, 5),
    RETIRED(5, 5),
    ABANDONED(6, 5),
    POSTPONED(7, 0),
    TERMINATED(8, 5),
    RACE_START_NOT_SET(9, 0),
    PRESTART(10, 1);

    private int code;
    private int order;
    private final static Map<Integer, RaceStatus> CODE_MAP = Collections.unmodifiableMap(initializeMapping());

    RaceStatus(int code, int order) {
        this.code = code;
        this.order = order;
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
     * Getter for the code of the race status type.
     *
     * @return the code of the race status type.
     */
    public int getCode() {
        return this.code;
    }


    public static RaceStatus from(int code) {
        return CODE_MAP.get(code);
    }


    private static Map<Integer, RaceStatus> initializeMapping() {
        Map<Integer, RaceStatus> statusMap = new HashMap<>();
        for (RaceStatus status : RaceStatus.values()) {
            statusMap.put(status.getCode(), status);
        }
        return statusMap;
    }


    /**
     * Returns true if the current status is after the given status.
     * @param status to compare to
     * @return if the current status is after the given status
     */
    public boolean isAfter(RaceStatus status) {
        return this.order > status.order;
    }
}
