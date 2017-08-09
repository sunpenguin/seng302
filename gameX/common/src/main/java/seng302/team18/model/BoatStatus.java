package seng302.team18.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Enumerated type for the status of a boat in a race
 */
public enum BoatStatus {
    UNDEFINED(0), PRESTART(1), RACING(2), FINISHED(3), DNS(4), DNF(5), DSQ(6), OCS(7);

    private int code;
    private final static Map<Integer, BoatStatus> STATUS_MAP = Collections.unmodifiableMap(initializeMapping());


    BoatStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public BoatStatus getStatus(int code) {
        return STATUS_MAP.get(code);
    }

    private static Map<Integer, BoatStatus> initializeMapping() {
        return Arrays.stream(values()).collect(Collectors.toMap(BoatStatus::getCode, bt -> bt));
    }


}
