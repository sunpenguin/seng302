package seng302.team18.model;

import seng302.team18.util.GPSCalculator;

/**
 * RoundingDetector
 */
public class RoundingDetector {

    private final static GPSCalculator GPS = new GPSCalculator();


    /**
     * Checks if a boat has past its destination mark.
     *
     * @param boat the boat to be checked
     * @return if has passed its destination mark
     */
    public boolean hasPassedDestination(Boat boat, Course course) {
        // TODO afj19 10/08/17: check that this is never called once the boat is finished, or make sure that we won't get index exceptions accessing the roundings
        int legNumber = boat.getLegNumber();
        MarkRounding currentRounding = course.getMarkSequence().get(legNumber);
        boolean passed = false;

        if (currentRounding.getCompoundMark().getMarks().size() == CompoundMark.MARK_SIZE) { //if boat is rounding a mark
            MarkRounding previousRounding = course.getMarkSequence().get(legNumber - 1);
            MarkRounding nextRounding = course.getMarkSequence().get(legNumber + 1);
            passed = hasPassedMark(boat, previousRounding, currentRounding, nextRounding);
        } else if (currentRounding.getCompoundMark().getMarks().size() == CompoundMark.GATE_SIZE) {
            passed = hasPassedGate(boat, currentRounding);
        }

        return passed;
    }


    /**
     * Method to check if a boat has passed the mark it is heading too
     *
     * @param boat the boat in question
     * @return true if the boat has passed its destination mark
     */
    private boolean hasPassedMark(Boat boat, MarkRounding past, MarkRounding current, MarkRounding next) {
        boolean inPreRounding = inPreRoundingZone(boat.getPreviousCoordinate(), past, current);
        boolean inPostRounding = inPostRoundingZone(boat.getCoordinate(), current, next);
        return inPreRounding && inPostRounding;
    }


    @SuppressWarnings("Duplicates")
    private boolean inPreRoundingZone(Coordinate coordinate, MarkRounding pastRounding, MarkRounding currentRounding) {
        double markToBoatHeading = GPS.getBearing(currentRounding.getCompoundMark().getCoordinate(), coordinate);
        double entryBearing = GPS.getBearing(currentRounding.getCompoundMark().getCoordinate(), pastRounding.getCompoundMark().getCoordinate());

        MarkRounding.Direction direction = currentRounding.getRoundingDirection();

        if (direction == MarkRounding.Direction.PORT) {
            return GPS.isBetween(markToBoatHeading, currentRounding.getPassAngle(), entryBearing);
        } else if (direction == MarkRounding.Direction.STARBOARD) {
            return GPS.isBetween(markToBoatHeading, entryBearing, currentRounding.getPassAngle());
        }

        return false;
    }


    @SuppressWarnings("Duplicates")
    private boolean inPostRoundingZone(Coordinate coordinate, MarkRounding currentRounding, MarkRounding nextRounding) {
        double markToBoatHeading = GPS.getBearing(currentRounding.getCompoundMark().getCoordinate(), coordinate);
        double exitBearing = GPS.getBearing(currentRounding.getCompoundMark().getCoordinate(), nextRounding.getCompoundMark().getCoordinate());

        MarkRounding.Direction direction = currentRounding.getRoundingDirection();

        if (direction == MarkRounding.Direction.PORT) {
            return GPS.isBetween(markToBoatHeading, exitBearing, currentRounding.getPassAngle());
        } else if (direction == MarkRounding.Direction.STARBOARD) {
            return GPS.isBetween(markToBoatHeading, currentRounding.getPassAngle(), exitBearing);
        }

        return false;
    }


    /**
     * Method to check if a boat has passed the gate it is heading too
     *
     * @param boat the boat in question
     * @return true if the boat has passed its destination gate
     */
    private boolean hasPassedGate(Boat boat, MarkRounding gate) {
        MarkRounding.GateType gateType = gate.getGateType();
        boolean passed = false;
        if (gateType == MarkRounding.GateType.THROUGH_GATE) {
            passed = checkForThroughGate(boat, gate);
        } else if (gateType == MarkRounding.GateType.ROUND_THEN_THROUGH) {
            passed = checkForThroughAndRoundGate(boat, gate, false);
        } else if (gateType == MarkRounding.GateType.THROUGH_THEN_ROUND) {
            passed = checkForThroughAndRoundGate(boat, gate, true);
        } else if (gateType == MarkRounding.GateType.ROUND_BOTH_MARKS) {
            // Not checking for this type as it should never be encountered
            passed = true;
        }
        return passed;
    }


