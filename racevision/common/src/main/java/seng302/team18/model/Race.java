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

    private List<Yacht> startingList;
    private Course course;
    private List<Yacht> finishedList;
    private ZonedDateTime startTime;
    private ZonedDateTime currentTime;
    private List<Integer> participantIds;
    private int id;
    private byte status;
    private String raceName;


    public Race() {
        participantIds = new ArrayList<>();
        startingList = new ArrayList<>();
        course = new Course();
        finishedList = new ArrayList<>();
        id = 0;
        status = 0;
        currentTime = ZonedDateTime.ofInstant(Instant.EPOCH, course.getTimeZone());
        startTime = ZonedDateTime.ofInstant(Instant.EPOCH, course.getTimeZone());
        raceName = "";
    }


    /**
     * Race class constructor.
     *
     * @param startingList ArrayList holding all entered boats
     * @param course       Course object
     */
    public Race(List<Yacht> startingList, Course course, int raceId) {
        this.startingList = startingList;
        this.course = course;
        finishedList = new ArrayList<>();
        participantIds = startingList.stream()
                .map(Yacht::getId)
                .collect(Collectors.toList());
        this.id = raceId;
        this.status = 0;
        setCourseForBoats();
        setInitialSpeed();
    }


    /**
     * Sets the speed of the boats at the start line
     */
    private void setInitialSpeed() {
        int speed = 200;
        for (Yacht b : startingList) {
            b.setSpeed(speed); //kph
            speed -= 50;
        }
    }


    /**
     * Convert a value given in knots to meters per second.
     *
     * @param knots speed in knots.
     * @return speed in meters per second.
     */
    // TODO: Put this somewhere more reasonable
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
            GPSCalculations gps = new GPSCalculations();
            for (Yacht yacht : startingList) {
                // Set Leg
                yacht.setLegNumber(course.getLegs().get(0).getLegNumber());
                // Set Dest
                yacht.setDestination(course.getLeg(yacht.getLegNumber()).getDestination().getCoordinate());
                // Set Coordinate
                Coordinate midPoint = course.getLeg(yacht.getLegNumber()).getDeparture().getCoordinate();
                yacht.setCoordinate(midPoint);
                // Set Heading
                yacht.setHeading(gps.getBearing(yacht.getCoordinate(), (yacht.getDestination())));
            }
        }
    }

    /**
     * Starting list getter.
     *
     * @return List holding all entered boats.
     */
    public List<Yacht> getStartingList() {
        return startingList;
    }

    /**
     * Starting list setter.
     *
     * @param startingList ArrayList holding all entered boats
     */
    public void setStartingList(List<Yacht> startingList) {
        if (participantIds.size() == 0) {
            this.startingList.clear();
            this.startingList.addAll(startingList);
        } else {
            this.startingList.clear();
            for (Yacht yacht : startingList) {
                if (participantIds.contains(yacht.getId())) {
                    this.startingList.add(yacht);
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
     *
     * @param time
     */
    public void updateBoats(double time) { // time in seconds
        for (Yacht yacht : startingList) {
            if (!finishedList.contains(yacht)) {
                updateBoat(yacht, time);
            }
        }
    }


    /**
     * Updates a boats position then heading.
     *
     * @param yacht
     * @param time
     */
    private void updateBoat(Yacht yacht, double time) {
        updatePosition(yacht, time);
        updateHeading(yacht);
    }


    /**
     * Changes the boats heading so that if it has reached its destination
     * it heads in the direction of its next destination. Otherwise set the heading
     * to be in the direction of its current destination.
     *
     * @param yacht to be updated
     */
    private void updateHeading(Yacht yacht) {
        // if yacht gets within range of its next destination changes its destination and heading
        if ((Math.abs(yacht.getDestination().getLongitude() - yacht.getCoordinate().getLongitude()) < 0.0001)
                && (Math.abs(yacht.getDestination().getLatitude() - yacht.getCoordinate().getLatitude()) < 0.0001)) {
            Leg nextLeg = course.getNextLeg(course.getLeg(yacht.getLegNumber())); // find next leg
            // if current leg is the last leg yacht is now finished
            if (nextLeg.equals(course.getLeg(yacht.getLegNumber()))) {
                finishedList.add(yacht);
                yacht.setSpeed(0d);
                yacht.setLegNumber(yacht.getLegNumber() + 1);
                return;
            }
            if (course.getLeg(yacht.getLegNumber()).getDestination().getMarks().size() == CompoundMark.GATE_SIZE &&  // if the destination is a gate
                    !yacht.getDestination().equals(nextLeg.getDeparture().getMarks().get(0).getCoordinate())) { // and it hasn't gone around the gate
                yacht.setDestination(nextLeg.getDeparture().getMarks().get(0).getCoordinate()); // move around the gate
            } else { // the destination was a mark or is already gone around gate so move onto the next leg
                setNextLeg(yacht, nextLeg);
            }
        }
        GPSCalculations gps = new GPSCalculations();
        yacht.setHeading(gps.getBearing(yacht.getCoordinate(), yacht.getDestination()));
    }


    /**
     * Sets the next Leg of the yacht, updates the mark to show the yacht has passed it,
     * and sets the destination to the next marks coordinates.
     *
     * @param yacht
     * @param nextLeg
     */

    public void setNextLeg(Yacht yacht, Leg nextLeg) {
        Leg currentLeg = course.getLeg(yacht.getLegNumber());
        currentLeg.addToBoatsCompleted(yacht);
        int newPlace = currentLeg.getBoatsCompleted().indexOf(yacht) + 1;
        yacht.setPlace(newPlace);
        yacht.setDestination(nextLeg.getDestination().getMarks().get(0).getCoordinate());
        yacht.setLegNumber(nextLeg.getLegNumber());

        for (Yacht currentYacht : getStartingList()) {
            int currentBoatPlace = currentYacht.placeProperty().intValue();
            int boatPlace = yacht.placeProperty().intValue();
            int currentBoatLeg = currentYacht.getLegNumber();
            int boatLeg = yacht.getLegNumber();
            if (!(currentYacht.getId().equals(yacht.getId())) && (currentBoatPlace == boatPlace)) {
                if (currentBoatLeg >= boatLeg) {
                    currentYacht.setPlace(currentBoatPlace - 1);
                } else {
                    currentYacht.setPlace(currentBoatPlace + 1);
                }
            }
        }

        // TODO when this is enabled it causes the visualiser to freeze, likely due to malformed packets
        markRoundingEvents.add(new MarkRoundingEvent(System.currentTimeMillis(), yacht, course.getLeg(yacht.getLegNumber()).getDeparture()));
        //startingList.set(startingList.indexOf(yacht), yacht); // forces list to notify the tableview
    }


    /**
     * Updates the boats coordinates to move closer to the boats destination.
     * Amount moved is proportional to the time passed
     *
     * @param yacht to be moved
     * @param time that has passed
     */
    private void updatePosition(Yacht yacht, double time) {
        double speed = yacht.getSpeed();
        double mpsSpeed = speed * 0.27778;//convert to metres/second
        double secondsTime = time / 1000;
        double distanceTravelled = mpsSpeed * secondsTime;
        GPSCalculations gps = new GPSCalculations();
        // set next position based on current coordinate, distance travelled, and heading.
        yacht.setCoordinate(gps.toCoordinate(yacht.getCoordinate(), yacht.getHeading(), distanceTravelled));
    }


    public List<Yacht> getFinishedList() {
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
        List<Yacht> newList = startingList.stream()
                .filter(boat -> participantIds.contains(boat.getId()))
                .collect(Collectors.toList());
        startingList.clear();
        startingList.addAll(newList);
    }

    public void setCurrentTime(ZonedDateTime currentTime) {
        this.currentTime = currentTime;
    }

    public ZonedDateTime getCurrentTime() {
        return currentTime;
    }

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

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }
}