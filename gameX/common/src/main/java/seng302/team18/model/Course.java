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

    private String name = "";

    private List<CompoundMark> compoundMarks = new ArrayList<>();
    private List<MarkRounding> markSequence = new ArrayList<>();
    private List<Coordinate> courseLimits = new ArrayList<>();

    private double windDirection = 0d;
    private double windSpeed = 0d;

    private Coordinate centralCoordinate = new Coordinate(0d, 0d);
    private ZoneId timeZone = ZoneId.systemDefault();


    public Course(Collection<CompoundMark> marks, Collection<Coordinate> boundaries, double windDirection, double windSpeed,
                  ZoneId timeZone, List<MarkRounding> markSequence) {
        this.compoundMarks.addAll(marks);
        this.courseLimits.addAll(boundaries);
        this.markSequence.addAll(markSequence);
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        centralCoordinate = new Coordinate(0d, 0d);
        this.timeZone = timeZone;

        initializeCourse();
    }


    public Course() {
    }


    public List<CompoundMark> getCompoundMarks() {
        return compoundMarks;
    }


    public void setCompoundMarks(Collection<CompoundMark> compoundMarks) {
        this.compoundMarks.clear();
        this.compoundMarks.addAll(compoundMarks);
    }


    public List<MarkRounding> getMarkSequence() {
        return markSequence;
    }


    public void setMarkSequence(Collection<MarkRounding> markSequence) {
        this.markSequence.clear();
        this.markSequence.addAll(markSequence);
        initializeCourse();
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


    public List<Coordinate> getCourseLimits() {
        return new ArrayList<>(courseLimits);
    }


    public void setCourseLimits(Collection<Coordinate> boundaries) {
        this.courseLimits.clear();
        this.courseLimits.addAll(boundaries);
    }


    public ZoneId getTimeZone() {
        return timeZone;
    }


    public void setTimeZone(ZoneId timeZone) {
        this.timeZone = timeZone;
    }


    public Coordinate getCentralCoordinate() {
        return centralCoordinate;
    }


    public void setCentralCoordinate(Coordinate centralCoordinate) {
        if (this.centralCoordinate.getLatitude() == 0d && this.centralCoordinate.getLongitude() == 0d) {
            this.centralCoordinate = centralCoordinate;
        }
    }


    /**
     * Initializes the legs of the course to be in the order of the compound marks as they appear in the list.
     * Then adds the pass angle to each compound mark (if you do not know what pass angle is talk to Alice).
     */
    private void initializeCourse() {
        if (markSequence.size() < 2) return;

        List<MarkRounding> nullBorderedRoundings = new ArrayList<>();
        nullBorderedRoundings.add(null);
        nullBorderedRoundings.addAll(markSequence);
        nullBorderedRoundings.add(null);

        MarkRounding previous;
        MarkRounding current = null;
        MarkRounding future = nullBorderedRoundings.get(1);

        GPSCalculations calculator = new GPSCalculations();

        for (int i = 1; i < nullBorderedRoundings.size() - 1; i++) {
            previous = current;
            current = future;
            future = nullBorderedRoundings.get(i + 1);

            if (current.getCompoundMark().getMarks().size() == CompoundMark.MARK_SIZE) {
                double previousAngle = calculator.getBearing(previous.getCompoundMark().getCoordinate(), current.getCompoundMark().getCoordinate());
                double futureAngle = calculator.getBearing(future.getCompoundMark().getCoordinate(), current.getCompoundMark().getCoordinate());
                current.setPassAngle(((previousAngle + futureAngle) / 2d));
            } else if (current.getCompoundMark().getMarks().size() == CompoundMark.GATE_SIZE) {
                setGateType(previous, current, future);
            }
        }
    }


    /**
     * Sets the gate type for a mark rounding that is a gate.
     * Will be set to a value in the GateType enum
     *
     * @param previousRounding mark before the gate
     * @param rounding         gate
     * @param nextRounding     mark after the gate
     */
    private void setGateType(MarkRounding previousRounding, MarkRounding rounding, MarkRounding nextRounding) {
        if (previousRounding == null) {
            rounding.setGateType(MarkRounding.GateType.THROUGH_GATE);
            return;
        }

        GPSCalculations calculator = new GPSCalculations();

        Mark mark1 = rounding.getCompoundMark().getMarks().get(0);
        Mark mark2 = rounding.getCompoundMark().getMarks().get(1);
        double bearingBetweenMarks = calculator.getBearing(mark1.getCoordinate(), mark2.getCoordinate());
        double oppositeBearing = (bearingBetweenMarks + 180) % 360;

        boolean isSP = rounding.getRoundingDirection() == MarkRounding.Direction.SP;

        Double bearingToPrevious = calculator.getBearing(rounding.getCompoundMark().getCoordinate(), previousRounding.getCompoundMark().getCoordinate());
        boolean isPreviousOnLeft = calculator.isBetween(bearingToPrevious, oppositeBearing, bearingBetweenMarks);
        boolean previousOnExitSide = isSP == isPreviousOnLeft;

        if (nextRounding == null) {
            if (previousOnExitSide) {
                rounding.setGateType(MarkRounding.GateType.ROUND_THEN_THROUGH);
            } else {
                rounding.setGateType(MarkRounding.GateType.THROUGH_GATE);
            }
            return;
        }

        Double bearingToFuture = calculator.getBearing(rounding.getCompoundMark().getCoordinate(), nextRounding.getCompoundMark().getCoordinate());
        boolean isFutureOnLeft = calculator.isBetween(bearingToFuture, oppositeBearing, bearingBetweenMarks);
        boolean futureOnExitSide = isSP == isFutureOnLeft;


        if (futureOnExitSide) {
            rounding.setGateType((previousOnExitSide) ?
                    MarkRounding.GateType.THROUGH_THEN_ROUND : MarkRounding.GateType.THROUGH_GATE);
        } else {
            rounding.setGateType((previousOnExitSide) ?
                    MarkRounding.GateType.ROUND_BOTH_MAKRS : MarkRounding.GateType.ROUND_THEN_THROUGH);
        }
    }


    public String getName() {
        return name;
    }
}

