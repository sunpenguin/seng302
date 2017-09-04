package seng302.team18.message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum for representing different requests when new players join a server.
 */
public enum RequestType {
    VIEWING(0),
    RACING(1),
    CONTROLS_TUTORIAL(2),
    GHOST(3),
    ARCADE(5),
    FAILURE_CLIENT_TYPE(18);


    private int code;
    private static final Map<Integer, RequestType> CODE_MAP = Collections.unmodifiableMap(initializeMapping());


    RequestType(int code) {
        this.code = code;
    }


    /**
     * Getter for the code of the request type.
     *
     * @return the code of the request type.
     */
    public int getCode() {
        return this.code;
    }


    /**
     * Returns if the code is an error code.
     *
     * @return if error.
     */
    public boolean isError() {
        return code >= 16;
    }


    /**
     * Returns the request type associated with a code. If none exists then it returns null.
     *
     * @param code representing the boat status.
     * @return the request type associated with a code.
     */
    public static RequestType from(int code) {
        return CODE_MAP.get(code);
    }


    /**
     * Creates a map between a code and its request type.
     *
     * @return a map between all codes and request type.
     */
    private static Map<Integer, RequestType> initializeMapping() {
        Map<Integer, RequestType> requestMap = new HashMap<>();
        for (RequestType request : RequestType.values()) {
            requestMap.put(request.getCode(), request);
        }
        return requestMap;
    }
}
