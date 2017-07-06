package seng302.team18.message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum for the different types of messages for the AC35 streaming protocol and their associated codes.
 */
public enum AC35MessageType {
    YACHT_EVENT(29), BOAT_LOCATION(37), MARK_ROUNDING(38),
    XML_MESSAGE(26), XML_REGATTA(5), XML_RACE(6), XML_BOATS(7),
    HEARTBEAT(1), RACE_STATUS(12), DISPLAY_TEXT_MESSAGE(20), RACE_START_STATUS(27), YACHT_ACTION_CODE(31),
    CHATTER_TEXT(36), COURSE_WIND(44), AVERAGE_WIND(47),
    REQUEST(55), ACCEPTANCE(56);

    private int code;
    private static final Map<Integer, AC35MessageType> CODE_MAP = Collections.unmodifiableMap(initializeMapping());


    AC35MessageType(int code) {
        this.code = code;
    }

    /**
     * Returns the AC35MessageType associated with a code. If none exists then it returns null.
     *
     * @param code representing the AC35MessageType.
     * @return the AC35MessageType associated with a code.
     */
    public static AC35MessageType from(int code) {
        return CODE_MAP.get(code);
    }

    /**
     * Getter for the code of the message type.
     *
     * @return the code of the message type.
     */
    public int getCode() {
        return code;
    }

    /**
     * Creates a map between a code and its message type.
     *
     * @return a map between all codes and their message type.
     */
    private static Map<Integer, AC35MessageType> initializeMapping() {
        Map<Integer, AC35MessageType> codeMap = new HashMap<>();
        for (AC35MessageType type : AC35MessageType.values()) {
            codeMap.put(type.code, type);
        }
        return codeMap;
    }

}
