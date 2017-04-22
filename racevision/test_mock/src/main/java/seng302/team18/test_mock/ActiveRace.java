package seng302.team18.test_mock;

import seng302.team18.model.*;
import seng302.team18.util.GPSCalculations;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Subclass of race that can simulate a race.
 */
public class ActiveRace extends Race {
    GPSCalculations gpsCalculations;
    private List<Boat> startingList;
    private Course course;
    private List<Boat> finishedList;
    private double duration;
    public static final double WARNING_TIME_SECONDS = 60;
    public static final double PREP_TIME_SECONDS = 120;

    /**
     * Race class constructor.
     *
     * @param startingList Arraylist holding all entered boats
     * @param course       Course object
     */
    public ActiveRace(List<Boat> startingList, Course course) {
        gpsCalculations = new GPSCalculations(course);
        startingList.sort(Comparator.comparingDouble(Boat::getSpeed));
        this.startingList = startingList;
        this.course = course;
        finishedList = new ArrayList<>();
        setCourseForBoats();
        duration = 60;
        setInitialSpeed();
        //set boats co-ords to start line
        //setStartingPositions();

    }

    /**
     * Sets the speed of the boats at the start line to 5
     */
    private void setInitialSpeed(){
        for(Boat b: startingList){
            b.setSpeed(0.005);
        }
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
            boat.setHeading(gpsCalculations.retrieveHeading(boat.getCoordinate(), boat.getDestination()));
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


    /**
     * Updates the position and heading of every boat in the race.
     * @param time
     */
    public void updateBoats(double time) { // time in seconds
        for (Boat boat : startingList) {
            if (!finishedList.contains(boat)) {
                updateBoat(boat, time);
            }
        }
    }


    /**
     * Updates a boats position then heading.
     * @param boat
     * @param time
     */
    private void updateBoat(Boat boat, double time) {
        updatePosition(boat, time);
        updateHeading(boat);
    }


    /**
     * Changes the boats heading so that if it has reached its destination
     * it heads in the direction of its next destination. Otherwise set the heading
     * to be in the direction of its current destination.
     * @param boat to be updated
     */
    private void updateHeading(Boat boat) {
        // if boat gets within range of its next destination changes its destination and heading
        if ((Math.abs(boat.getDestination().getLongitude() - boat.getCoordinate().getLongitude()) < 0.001)
                && (Math.abs(boat.getDestination().getLatitude() - boat.getCoordinate().getLatitude()) < 0.001)) {
            Leg nextLeg = course.getNextLeg(boat.getLeg()); // find next leg
            // if current leg is the last leg boat is now finished
            if (nextLeg.equals(boat.getLeg())) {
                finishedList.add(boat);
                boat.setSpeed(0d);
                return;
            }
            if (boat.getLeg().getDestination().getMarks().size() == CompoundMark.GATE_SIZE &&  // if the destination is a gate
                    !boat.getDestination().equals(nextLeg.getDeparture().getMarks().get(0).getCoordinate())) { // and it hasn't gone around the gate
                boat.setDestination(nextLeg.getDeparture().getMarks().get(0).getCoordinate()); // move around the gate
            } else { // the destination was a mark or is already gone around gate so move onto the next leg
                setNextLeg(boat, nextLeg);
            }
        }
        boat.setHeading(gpsCalculations.retrieveHeading(boat.getCoordinate(), boat.getDestination()));
    }


    /**
     * Sets the next Leg of the boat, updates the mark to show the boat has passed it,
     * and sets the destination to the next marks coordinates.
     * @param boat
     * @param nextLeg
     */
    private void setNextLeg(Boat boat, Leg nextLeg) {
        CompoundMark passedMark = boat.getLeg().getDestination();
        passedMark.addPassed(boat);
        boat.setPlace(passedMark.getPassed().indexOf(boat) + 1);
        boat.setDestination(nextLeg.getDestination().getMidCoordinate());
        boat.setLeg(nextLeg);
        //startingList.set(startingList.indexOf(boat), boat); // forces list to notify the tableview
    }


    /**
     * Updates the boats coordinates to move closer to the boats destination.
     * Amount moved is proportional to the time passed
     * @param boat to be moved
     * @param time that has passed
     */
    private void updatePosition(Boat boat, double time) {
        //System.out.println(boat.getCoordinate());
        final double KMPH_TO_MPS = 1000.0 / 3600.0;
        double speed = boat.getSpeed() * KMPH_TO_MPS;
        double distanceTravelled = speed * time;
        boat.setCoordinate( // set next position based on current coordinate, distance travelled, and heading.
                gpsCalculations.coordinateToCoordinate(boat.getCoordinate(), boat.getHeading(), distanceTravelled));
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

    public boolean isFinished() {
        return startingList.size() == finishedList.size();
    }}
