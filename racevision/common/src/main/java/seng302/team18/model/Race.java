package seng302.team18.model;

import seng302.team18.util.GPSCalculations;

import java.time.Instant;
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


    public Race() {
        participantIds = new ArrayList<>();
        startingList = new ArrayList<>();
        course = new Course();
        finishedList = new ArrayList<>();
        id = 0;
        status = 0;
        currentTime = ZonedDateTime.ofInstant(Instant.EPOCH, course.getTimeZone());
        startTime = ZonedDateTime.ofInstant(Instant.EPOCH, course.getTimeZone());
//        setInitialSpeed();
    }


    /**
     * Race class constructor.
     *
     * @param startingList ArrayList holding all entered boats
     * @param course       Course object
     */
    public Race(List<Boat> startingList, Course course, int raceId) {
        this.startingList = startingList;
        this.course = course;
        finishedList = new ArrayList<>();
        participantIds = startingList.stream()
                .map(Boat::getId)
                .collect(Collectors.toList());
        this.id = raceId;
        this.status = 0;
        setCourseForBoats();
        setInitialSpeed();
    }



    /**
     * Sets the speed of the boats at the start line
     */
    private void setInitialSpeed(){
        int speed = 2000;
        for(Boat b: startingList){
            b.setSpeed(speed); //kph
            speed -= 200;
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
                boat.setLegNumber(course.getLegs().get(0).getLegNumber());
                // Set Dest
                boat.setDestination(course.getLeg(boat.getLegNumber()).getDestination().getMidCoordinate());
                // Set Coordinate
                Coordinate midPoint = course.getLeg(boat.getLegNumber()).getDeparture().getMidCoordinate();
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
            this.startingList.clear();
            this.startingList.addAll(startingList);
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
        // if boat gets within range of its next destination changes its destination and heading
        if ((Math.abs(boat.getDestination().getLongitude() - boat.getCoordinate().getLongitude()) < 0.0001)
                && (Math.abs(boat.getDestination().getLatitude() - boat.getCoordinate().getLatitude()) < 0.0001)) {
            Leg nextLeg = course.getNextLeg(course.getLeg(boat.getLegNumber())); // find next leg
            // if current leg is the last leg boat is now finished
            if (nextLeg.equals(course.getLeg(boat.getLegNumber()))) {
                finishedList.add(boat);
                boat.setSpeed(0d);
                boat.setLegNumber(boat.getLegNumber() + 1);
                return;
            }
            if (course.getLeg(boat.getLegNumber()).getDestination().getMarks().size() == CompoundMark.GATE_SIZE &&  // if the destination is a gate
                    !boat.getDestination().equals(nextLeg.getDeparture().getMarks().get(0).getCoordinate())) { // and it hasn't gone around the gate
                boat.setDestination(nextLeg.getDeparture().getMarks().get(0).getCoordinate()); // move around the gate
            } else { // the destination was a mark or is already gone around gate so move onto the next leg
                setNextLeg(boat, nextLeg);
            }
        }
        GPSCalculations gps = new GPSCalculations(course);
        boat.setHeading(gps.retrieveHeading(boat.getCoordinate(), boat.getDestination()));
    }


    /**
     * Sets the next Leg of the boat, updates the mark to show the boat has passed it,
     * and sets the destination to the next marks coordinates.
     * @param boat
     * @param nextLeg
     */

    public void setNextLeg(Boat boat, Leg nextLeg) {
        Leg currentLeg = course.getLeg(boat.getLegNumber());
        currentLeg.addToBoatsCompleted(boat);
        boat.setPlace(currentLeg.getBoatsCompleted().indexOf(boat) + 1);
        boat.setDestination(nextLeg.getDestination().getMarks().get(0).getCoordinate());
        boat.setLegNumber(nextLeg.getLegNumber());

        // TODO when this is enabled it causes the visualiser to freeze, likely due to malformed packets
        markRoundingEvents.add(new MarkRoundingEvent(System.currentTimeMillis(), boat, course.getLeg(boat.getLegNumber()).getDeparture()));
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


//    public void setDuration(double duration) {
//        this.duration = duration;
//    }
//
//    public double getDuration() {
//        return duration;
//    }

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
        List<Boat> newList = startingList.stream()
                .filter(boat -> participantIds.contains(boat.getId()))
                .collect(Collectors.toList());
        startingList.clear();
        startingList.addAll(newList);
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

    private List<MarkRoundingEvent> markRoundingEvents = new ArrayList<>();

    public List<MarkRoundingEvent> popMarkRoundingEvents() {
        List<MarkRoundingEvent> events = markRoundingEvents;
        markRoundingEvents = new ArrayList<>();
        return events;
    }
}