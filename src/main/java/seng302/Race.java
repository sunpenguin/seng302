package seng302;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A class to represent an individual race.
 */
public class Race {

    private List<Boat> startingList;
    private Course course;
    private List<Boat> finishedList;
    private double duration;

    /**
     * Race class constructor.
     *
     * @param startingList Arraylist holding all entered boats
     * @param course       Course object
     */
    public Race(List<Boat> startingList, Course course) {
        startingList.sort(Comparator.comparingDouble(Boat::getSpeed));
        this.startingList = startingList;
        this.course = course;
        finishedList = new ArrayList<>();
        setCourseForBoats();
        duration = 30;
    }

    /**
     * Convert a value given in knots to meters per second.
     *
     * @param knots speed in knots.
     * @return speed in meters per second.
     */
    public static double knotsToMetersPerSecond(double knots) {
        return ((knots * 1.852) / 3.6);
    }

    /**
     * Called in Race constructor.
     * Set up the course CompoundMarks for each boat in the race as well as set the
     * current(starting CompoundMark) and next CompoundMark.
     */
    private void setCourseForBoats() {
        for (Boat boat : startingList) {
            // Set Leg
            boat.setLeg(course.getLegs().get(0));
            // Set Dest
            boat.setDestination(boat.getLeg().getDestination().getMidCoordinate());
            // Set Coordinate
            Coordinate midPoint = course.getCompoundMarks().get(0).getMidCoordinate();
            boat.setCoordinate(midPoint);
            // Set Heading
            boat.setHeading(GPSCalculations.retrieveHeading(boat.getCoordinate(), boat.getDestination()));
        }
    }

    /**
     * Starting list getter.
     *
     * @return ObservableList holding all entered boats
     */
    public List<Boat> getStartingList() {
        return startingList;
    }

    /**
     * Starting list setter.
     *
     * @param startingList Arraylist holding all entered boats
     */
    public void setStartingList(List<Boat> startingList) {
        this.startingList = startingList;
    }

    /**
     * Course getter.
     *
     * @return Course object
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Course setter.
     *
     * @param course Course object
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    public void updateBoats(double time) { // time in seconds
        for (Boat boat : startingList) {
            if (!finishedList.contains(boat)) {
                updateBoat(boat, time);
            }
        }
    }

    private void updateBoat(Boat boat, double time) {
        updatePosition(boat, time);
        updateHeading(boat);
    }

    private void updateHeading(Boat boat) {
        // if boat gets within range of its next destination changes its destination and heading
        if ((Math.abs(boat.getDestination().getLongitude() - boat.getCoordinate().getLongitude()) < 0.0001)
                && (Math.abs(boat.getDestination().getLatitude() - boat.getCoordinate().getLatitude()) < 0.0001)) {
            Leg nextLeg = course.getNextLeg(boat.getLeg()); // find next leg
            // if current leg is the last leg boat is now finished
            if (nextLeg.equals(boat.getLeg())) {
                finishedList.add(boat);
                boat.setSpeed(0d);
                return;
            }
            if (boat.getLeg().getDestination().getMarks().size() == CompoundMark.GATE_SIZE &&  // if the destination is a gate
                    !boat.getDestination().equals(nextLeg.getDeparture().getMarks().get(0).getCoordinates())) { // and it hasn't gone around the gate
                boat.setDestination(nextLeg.getDeparture().getMarks().get(0).getCoordinates()); // move around the gate
            } else { // the destination was a mark or is already gone around gate so move onto the next leg
                setNextLeg(boat, nextLeg);
            }
        }
        boat.setHeading(GPSCalculations.retrieveHeading(boat.getCoordinate(), boat.getDestination()));
    }


    private void setNextLeg(Boat boat, Leg nextLeg) {
        CompoundMark passedMark = boat.getLeg().getDestination();
        passedMark.addPassed(boat);
        boat.setPlace(passedMark.getPassed().indexOf(boat) + 1);
        boat.setDestination(nextLeg.getDestination().getMidCoordinate());
        boat.setLeg(nextLeg);
        //startingList.set(startingList.indexOf(boat), boat); // forces list to notify the tableview
    }


    private void updatePosition(Boat boat, double time) {
        final double KILOMETERS_PER_HOUR_TO_METERS_PER_SECOND_CONVERSION_CONSTANT = 1000.0 / 3600.0;
        double speed = boat.getSpeed() * KILOMETERS_PER_HOUR_TO_METERS_PER_SECOND_CONVERSION_CONSTANT;
        double distanceTravelled = speed * time
                / (duration / (course.getCourseDistance()
                / (startingList.get(0).getSpeed() * KILOMETERS_PER_HOUR_TO_METERS_PER_SECOND_CONVERSION_CONSTANT))); // meters
        boat.setCoordinate( // set next position based on current coordinate, distance travelled, and heading.
                GPSCalculations.coordinateToCoordinate(boat.getCoordinate(), boat.getHeading(), distanceTravelled));
    }


    public List<Boat> getFinishedList() {
        return finishedList;
    }


    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getDuration() {
        return duration;
    }

}