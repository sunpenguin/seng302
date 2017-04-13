package seng302.team18.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dhl25 on 10/04/17.
 */
public enum AC35MessageType {
    XML_MESSAGE (26), YACHT_EVENT (29), BOAT_LOCATION (37), XML_REGATTA (5), XML_RACE (6), XML_BOATS (7);

    private int code;
    private static final Map<Integer, AC35MessageType> CODE_MAP = Collections.unmodifiableMap(initializeMapping());


    private AC35MessageType(int code) {
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
