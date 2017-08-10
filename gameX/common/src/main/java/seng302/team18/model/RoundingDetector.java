package seng302.team18.model;

import seng302.team18.util.GPSCalculations;

/**
 * RoundingDetector
 */
public class RoundingDetector {

    private final static GPSCalculations GPS = new GPSCalculations();


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

        System.out.println(course.getMarkSequence().get(boat.getLegNumber()).getCompoundMark().getName() + " " + passed);

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
        System.out.println("" + inPreRounding + " " + inPostRounding);
        return inPreRounding && inPostRounding;
    }


    // TODO afj19 10/08/17: deal with fact that this is only for marks and thus the leg is pre-start or post-finish (add dbc precondition?)
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


    // TODO afj19 10/08/17: deal with fact that this is only for marks and thus the leg is pre-start or post-finish (add dbc precondition?)
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
        System.out.println("Checking if passing gate type: " + gateType.getType());
        boolean passed = false;
        if (gateType == MarkRounding.GateType.THROUGH_GATE) {
            passed = checkForThroughGate(boat, gate);
        } else if (gateType == MarkRounding.GateType.ROUND_THEN_THROUGH) {
            passed = checkForRoundThenThroughGate(boat, gate);
        } else if (gateType == MarkRounding.GateType.THROUGH_THEN_ROUND) {
            passed = checkForThroughThenRoundGate(boat, gate);
        } else if (gateType == MarkRounding.GateType.ROUND_BOTH_MAKRS) {
            // Not checking for this type as it should never be encountered
            passed = true;
        }
        return passed;
    }


    private boolean checkForThroughGate(Boat boat, MarkRounding gate) {
        double mark1ToBoatBearing = GPS.getBearing(gate.getCompoundMark().getMarks().get(0).getCoordinate(), boat.getCoordinate());
        double mark2ToBoatBearing = GPS.getBearing(gate.getCompoundMark().getMarks().get(1).getCoordinate(), boat.getCoordinate());
        double mark1ToMark2Bearing = GPS.getBearing(gate.getCompoundMark().getMarks().get(0).getCoordinate(), gate.getCompoundMark().getMarks().get(1).getCoordinate());
        double mark2ToMark1Bearing = GPS.getBearing(gate.getCompoundMark().getMarks().get(1).getCoordinate(), gate.getCompoundMark().getMarks().get(0).getCoordinate());
        double mark1toBoatPrevious = GPS.getBearing(gate.getCompoundMark().getMarks().get(0).getCoordinate(), boat.getPreviousCoordinate());

        boolean isInsideMark1 = GPS.isBetween(mark1ToBoatBearing, (mark1ToMark2Bearing - 90) % 360, (mark1ToMark2Bearing + 90) % 360);
        boolean isInsideMark2 = GPS.isBetween(mark2ToBoatBearing, (mark2ToMark1Bearing - 90) % 360, (mark2ToMark1Bearing + 90) % 360);
        boolean isPS = gate.getRoundingDirection().equals(MarkRounding.Direction.PS);
        boolean isBehindGate = GPS.isBetween(mark1ToBoatBearing, (mark1ToMark2Bearing + 180) % 360, mark1ToMark2Bearing) != isPS;
        boolean wasBehindGate = GPS.isBetween(mark1toBoatPrevious, (mark1ToMark2Bearing + 180) % 360, mark1ToMark2Bearing) != isPS;

        return isInsideMark1 && isInsideMark2 && isBehindGate && !wasBehindGate;
    }


    private boolean checkForRoundThenThroughGate(Boat boat, MarkRounding gate) {
        Mark mark1 = gate.getCompoundMark().getMarks().get(0);
        Mark mark2 = gate.getCompoundMark().getMarks().get(1);

        double mark1ToBoatBearing = GPS.getBearing(mark1.getCoordinate(), boat.getCoordinate());
        double mark2ToBoatBearing = GPS.getBearing(mark2.getCoordinate(), boat.getCoordinate());
        double mark1ToMark2Bearing = GPS.getBearing(mark1.getCoordinate(), mark2.getCoordinate());
        double mark2ToMark1Bearing = GPS.getBearing(mark2.getCoordinate(), mark1.getCoordinate());
        if (boat.getRoundZone() == Boat.RoundZone.ZONE1) {
            if (GPS.isBetween(mark1ToBoatBearing, (mark2ToMark1Bearing - 90) % 360, (mark2ToMark1Bearing + 90) % 360)) { // pre-rounding mark1
                double previousAngle = GPS.getBearing(mark1.getCoordinate(), boat.getPreviousCoordinate());
                if (GPS.isBetween(previousAngle, mark2ToMark1Bearing, mark1ToMark2Bearing) && !(GPS.isBetween(mark1ToBoatBearing, mark2ToMark1Bearing, mark1ToMark2Bearing))) {
                    boat.setRoundZone(Boat.RoundZone.ZONE2);
                }
            } else if (GPS.isBetween(mark2ToBoatBearing, (mark1ToMark2Bearing - 90) % 360, (mark1ToMark2Bearing + 90) % 360)) { // pre-rounding mark2
                double previousAngle = GPS.getBearing(mark1.getCoordinate(), boat.getPreviousCoordinate());
                if (GPS.isBetween(previousAngle, mark2ToMark1Bearing, mark1ToMark2Bearing) && !(GPS.isBetween(mark1ToBoatBearing, mark2ToMark1Bearing, mark1ToMark2Bearing))) {
                    boat.setRoundZone(Boat.RoundZone.ZONE2);
                }
            }
        } else if (boat.getRoundZone() == Boat.RoundZone.ZONE2) {
            if (!checkForThroughGate(boat, gate)) {
                boat.setRoundZone(Boat.RoundZone.ZONE1);
                return true;
            }
        }
        return false;
    }


    private boolean checkForThroughThenRoundGate(Boat boat, MarkRounding gate) {
        Mark mark1 = gate.getCompoundMark().getMarks().get(0);
        Mark mark2 = gate.getCompoundMark().getMarks().get(1);

        double mark1ToBoatBearing = GPS.getBearing(mark1.getCoordinate(), boat.getCoordinate());
        double mark2ToBoatBearing = GPS.getBearing(mark2.getCoordinate(), boat.getCoordinate());
        double mark1ToMark2Bearing = GPS.getBearing(mark1.getCoordinate(), mark2.getCoordinate());
        double mark2ToMark1Bearing = GPS.getBearing(mark2.getCoordinate(), mark1.getCoordinate());

        if (boat.getRoundZone() == Boat.RoundZone.ZONE1) {
            if (!checkForThroughGate(boat, gate)) {
                boat.setRoundZone(Boat.RoundZone.ZONE2);
                return false;
            }
        } else if (boat.getRoundZone() == Boat.RoundZone.ZONE2) {
            if (GPS.isBetween(mark1ToBoatBearing, (mark2ToMark1Bearing - 90) % 360, (mark2ToMark1Bearing + 90) % 360)) { //rounding mark 1
                double previousAngle = GPS.getBearing(mark1.getCoordinate(), boat.getPreviousCoordinate());
                if (GPS.isBetween(previousAngle, mark2ToMark1Bearing, mark1ToMark2Bearing) && !(GPS.isBetween(mark1ToBoatBearing, mark2ToMark1Bearing, mark1ToMark2Bearing))) {
                    boat.setRoundZone(Boat.RoundZone.ZONE1);
                    return true;
                }
            } else if (GPS.isBetween(mark2ToBoatBearing, (mark1ToMark2Bearing - 90) % 360, (mark1ToMark2Bearing + 90) % 360)) { //rounding mark 2
                double previousAngle = GPS.getBearing(mark1.getCoordinate(), boat.getPreviousCoordinate());
                if (GPS.isBetween(previousAngle, mark2ToMark1Bearing, mark1ToMark2Bearing) && !(GPS.isBetween(mark1ToBoatBearing, mark2ToMark1Bearing, mark1ToMark2Bearing))) {
                    boat.setRoundZone(Boat.RoundZone.ZONE1);
                    return true;
                }
            }
        }

        return false;
    }
}
