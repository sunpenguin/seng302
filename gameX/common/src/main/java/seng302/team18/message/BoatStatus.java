package seng302.team18.message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum for the different types of boat statuses and their associated codes.
 */
public enum BoatStatus {
    UNDEFINED(0),
    PRE_START(1),
    RACING(2),
    FINISHED(3),
    DNS(4), // Did not start
    DNF(5), // Did not finish
    DSQ(6), // Disqualified
    OCS(7); //(On Course Side – across start line early)

    private int code;
    private static final Map<Integer, BoatStatus> CODE_MAP = Collections.unmodifiableMap(initializeMapping());

    BoatStatus(int code) {
        this.code = code;
    }

    /**
     * Returns the boat status associated with a getCode. If none exists then it returns null.
     *
     * @param code representing the boat status.
     * @return the boat status associated with a getCode.
     */
    // Code not used for anything right now. Can be removed if not needed.
    public static BoatStatus from(int code) {
        return CODE_MAP.get(code);
    }

    /**
     * Getter for the getCode of the boat status type.
     *
     * @return the getCode of the boat status type.
     */
    public int code() {
        return code;
    }

    /**
     * Creates a map between a getCode and its boat status type.
     *
     * @return a map between all codes and boat status type.
     */
    private static Map<Integer, BoatStatus> initializeMapping() {
        Map<Integer, BoatStatus> statusMap = new HashMap<>();
        for (BoatStatus status : BoatStatus.values()) {
            statusMap.put(status.code(), status);
        }
        return statusMap;
    }
}
