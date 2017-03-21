package seng302;

import java.util.ArrayList;


/**
 * A class to represent an individual race.
 */
public class Race {

    private ArrayList<Boat> startingList = new ArrayList<>();
    private Course course;
    private ArrayList<Boat> finishedList = new ArrayList<>();


    /**
     * Race class constructor.
     *
     * @param startingList Arraylist holding all entered boats
     * @param course       Course object
     */
    public Race(ArrayList<Boat> startingList, Course course) {
        this.startingList = startingList;
        this.course = course;
        setCourseForBoats();
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
            boat.setCurrentLeg(course.getLegs().get(0)); // Set Leg
            boat.setNextDestination(boat.getCurrentLeg().getDestination().getMidCoordinate()); // Set Dest
            // Set Coordinate
            Coordinate start1 = course.getCompoundMarks().get(0).getMarks().get(0).getMarkCoordinates();
            Coordinate start2 = course.getCompoundMarks().get(0).getMarks().get(1).getMarkCoordinates();
            Coordinate midPoint = GPSCalculations.GPSMidpoint(start1, start2);
            boat.setCoordinate(midPoint);
            // Set Heading
            boat.setHeading(GPSCalculations.retrieveHeading(boat.getCoordinate(), boat.getNextDestination()));
        }
    }

    /**
     * Starting list getter.
     *
     * @return Arraylist holding all entered boats
     */
    public ArrayList<Boat> getStartingList() {
        return startingList;
    }

    /**
     * Starting list setter.
     *
     * @param startingList Arraylist holding all entered boats
     */
    public void setStartingList(ArrayList<Boat> startingList) {
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
        if ((Math.abs(boat.getNextDestination().getLongitude() - boat.getCoordinate().getLongitude())
                < 0.0001)
                && (Math.abs(boat.getNextDestination().getLatitude() - boat.getCoordinate().getLatitude())
                < 0.0001)) {
            Leg nextLeg = course.getNextLeg(boat.getCurrentLeg());
            if (nextLeg.equals(boat.getCurrentLeg())) {
                finishedList.add(boat);
            }
            if (boat.getCurrentLeg().getDestination().getMarks().size() == CompoundMark.GATE_SIZE) {
                if (!boat.getNextDestination().equals(nextLeg.getDeparture().getMarks().get(0).getMarkCoordinates())) {
                    boat.setNextDestination(nextLeg.getDeparture().getMarks().get(0).getMarkCoordinates());
                } else {
                    boat.setNextDestination(nextLeg.getDestination().getMidCoordinate());
                    boat.setCurrentLeg(nextLeg);
                }
            } else {
                boat.setNextDestination(nextLeg.getDestination().getMidCoordinate());
                boat.setCurrentLeg(nextLeg);
            }

        }
        boat.setHeading(GPSCalculations.retrieveHeading(boat.getCoordinate(), boat.getNextDestination())); //TODO set at start
        double speed = boat.getSpeed() * 1000 / 3600; // meters per second
        double distanceTravelled = speed * time; // meters
        Coordinate nextCoordinate = GPSCalculations.coordinateToCoordinate(boat.getCoordinate(), boat.getHeading(), distanceTravelled);
        boat.setCoordinate(nextCoordinate);

    }

    public ArrayList<Boat> getFinishedList() {
        return finishedList;
    }
}