    private boolean checkForThroughGate(Boat boat, MarkRounding gate) {
        double mark1ToBoatBearing = GPS.getBearing(gate.getCompoundMark().getMarks().get(0).getCoordinate(), boat.getCoordinate());
        double mark1ToMark2Bearing = GPS.getBearing(gate.getCompoundMark().getMarks().get(0).getCoordinate(), gate.getCompoundMark().getMarks().get(1).getCoordinate());
        double mark1toBoatPrevious = GPS.getBearing(gate.getCompoundMark().getMarks().get(0).getCoordinate(), boat.getPreviousCoordinate());

        boolean isPS = gate.getRoundingDirection().equals(MarkRounding.Direction.PS);
        boolean isBehindGate = GPS.isBetween(mark1ToBoatBearing, (mark1ToMark2Bearing + 180) % 360, mark1ToMark2Bearing) == isPS;
        boolean wasBehindGate = GPS.isBetween(mark1toBoatPrevious, (mark1ToMark2Bearing + 180) % 360, mark1ToMark2Bearing) == isPS;

        return isInsideGate(boat, gate.getCompoundMark()) && isBehindGate && !wasBehindGate;
    }


    /**
     * Check if the boat has rounded the through-then-round/round-then-through gate
     *
     * @param boat         the boat
     * @param gate         the gate
     * @param throughFirst true if the gate is a through-then-round gate, false if the gate is a round-then-through gate
     * @return true if the boat's latest movement has caused it to round the gate, else false
     */
    private boolean checkForThroughAndRoundGate(Boat boat, MarkRounding gate, boolean throughFirst) {
        boolean isPS = gate.getRoundingDirection().equals(MarkRounding.Direction.PS);

        Mark mark1 = gate.getCompoundMark().getMarks().get(0);
        Mark mark2 = gate.getCompoundMark().getMarks().get(1);

        double mark1ToMark2Bearing = GPS.getBearing(mark1.getCoordinate(), mark2.getCoordinate());
        double mark2ToMark1Bearing = (mark1ToMark2Bearing + 180) % 360;

        // The front of a round-then-through gate is the side that boats approach and leave from
        double previousAngle = GPS.getBearing(mark1.getCoordinate(), boat.getPreviousCoordinate());
        boolean wasInFront = GPS.isBetween(previousAngle, mark1ToMark2Bearing, mark2ToMark1Bearing) == isPS == throughFirst;

        // The back of a round-then-through gate is the side that boats round
        double currentAngle = GPS.getBearing(mark1.getCoordinate(), boat.getCoordinate());
        boolean isBehind = GPS.isBetween(currentAngle, mark2ToMark1Bearing, mark1ToMark2Bearing) == isPS == throughFirst;

        boolean isInsideGate = isInsideGate(boat, gate.getCompoundMark());

        boolean hasRounded = false;

        if ((boat.getRoundZone() == Boat.RoundZone.ZONE1) && wasInFront && isBehind && (isInsideGate == throughFirst)) {
            boat.setRoundZone(Boat.RoundZone.ZONE2);
        } else if ((boat.getRoundZone() == Boat.RoundZone.ZONE2) && !wasInFront && !isBehind && (isInsideGate != throughFirst)) {
            boat.setRoundZone(Boat.RoundZone.ZONE1);
            hasRounded = true;
        }

        return hasRounded;
    }


    /**
     * Checks if the boat's current position is in the rectangular region with sides passing through the gate's two
     * marks, and ends parallel to the gate at an infinite distance
     *
     * @param boat the boat
     * @param gate the gate
     * @return true if the boat is within the region, else false
     */
    private boolean isInsideGate(Boat boat, CompoundMark gate) {
        double mark1ToBoatBearing = GPS.getBearing(gate.getMarks().get(0).getCoordinate(), boat.getCoordinate());
        double mark2ToBoatBearing = GPS.getBearing(gate.getMarks().get(1).getCoordinate(), boat.getCoordinate());
        double mark1ToMark2Bearing = GPS.getBearing(gate.getMarks().get(0).getCoordinate(), gate.getMarks().get(1).getCoordinate());
        double mark2ToMark1Bearing = (mark1ToMark2Bearing + 180) % 360;

        boolean isInsideMark1 = GPS.isBetween(mark1ToBoatBearing, (mark1ToMark2Bearing - 90) % 360, (mark1ToMark2Bearing + 90) % 360);
        boolean isInsideMark2 = GPS.isBetween(mark2ToBoatBearing, (mark2ToMark1Bearing - 90) % 360, (mark2ToMark1Bearing + 90) % 360);
        return isInsideMark1 && isInsideMark2;
    }
}
