package seng302.team18.model;

import seng302.team18.util.GPSCalculations;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * A class which represents a course that is used in a race event.
 */
public class Course {

    private List<CompoundMark> compoundMarks;
    private List<Leg> legs;
    private double windDirection;
    private double windSpeed;
    private List<BoundaryMark> boundaries;
    private Coordinate centralCoordinate;
    private ZoneId timeZone;
    private List<MarkRounding> markRoundings;
    private String name = "";

    public Course(Collection<CompoundMark> marks, Collection<BoundaryMark> boundaries, double windDirection, double windSpeed,
                  ZoneId timeZone, List<MarkRounding> markRoundings) {
        this.compoundMarks = new ArrayList<>(marks);
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.boundaries = new ArrayList<>(boundaries);
        this.timeZone = timeZone;
        this.markRoundings = markRoundings;
        centralCoordinate = new Coordinate(0d, 0d);
        initializeCourse();
    }

    public Course() {
        legs = new ArrayList<>();
        compoundMarks = new ArrayList<>();
        boundaries = new ArrayList<>();
        markRoundings = new ArrayList<>();
        timeZone = ZoneId.systemDefault();
        windDirection = 0d;
        windSpeed = 0d;
        centralCoordinate = new Coordinate(0d, 0d);
    }

    /**
     * A getter for the CompoundMarks in the course
     *
     * @return the list of CompoundMarks
     */
    public List<CompoundMark> getCompoundMarks() {
        return compoundMarks;
    }

    public void setCompoundMarks(Collection<CompoundMark> compoundMarks) {
        this.compoundMarks.clear();
        this.compoundMarks.addAll(compoundMarks);
    }

    public void setMarkRoundings(Collection<MarkRounding> markRoundings) {
        this.markRoundings.clear();
        this.markRoundings.addAll(markRoundings);
        initializeCourse();
    }

    public List<MarkRounding> getMarkRoundings() {
        return markRoundings;
    }

    public double getWindDirection() {
        return windDirection;
    }


    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public List<Mark> getMarks() {
        List<Mark> marks = new ArrayList<>();
        for (CompoundMark cMark : compoundMarks) {
            marks.addAll(cMark.getMarks());
        }
        return marks;
    }

    public List<BoundaryMark> getBoundaries() {
        return new ArrayList<>(boundaries);
    }

    public void setBoundaries(Collection<BoundaryMark> boundaries) {
        this.boundaries.clear();
        this.boundaries.addAll(boundaries);
    }

    public void setTimeZone(ZoneId timeZone) {
        this.timeZone = timeZone;
    }

    public ZoneId getTimeZone() {
        return timeZone;
    }

    public void setCentralCoordinate(Coordinate centralCoordinate) {
        if (this.centralCoordinate.getLatitude() == 0d && this.centralCoordinate.getLongitude() == 0d) {
            this.centralCoordinate = centralCoordinate;
        }
    }

    public Coordinate getCentralCoordinate() {
        return centralCoordinate;
    }

    public List<Leg> getLegs() {
        return legs;
    }


    public Leg getNextLeg(int legNumber) {
        if (legNumber == legs.size()) {
            return legs.get(legNumber - 1);
        }
        return legs.get(legNumber);
    }


