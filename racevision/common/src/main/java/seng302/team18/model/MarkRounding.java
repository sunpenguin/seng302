package seng302.team18.model;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Stores the direction taken to round a mark.
 */
public class MarkRounding {
    private final int sequenceNumber;
    private final CompoundMark compoundMark;
    private final Direction roundingDirection;
    private final int zoneSize;

    public MarkRounding(int sequenceNumber, CompoundMark compoundMark, Direction roundingDirection, int zoneSize) {
        this.sequenceNumber = sequenceNumber;
        this.compoundMark = compoundMark;
        this.roundingDirection = roundingDirection;
        this.zoneSize = zoneSize;
    }

    public CompoundMark getCompoundMark() {
        return compoundMark;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public Direction getRoundingDirection() {
        return roundingDirection;
    }

    public int getZoneSize() {
        return zoneSize;
    }

    public enum Direction {
        PORT("Port"),
        STARBOARD("Stbd"),
        SP("SP"),
        PS("PS");

        private final String value;

        private final static Map<String, Direction> MAPPING = initializeMapping();

        Direction(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Direction fromValue(String value) {
            return MAPPING.get(value);
        }

        private static Map<String, Direction> initializeMapping() {
            return Arrays.stream(values()).collect(Collectors.toMap(Direction::getValue, rt -> rt));
        }
    }
}
