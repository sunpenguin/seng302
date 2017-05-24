package seng302.team18.message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by david on 5/19/17.
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

    private BoatStatus(int code) {
        this.code = code;
    }

    public BoatStatus ofCode(int code) {
        return CODE_MAP.get(code);
    }

    public int code() {
        return code;
    }

    private static Map<Integer, BoatStatus> initializeMapping() {
        Map<Integer, BoatStatus> statusMap = new HashMap<>();
        for (BoatStatus status : BoatStatus.values()) {
            statusMap.put(status.code(), status);
        }
        return statusMap;
    }
}
