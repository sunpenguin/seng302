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
        Coordinate midPoint = course.getMarkSequence().get(0).getCompoundMark().getCoordinate();
        Coordinate startMark1 = course.getMarkSequence().get(0).getCompoundMark().getMarks().get(0).getCoordinate();
        Coordinate startMark2 = course.getMarkSequence().get(0).getCompoundMark().getMarks().get(1).getCoordinate();

        double bearing = gps.getBearing(startMark1, startMark2);

        double offset = startingList.size();

        if ((offset % 2) == 0) {
            offset /= 2;
        } else {
            offset = -Math.floor(offset / 2);
        }

        return gps.toCoordinate(midPoint, bearing, (boat.getLength() * offset + 10));
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
            if (!finishedList.contains(boat)) {
                updatePosition(boat, time);
            }
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

        if (hasPassedDestination(boat)) {
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


    /**
     * Checks if a boat has past its destination mark.
     *
     * @param boat the boat to be checked
     * @return if has passed its destination mark
     */
    private boolean hasPassedDestination(Boat boat) {
        // TODO afj19 10/08/17: check that this is never called once the boat is finished, or make sure that we won't get index exceptions accessing the roundings

        boolean passed;
        if (course.getMarkSequence().get(boat.getLegNumber()).getCompoundMark().getMarks().size() == 1) { //if boat is rounding a mark
            passed = hasPassedMark(boat);
        } else {
            passed = hasPassedGate(boat);
        }
        System.out.println(course.getMarkSequence().get(boat.getLegNumber()).getCompoundMark().getName() + " " + passed);

        return passed;
    }


    /**
     * Method to check if a boat has passed the mark it is heading too
     *
     * @param boat the boat in question
     * @return true if the boat has passed its destination mark
     */
    private boolean hasPassedMark(Boat boat) {
        return inPreRoundingZone(boat.getPreviousCoordinate(), boat.getLegNumber()) && inPostRoundingZone(boat.getCoordinate(), boat.getLegNumber());
    }


    /**
     * Method to check if a boat has passed the gate it is heading too
     *
     * @param boat the boat in question
     * @return true if the boat has passed its destination gate
     */
    private boolean hasPassedGate(Boat boat) {
        MarkRounding.GateType gateType = course.getMarkSequence().get(boat.getLegNumber() + 1).getGateType();
        boolean passed = false;
        if (gateType == MarkRounding.GateType.THROUGH_GATE) {
            passed = checkForThroughGate(boat);
        } else if (gateType == MarkRounding.GateType.ROUND_THEN_THROUGH) {
            passed = checkForRoundThenThroughGate(boat);
        } else if (gateType == MarkRounding.GateType.THROUGH_THEN_ROUND) {
            passed = checkForThroughThenRoundGate(boat);
        } else if (gateType == MarkRounding.GateType.ROUND_BOTH_MAKRS) {
            // Not checking for this type as it should never be encountered
            passed = true;
        }
        return passed;
    }


    /**
     * Method to check if a was is the pre pass zone for the destination of a leg
     *
     * @param coordinate coordinate to be checked
     * @param legNumber  the leg number for the leg in course
     * @return true if coordinate is in pre pass zone
     */
    // TODO afj19 10/08/17: deal with fact that this is only for marks and thus the leg is pre-start or post-finish (add dbc precondition?)
    private boolean inPreRoundingZone(Coordinate coordinate, int legNumber) {
        MarkRounding legStart = course.getMarkSequence().get(legNumber - 1);
        CompoundMark legEnd = course.getMarkSequence().get(legNumber).getCompoundMark();

        double markToBoatHeading = gps.getBearing(legEnd.getCoordinate(), coordinate);
        double entryBearing = gps.getBearing(legEnd.getCoordinate(), legStart.getCompoundMark().getCoordinate());

        MarkRounding.Direction direction = course.getMarkSequence().get(legNumber).getRoundingDirection();

        if (direction == MarkRounding.Direction.PORT) {
            return gps.isBetween(markToBoatHeading, legStart.getPassAngle(), entryBearing);
        } else if (direction == MarkRounding.Direction.STARBOARD) {
            return gps.isBetween(markToBoatHeading, entryBearing, legStart.getPassAngle());
        }

        return false;
    }


    /**
     * Method to check if a coordinate is in the post pass zone for a leg
     *
     * @param coordinate coordinate to be checked
     * @param legNumber  the leg number for the leg in course
     * @return true of coordinate is in post pass zone
     */
    // TODO afj19 10/08/17: deal with fact that this is only for marks and thus the leg is pre-start or post-finish (add dbc precondition?)
    private boolean inPostRoundingZone(Coordinate coordinate, int legNumber) {
        MarkRounding legStart = course.getMarkSequence().get(legNumber);
        CompoundMark legEnd = course.getMarkSequence().get(legNumber + 1).getCompoundMark();

        double markToBoatHeading = gps.getBearing(legStart.getCompoundMark().getCoordinate(), coordinate);
        double exitBearing = gps.getBearing(legStart.getCompoundMark().getCoordinate(), legEnd.getCoordinate());

        MarkRounding.Direction direction = course.getMarkSequence().get(legNumber - 1).getRoundingDirection();

        if (direction == MarkRounding.Direction.PORT) {
            return gps.isBetween(markToBoatHeading, exitBearing, legStart.getPassAngle());
        } else if (direction == MarkRounding.Direction.STARBOARD) {
            return gps.isBetween(markToBoatHeading, legStart.getPassAngle(), exitBearing);
        }

        return false;
    }


    private boolean checkForThroughGate(Boat boat) {
        MarkRounding gate = course.getMarkSequence().get(boat.getLegNumber());
        double mark1ToBoatBearing = this.gps.getBearing(gate.getCompoundMark().getMarks().get(0).getCoordinate(), boat.getCoordinate());
        double mark2ToBoatBearing = this.gps.getBearing(gate.getCompoundMark().getMarks().get(1).getCoordinate(), boat.getCoordinate());
        double mark1ToMark2Bearing = this.gps.getBearing(gate.getCompoundMark().getMarks().get(0).getCoordinate(), gate.getCompoundMark().getMarks().get(1).getCoordinate());
        double mark2ToMark1Bearing = this.gps.getBearing(gate.getCompoundMark().getMarks().get(1).getCoordinate(), gate.getCompoundMark().getMarks().get(0).getCoordinate());

        if (gps.isBetween(mark1ToBoatBearing, (mark1ToMark2Bearing - 90) % 360, (mark1ToMark2Bearing + 90) % 360)
                && gps.isBetween(mark2ToBoatBearing, (mark2ToMark1Bearing - 90) % 360, (mark2ToMark1Bearing + 90) % 360)) {
            double mark1toBoatPrevious = gps.getBearing(gate.getCompoundMark().getMarks().get(0).getCoordinate(), boat.getPreviousCoordinate());
            if (gps.isBetween(mark1ToBoatBearing, (mark1ToMark2Bearing + 180) % 360, mark1ToMark2Bearing) &&
                    !gps.isBetween(mark1toBoatPrevious, (mark1ToMark2Bearing + 180) % 360, mark1ToMark2Bearing)) {
                return true;
            }
        }
        return false;
    }


    private boolean checkForRoundThenThroughGate(Boat boat) {
        MarkRounding gate = course.getMarkSequence().get(boat.getLegNumber());
        Mark mark1 = gate.getCompoundMark().getMarks().get(0);
        Mark mark2 = gate.getCompoundMark().getMarks().get(1);

        double mark1ToBoatBearing = this.gps.getBearing(mark1.getCoordinate(), boat.getCoordinate());
        double mark2ToBoatBearing = this.gps.getBearing(mark2.getCoordinate(), boat.getCoordinate());
        double mark1ToMark2Bearing = this.gps.getBearing(mark1.getCoordinate(), mark2.getCoordinate());
        double mark2ToMark1Bearing = this.gps.getBearing(mark2.getCoordinate(), mark1.getCoordinate());
        if (boat.getRoundZone() == Boat.RoundZone.ZONE1) {
            if (gps.isBetween(mark1ToBoatBearing, (mark2ToMark1Bearing - 90) % 360, (mark2ToMark1Bearing + 90) % 360)) { // pre-rounding mark1
                double previousAngle = this.gps.getBearing(mark1.getCoordinate(), boat.getPreviousCoordinate());
                if (gps.isBetween(previousAngle, mark2ToMark1Bearing, mark1ToMark2Bearing) && !(gps.isBetween(mark1ToBoatBearing, mark2ToMark1Bearing, mark1ToMark2Bearing))) {
                    boat.setRoundZone(Boat.RoundZone.ZONE2);
                }
            } else if (gps.isBetween(mark2ToBoatBearing, (mark1ToMark2Bearing - 90) % 360, (mark1ToMark2Bearing + 90) % 360)) { // pre-rounding mark2
                double previousAngle = this.gps.getBearing(mark1.getCoordinate(), boat.getPreviousCoordinate());
                if (gps.isBetween(previousAngle, mark2ToMark1Bearing, mark1ToMark2Bearing) && !(gps.isBetween(mark1ToBoatBearing, mark2ToMark1Bearing, mark1ToMark2Bearing))) {
                    boat.setRoundZone(Boat.RoundZone.ZONE2);
                }
            }
        } else if (boat.getRoundZone() == Boat.RoundZone.ZONE2) {
            if (checkForThroughGate(boat)) {
                boat.setRoundZone(Boat.RoundZone.ZONE1);
                return true;
            }
        }
        return false;
    }


    private boolean checkForThroughThenRoundGate(Boat boat) {
        MarkRounding gate = course.getMarkSequence().get(boat.getLegNumber());
        Mark mark1 = gate.getCompoundMark().getMarks().get(0);
        Mark mark2 = gate.getCompoundMark().getMarks().get(1);

        double mark1ToBoatBearing = this.gps.getBearing(mark1.getCoordinate(), boat.getCoordinate());
        double mark2ToBoatBearing = this.gps.getBearing(mark2.getCoordinate(), boat.getCoordinate());
        double mark1ToMark2Bearing = this.gps.getBearing(mark1.getCoordinate(), mark2.getCoordinate());
        double mark2ToMark1Bearing = this.gps.getBearing(mark2.getCoordinate(), mark1.getCoordinate());

        if (boat.getRoundZone() == Boat.RoundZone.ZONE1) {
            if (checkForThroughGate(boat)) {
                boat.setRoundZone(Boat.RoundZone.ZONE2);
                return false;
            }
        } else if (boat.getRoundZone() == Boat.RoundZone.ZONE2) {
            if (gps.isBetween(mark1ToBoatBearing, (mark2ToMark1Bearing - 90) % 360, (mark2ToMark1Bearing + 90) % 360)) { //rounding mark 1
                double previousAngle = this.gps.getBearing(mark1.getCoordinate(), boat.getPreviousCoordinate());
                if (gps.isBetween(previousAngle, mark2ToMark1Bearing, mark1ToMark2Bearing) && !(gps.isBetween(mark1ToBoatBearing, mark2ToMark1Bearing, mark1ToMark2Bearing))) {
                    boat.setRoundZone(Boat.RoundZone.ZONE1);
                    return true;
                }
            } else if (gps.isBetween(mark2ToBoatBearing, (mark1ToMark2Bearing - 90) % 360, (mark1ToMark2Bearing + 90) % 360)) { //rounding mark 2
                double previousAngle = this.gps.getBearing(mark1.getCoordinate(), boat.getPreviousCoordinate());
                if (gps.isBetween(previousAngle, mark2ToMark1Bearing, mark1ToMark2Bearing) && !(gps.isBetween(mark1ToBoatBearing, mark2ToMark1Bearing, mark1ToMark2Bearing))) {
                    boat.setRoundZone(Boat.RoundZone.ZONE1);
                    return true;
                }
            }
        }

        return false;
    }
}