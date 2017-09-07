package seng302.team18.message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dhl25 on 3/09/17.
 */
public enum PowerType {
    
    SPEED(0);
    
    private int code;
    private static final Map<Integer, PowerType> CODE_MAP = Collections.unmodifiableMap(initializeMapping());

    PowerType(int code) {
        this.code = code;
    }

    public static PowerType from(int code) {
        return CODE_MAP.get(code);
    }
    

    public int getCode() {
        return code;
    }


    /**
     * Creates a map between a code and its message type.
     *
     * @return a map between all codes and their message type.
     */
    private static Map<Integer, PowerType> initializeMapping() {
        Map<Integer, PowerType> codeMap = new HashMap<>();
        for (PowerType type : PowerType.values()) {
            codeMap.put(type.code, type);
        }
        return codeMap;
    }
}
