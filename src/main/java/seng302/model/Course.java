package seng302.model;

import seng302.raceutil.GPSCalculations;

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

    /**
     * A constructor for the Course class
     * @param marks The list of marks that the course consists of
     * @param boundaries The boundries of the course
     * @param windDirection The direction of the wind in the course
     * @param timeZone The timezone of the course
     */
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


    /**
     * Returns the next leg of the race for a boats
     * @param leg The current leg
     * @return The next leg of the race
     */
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


    /**
     * Returns the total distance of the course
     * @return The distance of the entire course
     */
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

