package seng302.team18.message;

import seng302.team18.model.Projectile;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum for the different types of messages for the AC35 streaming protocol and their associated codes.
 */
public enum AC35MessageType {
    HEARTBEAT(1),
    XML_REGATTA(5),
    XML_RACE(6),
    XML_BOATS(7),
    RACE_STATUS(12),
    DISPLAY_TEXT_MESSAGE(20),
    YACHT_EVENT(29),
    XML_MESSAGE(26),
    RACE_START_STATUS(27),
    YACHT_ACTION_CODE(31),
    CHATTER_TEXT(36),
    BOAT_LOCATION(37),
    MARK_ROUNDING(38),
    COURSE_WIND(44),
    AVERAGE_WIND(47),
    // non ac35 codes
    REQUEST(55),
    ACCEPTANCE(56),
    BOAT_ACTION(100),
    COLOUR(105),
    NAME(106),
    POWER_UP(112),
    POWER_TAKEN(113),
    PROJECTILE_LOCATION(115),
    PROJECTILE_GONE(116),
    PROJECTILE_CREATION(117);

    private int code;
    private static final Map<Integer, AC35MessageType> CODE_MAP = Collections.unmodifiableMap(initializeMapping());


    /**
     * sets the type of the message
     *
     * @param code the value the getCode will be set to
     */
    AC35MessageType(int code) {
        this.code = code;
    }

    /**
     * Returns the AC35MessageType associated with a getCode. If none exists then it returns null.
     *
     * @param code representing the AC35MessageType.
     * @return the AC35MessageType associated with a getCode.
     */
    public static AC35MessageType from(int code) {
        return CODE_MAP.get(code);
    }

    /**
     * Getter for the getCode of the message type.
     *
     * @return the getCode of the message type.
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
