package seng302.team18.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by david on 4/10/17.
 */
public enum AC35XMLMessageType {
    XML_REGATTA (5), XML_RACE (6), XML_BOATS (7);

    private int code;
    private static final Map<Integer, AC35XMLMessageType> CODE_MAP = Collections.unmodifiableMap(initializeMapping());


    private AC35XMLMessageType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static AC35XMLMessageType from(int code) {
        return CODE_MAP.get(code);
    }

    private static Map<Integer, AC35XMLMessageType> initializeMapping() {
        Map<Integer, AC35XMLMessageType> codeMap = new HashMap<>();
        for (AC35XMLMessageType type : AC35XMLMessageType.values()) {
            codeMap.put(type.code, type);
        }
        return codeMap;
    }
}
