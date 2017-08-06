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
    private RaceMode mode = RaceMode.RACE;
    GPSCalculations gps = new GPSCalculations();

    public Race() {
        participantIds = new ArrayList<>();
        startingList = new ArrayList<>();
        course = new Course();
        finishedList = new ArrayList<>();
        id = 0;
        status = RaceStatus.NOT_ACTIVE;
        currentTime = ZonedDateTime.ofInstant(Instant.EPOCH, course.getTimeZone()).now(course.getTimeZone());
        startTime = ZonedDateTime.ofInstant(Instant.EPOCH, course.getTimeZone());
        raceType = RaceType.MATCH;
    }


    /**
     * Race class constructor.
     *
     * @param startingList ArrayList holding all entered boats
     * @param course       Course object
     * @param raceId Integer representing the race id
     * @param raceType RaceType enum indicating the type of race to create
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
        if (!course.getLegs().isEmpty()) {
            // Set Leg
            boat.setLegNumber(course.getLegs().get(0).getLegNumber());
            // Set Dest
            boat.setDestination(course.getLeg(boat.getLegNumber()).getDestination().getCoordinate());
            // Set Coordinate
            boat.setCoordinate(getStartPosition(boat));
            // Set Heading
            GPSCalculations gps = new GPSCalculations();
            boat.setHeading(gps.getBearing(boat.getCoordinate(), (boat.getDestination())));
            double tws = boat.getBoatTWS(course.getWindSpeed(), course.getWindDirection());
            boat.setSpeed(tws);
        }
    }


    /**
     * Method to calculate the starting position for a boat
     * Prevents boats from overlapping
     *
     * @param boat boat to get starting position for
     * @return position for boat to start at
     */
    private Coordinate getStartPosition(Boat boat) {
        GPSCalculations calculator = new GPSCalculations();
        Coordinate midPoint = course.getLeg(boat.getLegNumber()).getDeparture().getCoordinate();
        Coordinate startMark1 = course.getLeg(boat.getLegNumber()).getDeparture().getMarks().get(0).getCoordinate();
        Coordinate startMark2= course.getLeg(boat.getLegNumber()).getDeparture().getMarks().get(1).getCoordinate();

        double bearing = calculator.getBearing(startMark1, startMark2);

        double offset = startingList.size();

        if ((offset % 2) == 0) {
            offset /= 2;
        } else {
            offset = -Math.floor(offset/2);
        }

        Coordinate startingPosition = calculator.toCoordinate(midPoint, bearing, (boat.getLength()*offset+10));

        return startingPosition;
    }

    public void addParticipant(Boat boat) {
        // check that it is alright to add a boat at this point
        startingList.add(boat);
        setCourseForBoat(boat);
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
                if (hasPassedMark(boat) && mode != RaceMode.CONTROLS_TUTORIAL) {
                    setNextLeg(boat, course.getNextLeg(boat.getLegNumber()));
                }
            }
        }
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
     * Detects if there has been a collision between the boat and another abstract boat after updating the position
     *
     * @param boat to be moved
     * @param time that has passed
     */
    private void updatePosition(Boat boat, double time) {
        double speed = boat.getSpeed(); // knots
        if (boat.isSailOut()) {
            speed = 0;
        }

        List<AbstractBoat> obstacles = new ArrayList<>(startingList);
        obstacles.addAll(course.getMarks());
        AbstractBoat obstacle = null;
        if (mode != RaceMode.CONTROLS_TUTORIAL) {
            obstacle = boat.hasCollided(obstacles);
        }
        if (obstacle != null){
            handleCollision(boat, obstacle);
        } else {
            double mpsSpeed = new SpeedConverter().knotsToMs(speed); // convert to meters/second
            double secondsTime = time / 1000.0d;
            double distanceTravelled = mpsSpeed * secondsTime;

            // set next position based on current coordinate, distance travelled, and heading.
            boat.setCoordinate(gps.toCoordinate(boat.getCoordinate(), boat.getHeading(), distanceTravelled));
        }
    }


    /**
     * Handles the collision when one is detected by printing to the console
     * NOTE: Bumper car edition currently in play
     * @param boat boat collision was detected from
     * @param obstacle obstacle the boat collided with
     */
    private void handleCollision(Boat boat, AbstractBoat obstacle){
        GPSCalculations calculator = new GPSCalculations();
        double bearingOfCollision = calculator.getBearing(obstacle.getCoordinate(), boat.getCoordinate());
        Coordinate newBoatCoordinate = calculator.toCoordinate(boat.getCoordinate(), bearingOfCollision, boat.getLength());
        boat.setCoordinate(newBoatCoordinate);
        if (obstacle instanceof Boat) {
            Coordinate newObstacleCoordinate = calculator.toCoordinate(obstacle.getCoordinate(), bearingOfCollision, -obstacle.getLength());
            ((Boat) obstacle).setCoordinate(newObstacleCoordinate);
        }
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

        startingList.forEach(boat -> boat.setControlled(boat.getId().equals(playerId)));
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


    /**
     * Checks if a boat has past its destination mark.
     *
     * @param boat the boat to be checked
     * @return if has passed its destination mark
     */
    public boolean hasPassedMark(Boat boat) {
        return inPreRoundingZone(boat.getPreviousCoordinate(), boat.getLegNumber()) && inPostRoundingZone(boat.getCoordinate(), boat.getLegNumber());
    }

    /**
     * Method to check if a was is the pre pass zone for the destination of a leg
     *
     * @param coordinate coordinate to be checked
     * @param legNumber the leg number for the leg in course
     * @return true if coordinate is in pre pass zone
     */
    private boolean inPreRoundingZone(Coordinate coordinate, int legNumber) {
        Leg leg = course.getLeg(legNumber);
        double markToBoatHeading = GPSCalculations.getBearing(leg.getDestination().getCoordinate(), coordinate);

        double entryBearing = GPSCalculations.getBearing(leg.getDestination().getCoordinate(), leg.getDeparture().getCoordinate());
        GPSCalculations gps = new GPSCalculations();

        MarkRounding.Direction direction = course.getMarkRoundings().get(leg.getLegNumber()).getRoundingDirection();

        if (direction == MarkRounding.Direction.PORT) {
            return gps.isBetween(markToBoatHeading, leg.getDestination().getPassAngle(), entryBearing);
        } else if (direction == MarkRounding.Direction.STARBOARD) {
            return gps.isBetween(markToBoatHeading,  entryBearing, leg.getDestination().getPassAngle());
        }

        return false;
    }


    /**
     * Method to check if a coordinate is in the post pass zone for a leg
     *
     * @param coordinate coordinate to be checked
     * @param legNumber the leg number for the leg in course
     * @return true of coordinate is in [ost pass zone
     */
    private boolean inPostRoundingZone(Coordinate coordinate, int legNumber) {
        Leg leg = course.getLeg(legNumber + 1);
        double markToBoatHeading = GPSCalculations.getBearing(leg.getDeparture().getCoordinate(), coordinate);
        double exitBearing = GPSCalculations.getBearing(leg.getDeparture().getCoordinate(), leg.getDestination().getCoordinate());
//        System.out.println(leg.getDestination().getPassAngle());
        GPSCalculations gps = new GPSCalculations();

        MarkRounding.Direction direction = course.getMarkRoundings().get(leg.getLegNumber() - 1).getRoundingDirection();

        if (direction == MarkRounding.Direction.PORT) {
            return gps.isBetween(markToBoatHeading, exitBearing, leg.getDeparture().getPassAngle());
        } else if (direction == MarkRounding.Direction.STARBOARD) {
            return gps.isBetween(markToBoatHeading, leg.getDeparture().getPassAngle(), exitBearing);
        }

        return false;
    }


    public RaceMode getMode() {
        return mode;
    }


    public void setMode(RaceMode mode) {
        this.mode = mode;
    }
}