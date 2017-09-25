package seng302.team18.model;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Enum for the different types of yacht events in the AC35 streaming protocol and their associated codes.
 */
public enum YachtEventCode {
    OVER_START_LINE_EARLY((byte) 0x03),
    CLEAR_BEHIND_START_AFTER_EARLY((byte) 0x04),
    OCS_PENALTY_COMPLETE((byte) 0x22),
    BOAT_IN_COLLISION(((byte) 0x21)),
    BOAT_COLLIDE_WITH_MARK(((byte) 0x23)),
    ACTIVATED_SPEED_BOOST((byte) 0x25);

    private final byte code;


    private final static Map<Byte, YachtEventCode> MAPPING = initializeMapping();


    YachtEventCode(byte code) {
        this.code = code;
    }


    public byte getCode() {
        return code;
    }


    public static YachtEventCode ofCode(byte code) {
        return MAPPING.get(code);
    }


    private static Map<Byte, YachtEventCode> initializeMapping() {
        return Arrays.stream(values()).collect(Collectors.toMap(YachtEventCode::getCode, yachtEventCode -> yachtEventCode));
    }
}
