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
                boat.setRoundZone(Boat.RoundZone.ZONE1);
            }
        }
    }

    private void setCourseForBoat(Boat boat) {
        if (!course.getLegs().isEmpty()) {
            // Set Leg
            boat.setLegNumber(course.getLegs().get(0).getLegNumber());
            // Set Dest
            boat.setDestination(course.getLeg(boat.getLegNumber()).getDestination().getCompoundMark().getCoordinate());
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
        Coordinate midPoint = course.getLeg(boat.getLegNumber()).getDeparture().getCompoundMark().getCoordinate();
        Coordinate startMark1 = course.getLeg(boat.getLegNumber()).getDeparture().getCompoundMark().getMarks().get(0).getCoordinate();
        Coordinate startMark2= course.getLeg(boat.getLegNumber()).getDeparture().getCompoundMark().getMarks().get(1).getCoordinate();

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
                updatePosition(boat, time);
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
        boat.setDestination(nextLeg.getDestination().getCompoundMark().getMarks().get(0).getCoordinate());
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
        markRoundingEvents.add(new MarkRoundingEvent(System.currentTimeMillis(), boat, course.getLeg(boat.getLegNumber()).getDeparture().getCompoundMark()));
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
        AbstractBoat obstacle = boat.hasCollided(obstacles);
        if (obstacle != null){
            handleCollision(boat, obstacle);
        } else {
            double mpsSpeed = new SpeedConverter().knotsToMs(speed); // convert to meters/second
            double secondsTime = time / 1000.0d;
            double distanceTravelled = mpsSpeed * secondsTime;

            // set next position based on current coordinate, distance travelled, and heading.
            boat.setCoordinate(gps.toCoordinate(boat.getCoordinate(), boat.getHeading(), distanceTravelled));
        }

        if (hasPassedDestination(boat)) {
            setNextLeg(boat, course.getNextLeg(boat.getLegNumber()));
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
    private boolean hasPassedDestination(Boat boat) {
        System.out.println(course.getLeg(boat.getLegNumber()).getDestination().getCompoundMark().getName());
        if(course.getLeg(boat.getLegNumber()).getDestination().getCompoundMark().getMarks().size() == 1) { //if boat is rounding a mark
            return hasPassedMark(boat);
        } else {
            return hasPassedGate(boat);
        }
    }


    /**
     * Method to check if a boat has passed the mark it is heading too
     *
     * @param boat
     * @return true if the boat has passed its destination mark
     */
    private boolean hasPassedMark(Boat boat) {
        return inPreRoundingZone(boat.getPreviousCoordinate(), boat.getLegNumber()) && inPostRoundingZone(boat.getCoordinate(), boat.getLegNumber());
    }


    /**
     * Method to check if a boat has passed the gate it is heading too
     *
     * @param boat
     * @return true if the boat has passed its destination gate
     */
    private boolean hasPassedGate(Boat boat) {
        MarkRounding.GateType  gateType = course.getLeg(boat.getLegNumber()).getDestination().getGateType();
        boolean passed = false;
        if (gateType ==  MarkRounding.GateType.THROUGH_GATE) {
            passed = checkForThroughGate(boat);
        } else if (gateType ==  MarkRounding.GateType.ROUND_THEN_THROUGH) {
            passed = checkForRoundThenThroughGate(boat);
        } else if (gateType ==  MarkRounding.GateType.THROUGH_THEN_ROUND) {
            passed = checkForThroughThenRoundGate(boat);
        }else if (gateType ==  MarkRounding.GateType.ROUND_BOTH_MAKRS) {
            passed = checkForRoundBothMarksGate(boat);
        }
        return passed;
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
        double markToBoatHeading = GPSCalculations.getBearing(leg.getDestination().getCompoundMark().getCoordinate(), coordinate);

        double entryBearing = GPSCalculations.getBearing(leg.getDestination().getCompoundMark().getCoordinate(), leg.getDeparture().getCompoundMark().getCoordinate());
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
        double markToBoatHeading = GPSCalculations.getBearing(leg.getDeparture().getCompoundMark().getCoordinate(), coordinate);
        double exitBearing = GPSCalculations.getBearing(leg.getDeparture().getCompoundMark().getCoordinate(), leg.getDestination().getCompoundMark().getCoordinate());
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


    public boolean checkForThroughGate(Boat boat) {
        MarkRounding gate = course.getLeg(boat.getLegNumber()).getDestination();
        GPSCalculations calculator = new GPSCalculations();
        double mark1ToBoatBearing = GPSCalculations.getBearing(gate.getCompoundMark().getMarks().get(0).getCoordinate(), boat.getCoordinate());
        double mark2ToBoatBearing = GPSCalculations.getBearing(gate.getCompoundMark().getMarks().get(1).getCoordinate(), boat.getCoordinate());
        double mark1ToMark2Bearing = GPSCalculations.getBearing(gate.getCompoundMark().getMarks().get(0).getCoordinate(), gate.getCompoundMark().getMarks().get(1).getCoordinate());
        double mark2ToMark1Bearing = GPSCalculations.getBearing(gate.getCompoundMark().getMarks().get(1).getCoordinate(), gate.getCompoundMark().getMarks().get(0).getCoordinate());

        if (calculator.isBetween(mark1ToBoatBearing, (mark1ToMark2Bearing-90)%360, (mark1ToMark2Bearing+90)%360)
            && calculator.isBetween(mark2ToBoatBearing, (mark2ToMark1Bearing-90)%360, (mark2ToMark1Bearing+90)%360)) {
            double mark1toBoatPrevious = GPSCalculations.getBearing(gate.getCompoundMark().getMarks().get(0).getCoordinate(), boat.getPreviousCoordinate());
            if (calculator.isBetween(mark1ToBoatBearing, (mark1ToMark2Bearing+180)%360, mark1ToMark2Bearing) &&
                    !calculator.isBetween(mark1toBoatPrevious, (mark1ToMark2Bearing+180)%360, mark1ToMark2Bearing)) {
                return true;
            }
        }
        return false;
    }


    public boolean checkForRoundThenThroughGate(Boat boat) {
        MarkRounding gate = course.getLeg(boat.getLegNumber()).getDestination();
        Mark mark1 = gate.getCompoundMark().getMarks().get(0);
        Mark mark2 = gate.getCompoundMark().getMarks().get(1);
        GPSCalculations calculator = new GPSCalculations();

        double mark1ToBoatBearing = GPSCalculations.getBearing(mark1.getCoordinate(), boat.getCoordinate());
        double mark2ToBoatBearing = GPSCalculations.getBearing(mark2.getCoordinate(), boat.getCoordinate());
        double mark1ToMark2Bearing = GPSCalculations.getBearing(mark1.getCoordinate(), mark2.getCoordinate());
        double mark2ToMark1Bearing = GPSCalculations.getBearing(mark2.getCoordinate(), mark1.getCoordinate());
        if (boat.getRoundZone() == Boat.RoundZone.ZONE1) {
            if (calculator.isBetween(mark1ToBoatBearing, (mark2ToMark1Bearing - 90) % 360, (mark2ToMark1Bearing + 90) % 360)) { // pre-rounding mark1
                double previousAngle = GPSCalculations.getBearing(mark1.getCoordinate(), boat.getPreviousCoordinate());
                if (calculator.isBetween(previousAngle, mark2ToMark1Bearing, mark1ToMark2Bearing) && !(calculator.isBetween(mark1ToBoatBearing, mark2ToMark1Bearing, mark1ToMark2Bearing))) {
                    boat.setRoundZone(Boat.RoundZone.ZONE2);
                }
            } else if (calculator.isBetween(mark2ToBoatBearing, (mark1ToMark2Bearing - 90) % 360, (mark1ToMark2Bearing + 90) % 360)) { // pre-rounding mark2
                double previousAngle = GPSCalculations.getBearing(mark1.getCoordinate(), boat.getPreviousCoordinate());
                if (calculator.isBetween(previousAngle, mark2ToMark1Bearing, mark1ToMark2Bearing) && !(calculator.isBetween(mark1ToBoatBearing, mark2ToMark1Bearing, mark1ToMark2Bearing))) {
                    boat.setRoundZone(Boat.RoundZone.ZONE2);
                }
            }
        } else if (boat.getRoundZone() == Boat.RoundZone.ZONE2) {
            if(checkForThroughGate(boat)) {
                boat.setRoundZone(Boat.RoundZone.ZONE1);
                return true;
            }
        }
        return false;
    }

    public boolean checkForRoundBothMarksGate(Boat boat) {
        //Note implemented as not necessary
        return true;
    }


    public boolean checkForThroughThenRoundGate(Boat boat) {
        MarkRounding gate = course.getLeg(boat.getLegNumber()).getDestination();
        Mark mark1 = gate.getCompoundMark().getMarks().get(0);
        Mark mark2 = gate.getCompoundMark().getMarks().get(1);
        GPSCalculations calculator = new GPSCalculations();

        double mark1ToBoatBearing = GPSCalculations.getBearing(mark1.getCoordinate(), boat.getCoordinate());
        double mark2ToBoatBearing = GPSCalculations.getBearing(mark2.getCoordinate(), boat.getCoordinate());
        double mark1ToMark2Bearing = GPSCalculations.getBearing(mark1.getCoordinate(), mark2.getCoordinate());
        double mark2ToMark1Bearing = GPSCalculations.getBearing(mark2.getCoordinate(), mark1.getCoordinate());

        if (boat.getRoundZone() == Boat.RoundZone.ZONE1) {
            if (checkForThroughGate(boat)) {
                boat.setRoundZone(Boat.RoundZone.ZONE2);
                return false;
            }
        } else if (boat.getRoundZone() == Boat.RoundZone.ZONE2) {
            if (calculator.isBetween(mark1ToBoatBearing, (mark2ToMark1Bearing - 90) % 360, (mark2ToMark1Bearing + 90) % 360)) { //rounding mark 1
                double previousAngle = GPSCalculations.getBearing(mark1.getCoordinate(), boat.getPreviousCoordinate());
                if (calculator.isBetween(previousAngle, mark2ToMark1Bearing, mark1ToMark2Bearing) && !(calculator.isBetween(mark1ToBoatBearing, mark2ToMark1Bearing, mark1ToMark2Bearing))) {
                    boat.setRoundZone(Boat.RoundZone.ZONE1);
                    return true;
                }
            } else if (calculator.isBetween(mark2ToBoatBearing, (mark1ToMark2Bearing - 90) % 360, (mark1ToMark2Bearing + 90) % 360)) { //rounding mark 2
                double previousAngle = GPSCalculations.getBearing(mark1.getCoordinate(), boat.getPreviousCoordinate());
                if (calculator.isBetween(previousAngle, mark2ToMark1Bearing, mark1ToMark2Bearing) && !(calculator.isBetween(mark1ToBoatBearing, mark2ToMark1Bearing, mark1ToMark2Bearing))) {
                    boat.setRoundZone(Boat.RoundZone.ZONE1);
                    return true;
                }
            }
        }

        return false;
    }
}