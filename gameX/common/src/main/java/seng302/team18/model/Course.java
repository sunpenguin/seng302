package seng302.team18.model;

import seng302.team18.util.GPSCalculator;

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
    private List<PickUp> pickUps = new ArrayList<>();

    private double windDirection = 0d;
    private double windSpeed = 0d;

    private Coordinate centralCoordinate = new Coordinate(0d, 0d);
    private ZoneId timeZone = ZoneId.systemDefault();


    public Course(Collection<CompoundMark> marks, List<Coordinate> boundaries, double windDirection, double windSpeed,
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

    public Course(Collection<CompoundMark> marks, List<Coordinate> boundaries, List<MarkRounding> markSequence) {
        this.compoundMarks.addAll(marks);
        this.courseLimits.addAll(boundaries);
        this.markSequence.addAll(markSequence);
        centralCoordinate = new Coordinate(0d, 0d);
        this.timeZone = ZoneId.systemDefault();

        initializeCourse();
    }


    public Course() {
    }


    public synchronized List<CompoundMark> getCompoundMarks() {
        return new ArrayList<>(compoundMarks);
    }


    public void setCompoundMarks(Collection<CompoundMark> compoundMarks) {
        this.compoundMarks.clear();
        this.compoundMarks.addAll(compoundMarks);
    }


    public synchronized List<MarkRounding> getMarkSequence() {
        return markSequence;
    }


    public synchronized void setMarkSequence(List<MarkRounding> markSequence) {
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


    public synchronized List<Coordinate> getLimits() {
        return new ArrayList<>(courseLimits);
    }


    public void setCourseLimits(List<Coordinate> boundaries) {
        this.courseLimits.clear();
        this.courseLimits.addAll(boundaries);
    }


    public ZoneId getTimeZone() {
        return timeZone;
    }


    public void setTimeZone(ZoneId timeZone) {
        this.timeZone = timeZone;
    }


    public Coordinate getCenter() {
        GPSCalculator calculator = new GPSCalculator();
        List<Coordinate> coordinates = calculator.findMinMaxPoints(this);
        return calculator.getCentralCoordinate(coordinates);
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


        for (int i = 1; i < nullBorderedRoundings.size() - 1; i++) {
            previous = current;
            current = future;
            future = nullBorderedRoundings.get(i + 1);

            if (current.getCompoundMark().getMarks().size() == CompoundMark.MARK_SIZE) {
                setMarkRoundingAngle(previous, current, future);
            } else if (current.getCompoundMark().getMarks().size() == CompoundMark.GATE_SIZE) {
                setGateType(previous, current, future);
            }
        }
    }


    private void setMarkRoundingAngle(MarkRounding previous, MarkRounding current, MarkRounding future) {
        GPSCalculator calculator = new GPSCalculator();

        double previousAngle = calculator.getBearing(previous.getCompoundMark().getCoordinate(), current.getCompoundMark().getCoordinate());
        double futureAngle = calculator.getBearing(future.getCompoundMark().getCoordinate(), current.getCompoundMark().getCoordinate());

        current.setPassAngle((((previousAngle + futureAngle) % 360) / 2d));
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
        MarkRounding.GateType gateType;
        if (previousRounding == null) {
            gateType = MarkRounding.GateType.THROUGH_GATE;

        } else {

            GPSCalculator calculator = new GPSCalculator();

            Mark mark1 = rounding.getCompoundMark().getMarks().get(0);
            Mark mark2 = rounding.getCompoundMark().getMarks().get(1);
            double bearingMark1To2 = calculator.getBearing(mark1.getCoordinate(), mark2.getCoordinate());
            double bearingMark2To1 = (bearingMark1To2 + 180) % 360;

            boolean isSP = rounding.getRoundingDirection() == MarkRounding.Direction.SP;

            Double bearingToPrevious = calculator.getBearing(rounding.getCompoundMark().getCoordinate(), previousRounding.getCompoundMark().getCoordinate());
            boolean isPreviousOnEntrance = calculator.isBetween(bearingToPrevious, bearingMark2To1, bearingMark1To2) == isSP;

            if (nextRounding == null) {
                gateType = (isPreviousOnEntrance) ? MarkRounding.GateType.THROUGH_GATE : MarkRounding.GateType.ROUND_THEN_THROUGH;

            } else {
                Double bearingToFuture = calculator.getBearing(rounding.getCompoundMark().getCoordinate(), nextRounding.getCompoundMark().getCoordinate());
                boolean isNextOnExitSide = calculator.isBetween(bearingToFuture, bearingMark1To2, bearingMark2To1) == isSP;

                if (isNextOnExitSide) {
                    gateType = (isPreviousOnEntrance) ? MarkRounding.GateType.THROUGH_GATE : MarkRounding.GateType.ROUND_THEN_THROUGH;
                } else {
                    gateType = (isPreviousOnEntrance) ? MarkRounding.GateType.THROUGH_THEN_ROUND : MarkRounding.GateType.ROUND_BOTH_MARKS;
                }
            }
        }

        rounding.setGateType(gateType);
    }


    public String getName() {
        return name;
    }


    public void addPickUp(PickUp pickUp) {
        pickUps.add(pickUp);
    }


    /**
     * Removes a single PickUp given an id.
     *
     * @param id of the PickUp.
     */
    public void removePickUp(int id) {
        pickUps.removeIf(pickUp -> pickUp.getId() == id);
    }


    /**
     * Removes PickUps that have expired.
     */
    public void removeOldPickUps() {
        List<PickUp> remaining = new ArrayList<>();
        for (PickUp pickUp: pickUps) {
            if (!pickUp.hasExpired()) {
                remaining.add(pickUp);
            }
        }
        setPickUps(remaining);
    }


    public List<PickUp> getPickUps() {
        return new ArrayList<>(pickUps);
    }


    /**
     * Returns pick up with specified id.
     * null if not exists
     *
     * @param id of the pick up
     * @return the pick up with the given id.
     */
    public PickUp getPickUp(int id) {
        for (PickUp pickUp : pickUps) {
            if (pickUp.getId() == id) {
                return pickUp;
            }
        }
        return null;
    }


    public void setPickUps(List<PickUp> pickUps) {
        this.pickUps = pickUps;
    }


    public synchronized MarkRounding getMarkRounding(int sequenceNumber) {
        return markSequence.get(sequenceNumber);
    }


    public synchronized Coordinate getDestination(int legNumber) {
        return markSequence.get(legNumber).getCoordinate();
    }


    public synchronized int getStartLineId() {
        try {
            return markSequence.get(0).getMarkId();
        } catch (Exception e) {
            for (MarkRounding mark : markSequence) {
                System.out.println(mark);
            }
            throw e;
        }
    }


    public synchronized int getFinishLineId() {
        return markSequence.get(markSequence.size() - 1).getMarkId();
    }
}

