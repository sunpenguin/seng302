package seng302.team18.model;

import seng302.team18.util.GPSCalculations;
import seng302.team18.util.SpeedConverter;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * A class to represent an individual race.
 */
public class Race {

    private Regatta regatta = new Regatta();
    private List<Boat> startingList;
    private Course course;
    private List<Boat> finishedList;
    private ZonedDateTime startTime = ZonedDateTime.now();
    private ZonedDateTime currentTime;
    private List<Integer> participantIds;
    private int id;
    private RaceStatus status;
    private Integer playerId;
    private RaceType raceType;


    public Race() {
        participantIds = new ArrayList<>();
        startingList = new ArrayList<>();
        course = new Course();
        finishedList = new ArrayList<>();
        id = 0;
        status = RaceStatus.NOT_ACTIVE;
        currentTime = ZonedDateTime.ofInstant(Instant.EPOCH, course.getTimeZone()); //.now(course.getTimeZone())
        startTime = ZonedDateTime.ofInstant(Instant.EPOCH, course.getTimeZone()); //.now(course.getTimeZone()).plusSeconds(300)
        raceType = RaceType.MATCH;
    }


    /**
     * Race class constructor.
     *
     * @param startingList ArrayList holding all entered boats
     * @param course       Course object
     */
    public Race(List<Boat> startingList, Course course, int raceId, RaceType raceType) {
        this.startingList = startingList;
        this.course = course;
        finishedList = new ArrayList<>();
        participantIds = startingList.stream()
                .map(Boat::getId)
                .collect(Collectors.toList());
        this.id = raceId;
        this.status = RaceStatus.NOT_ACTIVE;
        this.raceType = raceType;
        setCourseForBoats();
        setInitialSpeed();
    }


    /**
     * Sets the speed of the boats at the start line
     */
    private void setInitialSpeed() {
        double speed = 100;
        for (Boat boat : startingList) {
            boat.setSpeed(speed); // knots
            speed -= 10;
        }
    }


    /**
     * Called in Race constructor.
     * Set up the course CompoundMarks for each boat in the race as well as set the
     * current(starting CompoundMark) and next CompoundMark.
     */
    private void setCourseForBoats() {
        if (course.getLegs().size() > 0) {
            for (Boat boat : startingList) {
                setCourseForBoat(boat);
            }
        }
    }

    private void setCourseForBoat(Boat boat) {
        // Set Leg
        boat.setLegNumber(course.getLegs().get(0).getLegNumber());
        // Set Dest
        boat.setDestination(course.getLeg(boat.getLegNumber()).getDestination().getCoordinate());
        // Set Coordinate
        Coordinate midPoint = course.getLeg(boat.getLegNumber()).getDeparture().getCoordinate();
        boat.setCoordinate(midPoint);
        // Set Heading
        GPSCalculations gps = new GPSCalculations();
        boat.setHeading(gps.getBearing(boat.getCoordinate(), (boat.getDestination())));
        double tws = boat.getBoatTWS(course.getWindSpeed(), course.getWindDirection());
        boat.setSpeed(tws);
    }

