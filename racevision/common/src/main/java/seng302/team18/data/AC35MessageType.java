package seng302.team18.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dhl25 on 10/04/17.
 */
public enum AC35MessageType {
    XML_MESSAGE (26), YACHT_EVENT (29), BOAT_LOCATION (37),
    XML_REGATTA (5), XML_RACE (6), XML_BOATS (7),
    // These are the other message types we are not dealing with right now.
    HEARTBEAT (1), RACE_STATUS (12), DISPLAY_TEXT_MESSAGE (20), RACE_START_STATUS (27), YACHT_ACTION_CODE (31),
    CHATTER_TEXT (36), MARK_ROUNDING (38), COURSE_WIND (44), AVERAGE_WIND (47);

    private int code;
    private static final Map<Integer, AC35MessageType> CODE_MAP = Collections.unmodifiableMap(initializeMapping());


    AC35MessageType(int code) {
        this.code = code;
    }

    public static AC35MessageType from(int code) {
        return CODE_MAP.get(code);
    }

    public int getCode() {
        return code;
    }

    private static Map<Integer, AC35MessageType> initializeMapping() {
        Map<Integer, AC35MessageType> codeMap = new HashMap<>();
        for (AC35MessageType type : AC35MessageType.values()) {
            codeMap.put(type.code, type);
        }
        return codeMap;
    }

}
