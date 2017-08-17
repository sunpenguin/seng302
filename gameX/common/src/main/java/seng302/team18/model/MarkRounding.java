package seng302.team18.model;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Stores information about the rounding of a compound mark in a course
 */
public class MarkRounding {
    private final int sequenceNumber; // This should be stored by their position relative to each other in an ordered collection
    private final CompoundMark compoundMark;
    private final Direction roundingDirection;
    private final int zoneSize;
    private double passAngle;
    private GateType gateType;


    /**
     * Constructs a new MarkRounding object
     *
     * @param sequenceNumber    the order of this rounding in the course
     * @param compoundMark      the compound mark rounded
     * @param roundingDirection the direction to round the compound mark
     * @param zoneSize          the size of the rounding zone
     */
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MarkRounding that = (MarkRounding) o;

        if (sequenceNumber != that.sequenceNumber) return false;
        if (zoneSize != that.zoneSize) return false;
        if (compoundMark != null ? !compoundMark.equals(that.compoundMark) : that.compoundMark != null) return false;
        return roundingDirection == that.roundingDirection;
    }


    @Override
    public int hashCode() {
        int result = sequenceNumber;
        result = 31 * result + (compoundMark != null ? compoundMark.hashCode() : 0);
        result = 31 * result + (roundingDirection != null ? roundingDirection.hashCode() : 0);
        result = 31 * result + zoneSize;
        return result;
    }


    /**
     * The direction that boat should take a mark rounding
     */
    public enum Direction {
        PORT("Port"),
        STARBOARD("Stbd"),
        SP("SP"),
        PS("PS");

        private final static Map<String, Direction> MAPPING = initializeMapping();
        private final String value;


        Direction(String value) {
            this.value = value;
        }


        /**
         * Get the enum value represented by the given string. Used to parse the enum from the string in the AC35 SDI
         * protocol
         *
         * @param value the string value
         * @return the enum value
         */
        public static Direction fromValue(String value) {
            return MAPPING.get(value);
        }


        private static Map<String, Direction> initializeMapping() {
            return Arrays.stream(values()).collect(Collectors.toMap(Direction::toString, rt -> rt));
        }


        @Override
        public String toString() {
            return value;
        }
    }


    public enum GateType {
        THROUGH_THEN_ROUND("through gate on arrival, round gate on departure"),
        THROUGH_GATE("straight through gate"),
        ROUND_BOTH_MARKS("s-bend gate (round both marks)"),
        ROUND_THEN_THROUGH("round gate on arrival, through gate on departure");

        private String gateType;

        GateType(String type) {
            gateType = type;
        }


        public String getType() {
            return gateType;
        }
    }


    public double getPassAngle() {
        return passAngle;
    }

    public void setPassAngle(double passAngle) {
        this.passAngle = passAngle;
    }


    public GateType getGateType() {
        return gateType;
    }

    public void setGateType(GateType gateType) {
        this.gateType = gateType;
    }
}
