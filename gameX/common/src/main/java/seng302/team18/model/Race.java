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
    private int id;
    private RaceStatus status;
    private RaceType raceType;
    private Regatta regatta = new Regatta();
    private Course course;

    private List<Integer> participantIds;
    private List<Boat> startingList;
    private List<Boat> finishedList;

    private ZonedDateTime startTime = ZonedDateTime.now();
    private ZonedDateTime currentTime;

    private Integer playerId;

    private final GPSCalculations gps = new GPSCalculations();

    private final RoundingDetector detector = new RoundingDetector();

    public Race() {
        participantIds = new ArrayList<>();
        startingList = new ArrayList<>();
        course = new Course();
        finishedList = new ArrayList<>();
        id = 0;
        status = RaceStatus.NOT_ACTIVE;
        currentTime = ZonedDateTime.now(course.getTimeZone());
        startTime = ZonedDateTime.ofInstant(Instant.EPOCH, course.getTimeZone());
        raceType = RaceType.MATCH;
    }


    /**
     * Race class constructor.
     *
     * @param startingList ArrayList holding all entered boats
     * @param course       Course object
     * @param raceId       Integer representing the race id
     * @param raceType     RaceType enum indicating the type of race to create
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
        if (course.getMarkSequence().size() > 1) {
            for (Boat boat : startingList) {
                setCourseForBoat(boat);
            }
        }
    }


    private void setCourseForBoat(Boat boat) {
        if (course.getMarkSequence().size() > 1) {
            boat.setLegNumber(0);
            boat.setCoordinate(getStartPosition(boat));
            boat.setHeading(gps.getBearing(
                    course.getMarkSequence().get(0).getCompoundMark().getCoordinate(),
                    course.getMarkSequence().get(0).getCompoundMark().getCoordinate()
            ));
            boat.setSpeed(boat.getBoatTWS(course.getWindSpeed(), course.getWindDirection()));
            boat.setRoundZone(Boat.RoundZone.ZONE1);
            boat.setStatus(BoatStatus.PRE_START);
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
        MarkRounding startRounding = course.getMarkSequence().get(0);
        Coordinate midPoint = startRounding.getCompoundMark().getCoordinate();
        Coordinate startMark1 = startRounding.getCompoundMark().getMarks().get(0).getCoordinate();
        Coordinate startMark2 = startRounding.getCompoundMark().getMarks().get(1).getCoordinate();

        double bearing = gps.getBearing(startMark1, startMark2);

        double diff = (startRounding.getRoundingDirection().equals(MarkRounding.Direction.PS)) ? -90 : 90;
        double behind = (bearing + diff + 360) % 360;

        double offset = startingList.size();

        if ((offset % 2) == 0) {
            offset /= 2;
        } else {
            offset = -Math.floor(offset / 2);
        }

        Coordinate behindMidPoint = gps.toCoordinate(midPoint, behind, boat.getLength() * 3);
        return gps.toCoordinate(behindMidPoint, bearing, (boat.getLength() * offset + 10));
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
    public void updateBoats(double time) { // time in seconds
        for (Boat boat : startingList) {
            updatePosition(boat, time);
        }
    }


    /**
     * Sets the next Leg of the boat, updates the mark to show the boat has passed it,
     * and sets the destination to the next marks coordinates.
     *
     * @param boat    the boat
     * @param nextLeg the next leg
     */
    public void setNextLeg(Boat boat, int nextLeg) {
        int currentLeg = boat.getLegNumber();
        boat.setLegNumber(nextLeg);

        int newPlace = ((Long) startingList.stream().filter(b -> b.getLegNumber() >= currentLeg).count()).intValue() + 1;
        boat.setPlace(newPlace);

        // Adjust the placings of the other boats to account for the new placing of the boat
        // TODO afj19 09/08/17: Check that this is not the cause of issue #5 (see issue tracker)
        for (Boat currentBoat : getStartingList()) {
            int currentBoatPlace = currentBoat.placeProperty().intValue();
            int currentBoatLeg = currentBoat.getLegNumber();
            int boatLeg = boat.getLegNumber();

            if (!(currentBoat.getId().equals(boat.getId())) && (currentBoatPlace == newPlace)) {
                if (currentBoatLeg >= boatLeg) {
                    currentBoat.setPlace(currentBoatPlace - 1);
                } else {
                    currentBoat.setPlace(currentBoatPlace + 1);
                }
            }
        }

        markRoundingEvents.add(new MarkRoundingEvent(System.currentTimeMillis(), boat, course.getMarkSequence().get(currentLeg).getCompoundMark()));

        if (nextLeg == course.getMarkSequence().size()) {
            boat.setStatus(BoatStatus.FINISHED);
        }
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
        AbstractBoat obstacle = boat.hasCollided(obstacles);
        if (obstacle != null) {
            handleCollision(boat, obstacle);
        } else {
            double mpsSpeed = new SpeedConverter().knotsToMs(speed); // convert to meters/second
            double secondsTime = time / 1000.0d;
            double distanceTravelled = mpsSpeed * secondsTime;

            // set next position based on current coordinate, distance travelled, and heading.
            boat.setCoordinate(gps.toCoordinate(boat.getCoordinate(), boat.getHeading(), distanceTravelled));
        }

        if (boat.getStatus().equals(BoatStatus.RACING) && detector.hasPassedDestination(boat, course)) {
            setNextLeg(boat, boat.getLegNumber() + 1);
        }
    }


    /**
     * Handles the collision when one is detected by printing to the console
     * NOTE: Bumper car edition currently in play
     *
     * @param boat     boat collision was detected from
     * @param obstacle obstacle the boat collided with
     */
    private void handleCollision(Boat boat, AbstractBoat obstacle) {
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
}