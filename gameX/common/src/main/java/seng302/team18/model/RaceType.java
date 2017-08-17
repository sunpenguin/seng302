package seng302.team18.model;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum RaceType {
    MATCH("Match", (byte) 0x01),
    FLEET("Fleet", (byte) 0x02);

    private final static Map<String, RaceType> NAME_MAPPING = initializeMapping();

    private final String value;
    private final byte code;


    RaceType(String value, byte code) {
        this.value = value;
        this.code = code;
    }


    public static RaceType fromValue(String value) {
        return NAME_MAPPING.get(value);
    }


    private static Map<String, RaceType> initializeMapping() {
        return Arrays.stream(values()).collect(Collectors.toMap(RaceType::toString, rt -> rt));
    }


    @Override
    public String toString() {
        return value;
    }


    public byte getCode() {
        return code;
    }
}