    public void addParticipant(Boat boat) {
        // check that it is alright to add a boat at this point
        setCourseForBoat(boat);
        startingList.add(boat);
        participantIds.add(boat.getId());
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
        this.startingList.clear();
        if (participantIds.size() == 0) {
            this.startingList.addAll(startingList);
        } else {
            for (Boat boat : startingList) {
                if (participantIds.contains(boat.getId())) {
                    this.startingList.add(boat);
                }
            }
        }
        for (Boat boat : this.startingList) {
            boat.setControlled(playerId != null && playerId.equals(boat.getId()));
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
     * @param time the time in seconds
     */
    // TODO afj19, 20th July: check the temporal unit here
    public void updateBoats(double time) { // time in seconds
        for (Boat boat : startingList) {
            if (!finishedList.contains(boat)) {
//                updateBoat(boat, time);
                updatePosition(boat, time);
            }
        }
    }


    /**
     * Updates a boats position then heading.
     *
     * @param boat the boat
     * @param time the time (units?)
     */
    // TODO afj19, 20th July: check the temporal unit here
    private void updateBoat(Boat boat, double time) {
        updatePosition(boat, time);
        updateHeading(boat);
    }


    /**
     * Changes the boats heading so that if it has reached its destination
     * it heads in the direction of its next destination. Otherwise set the heading
     * to be in the direction of its current destination.
     *
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
        GPSCalculations gps = new GPSCalculations();
        boat.setHeading(gps.getBearing(boat.getCoordinate(), boat.getDestination()));
        double tws = boat.getBoatTWS(course.getWindSpeed(), course.getWindDirection());
        boat.setSpeed(tws);
    }


    /**
     * Sets the next Leg of the boat, updates the mark to show the boat has passed it,
     * and sets the destination to the next marks coordinates.
     *
     * @param boat the boat
     * @param nextLeg the next leg
     */

    public void setNextLeg(Boat boat, Leg nextLeg) {
        Leg currentLeg = course.getLeg(boat.getLegNumber());
        currentLeg.addToBoatsCompleted(boat);
        int newPlace = currentLeg.getBoatsCompleted().indexOf(boat) + 1;
        boat.setPlace(newPlace);
        boat.setDestination(nextLeg.getDestination().getMarks().get(0).getCoordinate());
        boat.setLegNumber(nextLeg.getLegNumber());

        for (Boat currentBoat : getStartingList()) {
            int currentBoatPlace = currentBoat.placeProperty().intValue();
            int boatPlace = boat.placeProperty().intValue();
            int currentBoatLeg = currentBoat.getLegNumber();
            int boatLeg = boat.getLegNumber();
            if (!(currentBoat.getId().equals(boat.getId())) && (currentBoatPlace == boatPlace)) {
                if (currentBoatLeg >= boatLeg) {
                    currentBoat.setPlace(currentBoatPlace - 1);
                } else {
                    currentBoat.setPlace(currentBoatPlace + 1);
                }
            }
        }

        // TODO when this is enabled it causes the visualiser to freeze, likely due to malformed packets
        markRoundingEvents.add(new MarkRoundingEvent(System.currentTimeMillis(), boat, course.getLeg(boat.getLegNumber()).getDeparture()));
        //startingList.set(startingList.indexOf(boat), boat); // forces list to notify the tableview
    }


    /**
     * Updates the boats coordinates to move closer to the boats destination.
     * Amount moved is proportional to the time passed
     *
     * @param boat to be moved
     * @param time that has passed
     */
    private void updatePosition(Boat boat, double time) {
        double speed = boat.getSpeed(); // knots
        if (boat.isSailOut()) {
            speed = 0;
        }
        double mpsSpeed = new SpeedConverter().knotsToMs(speed); // convert to meters/second
        double secondsTime = time / 1000.0d;
        double distanceTravelled = mpsSpeed * secondsTime;
        GPSCalculations gps = new GPSCalculations();
        // set next position based on current coordinate, distance travelled, and heading.
        boat.setCoordinate(gps.toCoordinate(boat.getCoordinate(), boat.getHeading(), distanceTravelled));
    }


    public List<Boat> getFinishedList() {
        return finishedList;
    }


    public boolean isFinished() {
        return startingList.size() == finishedList.size() && startingList.size() != 0;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Sets participants and removes non participants for current list of boats.
     *
     * @param participantIds ids of all participants
     */
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

    public ZonedDateTime getCurrentTime() {
        return currentTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RaceStatus getStatus() {
        return status;
    }

    public void setStatus(RaceStatus status) {
        this.status = status;
    }

    private List<MarkRoundingEvent> markRoundingEvents = new ArrayList<>();

    public List<MarkRoundingEvent> popMarkRoundingEvents() {
        List<MarkRoundingEvent> events = markRoundingEvents;
        markRoundingEvents = new ArrayList<>();
        return events;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public RaceType getRaceType() {
        return raceType;
    }

    public Regatta getRegatta() {
        return regatta;
    }

    public void setRegatta(Regatta regatta) {
        this.regatta = regatta;
    }

    public void setRaceType(RaceType raceType) {
        this.raceType = raceType;
    }

    public enum RaceType {
        MATCH("Match"),
        FLEET("Fleet");

        private final String value;

        private final static Map<String, RaceType> MAPPING = initializeMapping();

        RaceType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static RaceType fromValue(String value) {
            return MAPPING.get(value);
        }

        private static Map<String, RaceType> initializeMapping() {
            return Arrays.stream(values()).collect(Collectors.toMap(RaceType::toString, rt -> rt));
        }
    }
}