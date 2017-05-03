package seng302.team18.model;

import seng302.team18.util.GPSCalculations;
import seng302.team18.util.PolarCalculator;
import seng302.team18.util.XYPair;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * A class to represent an individual race.
 */
public class Race {

    private List<Boat> startingList;
    private Course course;
    private List<Boat> finishedList;
    private ZonedDateTime startTime;
    private ZonedDateTime currentTime;
    private List<Integer> participantIds;
    private int id;
    private byte status;
    public static int PREP_TIME_SECONDS = 120;

    private PolarCalculator polars;

    public Race() {
        participantIds = new ArrayList<>();
        startingList = new ArrayList<>();
        course = new Course();
        finishedList = new ArrayList<>();
        id = 0;
//        setInitialSpeed();
    }


    /**
     * Race class constructor.
     *
     * @param startingList ArrayList holding all entered boats
     * @param course       Course object
     */
    public Race(List<Boat> startingList, Course course, int raceId, PolarCalculator polars) {
        this.startingList = startingList;
        this.course = course;
        finishedList = new ArrayList<>();
        participantIds = startingList.stream().map(Boat::getId).collect(Collectors.toList());
        this.id = raceId;
        this.polars = polars;
        setCourseForBoats();
        setInitialSpeed();
    }



    /**
     * Sets the speed of the boats at the start line
     */
    private void setInitialSpeed(){
        int speed = 200;
        for(Boat b: startingList){
            b.setSpeed(speed); //kph
            speed -= 25;
        }
    }


    /**
     * Convert a value given in knots to meters per second.
     *
     * @param knots speed in knots.
     * @return speed in meters per second.
     * TODO: Put this somewhere more reasonable
     */
    public double knotsToMetersPerSecond(double knots) {
        return ((knots * 1.852) / 3.6);
    }


    /**
     * Called in Race constructor.
     * Set up the course CompoundMarks for each boat in the race as well as set the
     * current(starting CompoundMark) and next CompoundMark.
     */
    private void setCourseForBoats() {
        if (course.getLegs().size() > 0) {
            for (Boat boat : startingList) {
                // Set Leg
                boat.setLeg(course.getLegs().get(0));
                // Set Dest
                boat.setDestination(boat.getLeg().getDestination().getMidCoordinate());
                // Set Coordinate
                Coordinate midPoint = boat.getLeg().getDeparture().getMidCoordinate();
                boat.setCoordinate(midPoint);
                // Set Heading
                boat.setHeading(boat.getCoordinate().retrieveHeading(boat.getDestination()));
            }
        }
    }

    /**
     * Starting list getter.
     *
     * @return List holding all entered boats.
     */
    public List<Boat> getStartingList() {
        return startingList;
    }

    /**
     * Starting list setter.
     *
     * @param startingList ArrayList holding all entered boats
     */
    public void setStartingList(List<Boat> startingList) {
        if (participantIds.size() == 0) {
            this.startingList = startingList;
        } else {
            this.startingList.clear();
            for (Boat boat : startingList) {
                if (participantIds.contains(boat.getId())) {
                    this.startingList.add(boat);
                }
            }
        }
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
        GPSCalculations gps = new GPSCalculations(course);
        Coordinate oldDestination = boat.getDestination();
        // if boat gets within range of its next destination changes its destination and heading
        if ((Math.abs(oldDestination.getLongitude() - oldDestination.getLongitude()) < 0.0001)
                && (Math.abs(oldDestination.getLatitude() - oldDestination.getLatitude()) < 0.0001)) {


            XYPair speedHeading;
            Leg nextLeg = course.getNextLeg(boat.getLeg()); // find next leg
            Coordinate destination = nextLeg.getDestination().getMarks().get(0).getCoordinate();
            Coordinate departure = nextLeg.getDeparture().getMarks().get(0).getCoordinate();
            if (nextLeg.equals(boat.getLeg())) { // on last leg
                if (oldDestination.equals(destination)) { // if current leg is the last leg boat is now finished
                    finishedList.add(boat);
                    boat.setSpeed(0d);
                    return;
                }
            }
            if (boat.getDestination().equals(departure)) { // starting new leg
                double newHeading = boat.getCoordinate().retrieveHeading(destination);
                speedHeading = polars.getBest(course.getWindSpeed(), newHeading, course.getWindDirection());
                boat.setSpeed(speedHeading.getX());
                boat.setHeading(speedHeading.getY());
                Coordinate boatCoord = boat.getCoordinate();
                double legDistance = gps.gpsDistance(boatCoord, destination);
                Coordinate halfWay = gps.coordinateToCoordinate(boat.getCoordinate(), boat.getHeading(), legDistance);
                boat.setDestination(halfWay);
                setNextLeg(boat, nextLeg);
            } else { // Half way through the leg;
                double newHeading = boat.getCoordinate().retrieveHeading(destination);
                speedHeading = polars.getBest(course.getWindSpeed(), newHeading, course.getWindDirection());
                boat.setSpeed(speedHeading.getX());
                boat.setHeading(speedHeading.getY());
                boat.setDestination(destination);
            }
//            XYPair speedHeading = PolarCalculator.getBest(course.getWindSpeed(), );
//            Coordinate departure = boat.getLeg().getDeparture().getMarks().get(0).getCoordinate();
//            double distance = departure.distance(boat.getDestination()) / Math.cos(speedHeading.getY());
//            Coordinate destination = gps.coordinateToCoordinate(boat.getCoordinate(), speedHeading.getY(), distance);
//            boat.setDestination(destination);
//            if (boat.getLeg().getDestination().getMarks().size() == CompoundMark.GATE_SIZE &&  // if the destination is a gate
//                    !boat.getDestination().equals(nextLeg.getDeparture().getMarks().get(0).getCoordinate())) { // and it hasn't gone around the gate
//                boat.setDestination(nextLeg.getDeparture().getMarks().get(0).getCoordinate()); // move around the gate
//            } else { // the destination was a mark or is already gone around gate so move onto the next leg
//                setNextLeg(boat, nextLeg);
//            }
        }

//        boat.setHeading(gps.retrieveHeading(boat.getCoordinate(), boat.getDestination()));
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
        double speed = boat.getSpeed();
        double mpsSpeed = speed * 0.27778;//convert to metres/second
        double secondsTime = time / 1000;
        double distanceTravelled = mpsSpeed * secondsTime;
        GPSCalculations gps = new GPSCalculations(course);
        // set next position based on current coordinate, distance travelled, and heading.
        boat.setCoordinate(gps.coordinateToCoordinate(boat.getCoordinate(), boat.getHeading(), distanceTravelled));
    }


    public List<Boat> getFinishedList() {
        return finishedList;
    }



    public boolean isFinished() {
        return startingList.size() == finishedList.size();
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public void setParticipantIds(List<Integer> participantIds) {
        this.participantIds = participantIds;
        startingList = startingList.stream()
                .filter(boat -> participantIds.contains(boat.getId()))
                .collect(Collectors.toList());
    }

    public void setCurrentTime(ZonedDateTime currentTime) {
        this.currentTime = currentTime;
    }

    public ZonedDateTime getCurrentTime() {return currentTime; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

}