    /**
     * Initializes the legs of the course to be in the order of the compound marks as they appear in the list.
     * Then adds the pass angle to each compound mark (if you do not know what pass angle is talk to Alice).
     */
    private void initializeCourse() {
        legs = new ArrayList<>();
        for (int i = 0; i < markRoundings.size() - 1; i++) {
            MarkRounding dep = markRoundings.get(i);
            MarkRounding dest = markRoundings.get(i + 1);
            Leg currentLeg = new Leg(dep, dest, markRoundings.get(i).getSequenceNumber());
            legs.add(currentLeg);
        }
        for (int i = 1; i < markRoundings.size() - 1; i++) {
            MarkRounding previous = markRoundings.get(i - 1);
            MarkRounding current = markRoundings.get(i);
            MarkRounding future = markRoundings.get(i + 1);
            if (current.getCompoundMark().getMarks().size() == CompoundMark.MARK_SIZE) {
                double previousAngle = GPSCalculations.getBearing(previous.getCompoundMark().getCoordinate(), current.getCompoundMark().getCoordinate());
                double futureAngle = GPSCalculations.getBearing(future.getCompoundMark().getCoordinate(), current.getCompoundMark().getCoordinate());
                current.setPassAngle(((previousAngle + futureAngle) / 2d));
            }else if (current.getCompoundMark().getMarks().size() == CompoundMark.GATE_SIZE) {
                setGateType(previous, current, future);
//                Mark mark1 = current.getCompoundMark().getMarks().get(0);
//                Mark mark2 = current.getCompoundMark().getMarks().get(1);
//                double bearingBetweenMarks = GPSCalculations.getBearing(mark1.getCoordinate(), mark2.getCoordinate());

            }
        }
    }

    /**
     * Sets the gate type for a mark rounding that is a gate.
     * Will be set to a value in the GateType enum
     * @param previousMark mark before the gate
     * @param currentMark gate
     * @param futureMark mark after the gate
     */
    private void setGateType(MarkRounding previousMark, MarkRounding currentMark, MarkRounding futureMark) {
        Mark mark1 = currentMark.getCompoundMark().getMarks().get(0);
        Mark mark2 = currentMark.getCompoundMark().getMarks().get(1);
        GPSCalculations calculator = new GPSCalculations();
        double bearingBetweenMarks = GPSCalculations.getBearing(mark1.getCoordinate(), mark2.getCoordinate());
        double oppisiteBearing = (bearingBetweenMarks +180) % 360;
        double bearingToFuture = GPSCalculations.getBearing(currentMark.getCompoundMark().getCoordinate(), futureMark.getCompoundMark().getCoordinate());
        double bearingToPrevious = GPSCalculations.getBearing(currentMark.getCompoundMark().getCoordinate(), previousMark.getCompoundMark().getCoordinate());

        boolean futureOnExitSide = false;
        boolean previousOnExitSide = false;
        if (currentMark.getRoundingDirection() == MarkRounding.Direction.SP) {
            if (calculator.isBetween(bearingToFuture, oppisiteBearing, bearingBetweenMarks)) {
                futureOnExitSide = true;
            } else {
                futureOnExitSide = false;
            }
            if (calculator.isBetween(bearingToPrevious, oppisiteBearing, bearingBetweenMarks)) {
                previousOnExitSide = true;
            } else {
                previousOnExitSide = false;
            }
        } else if (currentMark.getRoundingDirection() == MarkRounding.Direction.PS) {
            if (calculator.isBetween(bearingToFuture, oppisiteBearing, bearingBetweenMarks)) {
                futureOnExitSide = false;
            } else {
                futureOnExitSide = true;
            }
            if (calculator.isBetween(bearingToPrevious, oppisiteBearing, bearingBetweenMarks)) {
                previousOnExitSide = false;
            } else {
                previousOnExitSide = true;
            }
        }

        if (futureOnExitSide && previousOnExitSide) {
            currentMark.setGateType(MarkRounding.GateType.TGFSS);
        }else if (futureOnExitSide && !previousOnExitSide) {
            currentMark.setGateType(MarkRounding.GateType.TG);
        }else if (!futureOnExitSide && previousOnExitSide) {
            currentMark.setGateType(MarkRounding.GateType.DRG);
        }else if (!futureOnExitSide && !previousOnExitSide) {
            currentMark.setGateType(MarkRounding.GateType.RG);
        }
    }

    /**
     * Method find a leg with the correct legNumber from the courses legs.
     *
     * @param legNumber integer, the leg number of the leg to be found.
     * @return Leg with the same leg number as the integer given.
     */
    public Leg getLeg(int legNumber) {
        Leg foundLeg = legs.get(0);
        for (Leg leg : legs) {
            if (leg.getLegNumber() == legNumber) {
                foundLeg = leg;
            }
        }
        return foundLeg;
    }

    public String getName() {
        return name;
    }
}

