package seng302.model;

import seng302.util.GPSCalculations;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


/**
 * A class which represents a course that is used in a race event.
 */
public class Course {

    private List<CompoundMark> compoundMarks;
    private List<Leg> legs;
    private double windDirection;
    private List<Coordinate> boundaries;
    private ZoneId timeZone;

    public Course(List<CompoundMark> marks, List<Coordinate> boundaries, double windDirection, ZoneId timeZone) {
        this.compoundMarks = marks;
        this.windDirection = windDirection;
        this.boundaries = boundaries;
        this.timeZone = timeZone;
        legs = new ArrayList<>();
        for (int i = 0; i < marks.size() - 1; i++) {
            legs.add(new Leg(marks.get(i), marks.get(i + 1), i));
        }
    }


    /**
     * A getter for the CompoundMarks in the course
     *
     * @return the Arraylist of CompoundMarks
     */
    public List<CompoundMark> getCompoundMarks() {
        return compoundMarks;
    }


    public List<Leg> getLegs() {
        return legs;
    }


    public Leg getNextLeg(Leg leg) {
        if (leg.getLegNumber() + 1 >= legs.size()) {
            return leg;
        }
        return legs.get(leg.getLegNumber() + 1);
    }

    public double getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
    }


    public double getCourseDistance(){
        double distance = 0;

        for (int i = 0; i < getLegs().size(); i++){
            Coordinate dep = getLegs().get(i).getDeparture().getMidCoordinate();
            Coordinate dest = getLegs().get(i).getDestination().getMidCoordinate();
            distance += GPSCalculations.GPSDistance(dep, dest);
        }
        return distance;
    }

    public List<Coordinate> getBoundaries() {
        return boundaries;
    }

    public ZoneId getTimeZone() {
        return timeZone;
    }
}

