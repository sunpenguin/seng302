package seng302.team18.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Holds information about "boats", as conveyed in the AC35 protocol. Note that boats here means all boats (potentially
 * including marks, umpires and more), rather than just racing yachts.
 */
public enum BoatType {
    _UNKNOWN(""),
    YACHT("Yacht"),
    RC("RC"),
    MARK("Mark"),
    UMPIRE("Umpire"),
    MARSHALL("Marshall"),
    PIN("Pin");

    private final static Map<String, BoatType> STRING_BOAT_TYPE_MAP = Collections.unmodifiableMap(initializeMapping());

    /**
     * The value name of the boat type, as used in Boats.xml of the AC35 SDI protocol.
     */
    private final String typeName;

    BoatType(String typeName) {
        this.typeName = typeName;
    }

    public String toString() {
        return typeName;
    }

    private static Map<String, BoatType> initializeMapping() {
        return Arrays.stream(values()).collect(Collectors.toMap(BoatType::toString, bt -> bt));
    }

    public static BoatType ofTypeName(String typeName) {
        return STRING_BOAT_TYPE_MAP.get(typeName);
    }
}
