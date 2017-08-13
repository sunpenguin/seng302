package seng302.team18.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum for the different types of boat statuses and their associated codes.
 */
public enum BoatStatus {
    UNDEFINED(0x00),
    PRE_START(0x01),
    RACING(0x02),
    FINISHED(0x03),
    DNS(0x04), // Did not start
    DNF(0x05), // Did not finish
    DSQ(0x06), // Disqualified
    OCS(0x07); // (On Course Side – across start line early)

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
