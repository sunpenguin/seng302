package seng302.team18.model;

import seng302.team18.util.GPSCalculator;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * A class to represent an individual race.
 */
public class Race extends Observable {
    private final GPSCalculator gps = new GPSCalculator();
    private final RoundingDetector detector = new RoundingDetector();
    private int id;
    private RaceStatus status;
    private RaceType raceType;
    private Regatta regatta = new Regatta();
    private Course course;
    private List<Integer> participantIds;
    private List<Boat> startingList;
    private ZonedDateTime startTime = ZonedDateTime.now();
    private ZonedDateTime currentTime;
    private Integer playerId;
    private List<MarkRoundingEvent> markRoundingEvents = new ArrayList<>();
    private List<YachtEvent> yachtEvents = new ArrayList<>();
    private RaceMode mode = RaceMode.RACE;

    public Race() {
        participantIds = new ArrayList<>();
        startingList = new ArrayList<>();
        course = new Course();
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
        participantIds = startingList.stream()
                .map(Boat::getId)
                .collect(Collectors.toList());
        this.id = raceId;
        this.status = RaceStatus.NOT_ACTIVE;
        this.raceType = raceType;
        setCourseForBoats();
    }


    /**
     * Randomly places power ups.
     * @param powerUps number of power ups to place
     */
    public void addPowerUps(int powerUps) {
        GPSCalculator calculator = new GPSCalculator();
        List<Coordinate> corners = gps.findMinMaxPoints(course);
        for (int i = 0; i < powerUps; i++) {
            Coordinate randomPoint = calculator.randomPoint(course.getCourseLimits());
            course.getCompoundMarks().add(new CompoundMark("a", Arrays.asList(new Mark(i * 3 + 2, randomPoint)), i * 2 + 3));
        }
    }


    /**
     * Called in Race constructor.
     * Set up the course CompoundMarks for each boat in the race as well as set the
     * current(starting CompoundMark) and next CompoundMark.
     */
    public void setCourseForBoats() {
        if (course.getMarkSequence().size() > 1) {
            for (Boat boat : startingList) {
                setCourseForBoat(boat);
            }
        }
    }


    private void setCourseForBoat(Boat boat) {
        if (course.getMarkSequence().size() > 1) {
            boat.setLegNumber(0);
            boat.setCoordinate(getStartPosition(boat, boat.getLength() * 3));
            boat.setHeading(gps.getBearing(
                    course.getMarkSequence().get(0).getCompoundMark().getCoordinate(),
                    course.getMarkSequence().get(1).getCompoundMark().getCoordinate()
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
     * @param boat       boat to get starting position for
     * @param distBehind the distance behind the start line to place the boat (m)
     * @return position for boat to start at
     */
    private Coordinate getStartPosition(Boat boat, double distBehind) {
        MarkRounding startRounding = course.getMarkSequence().get(0);
        Coordinate midPoint = startRounding.getCompoundMark().getCoordinate();
        Coordinate startMark1 = startRounding.getCompoundMark().getMarks().get(0).getCoordinate();
        Coordinate startMark2 = startRounding.getCompoundMark().getMarks().get(1).getCoordinate();

        double bearing = gps.getBearing(startMark1, startMark2);

        double diff = (startRounding.getRoundingDirection().equals(MarkRounding.Direction.PS)) ? 90 : -90;
        double behind = (bearing + diff + 360) % 360;

        double offset = startingList.size();

        if ((offset % 2) == 0) {
            offset /= 2;
        } else {
            offset = -Math.floor(offset / 2);
        }

        Coordinate behindMidPoint = gps.toCoordinate(midPoint, behind, distBehind);
        return gps.toCoordinate(behindMidPoint, bearing, (boat.getLength() * offset + 10));
    }


    public void addParticipant(Boat boat) {
        // check that it is alright to add a boat at this point
        startingList.add(boat);
        setCourseForBoat(boat);
        participantIds.add(boat.getId());
    }


    /**
     * Removes a participant from the race
     * @param boatID id for the participant to be removed
     */
    public void removeParticipant (int boatID) {
        for (Boat boat : startingList) {
            if(boat.getId().equals(boatID)){
                startingList.remove(boat);
                participantIds.remove(boatID);
            }
        }
    }


    /**
     * Set the status for boat.
     *
     * @param id Source ID for the boat to have its status changed.
     */
    public void setBoatStatus(Integer id, BoatStatus status) {
        for (Boat boat : startingList) {
            if (boat.getId().equals(id)) {
                boat.setStatus(status);
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
     * Updates the race by the given amount of time.
     *
     * @param time the time in seconds
     */
    public void update(double time) { // time in seconds
        for (Boat boat : startingList) {
            collisionStuff(boat);
            boat.update(time);
            roundingStuff(boat);
            boundaryStuff(boat);
            powerUpStuff(boat);
        }
    }


    /**
     * Determines if a boat picked up a power up.
     * @param boat
     */
    private void powerUpStuff(Boat boat) {

    }


    /**
     * Disqualifies boat if outside boundary.
     *
     * @param boat to check
     */
    private void boundaryStuff(Boat boat) {
        if (isOutSide(boat)) {
            boat.setStatus(BoatStatus.DSQ);
            setChanged();
            notifyObservers(boat);
        }
    }


    /**
     * Check if a boat is outside the boundary
     *
     * @param boat to check
     * @return if the boat is outside.
     */
    private boolean isOutSide(Boat boat) {
        GPSCalculator calculator = new GPSCalculator();
        List<Coordinate> boundaries = course.getCourseLimits();

        return boundaries.size() > 2
                && !calculator.isInside(boat.getCoordinate(), boundaries)
                && !(boat.getStatus().equals(BoatStatus.DSQ));
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

        if (currentLeg == nextLeg) return;

        final int newPlace = ((Long) startingList.stream().filter(b -> b.getLegNumber() >= nextLeg).count()).intValue() + 1;
        final int oldPace = boat.getPlace();

        if (oldPace < newPlace) {
            startingList.stream()
                    .filter(boat1 -> boat1.getPlace() <= newPlace)
                    .filter(boat1 -> oldPace < boat1.getPlace())
                    .forEach(boat1 -> boat1.setPlace(boat1.getPlace() + 1));
        } else if (newPlace < oldPace) {
            startingList.stream()
                    .filter(boat1 -> boat1.getPlace() < oldPace)
                    .filter(boat1 -> newPlace <= boat1.getPlace())
                    .forEach(boat1 -> boat1.setPlace(boat1.getPlace() + 1));
        }

        boat.setPlace(newPlace);
        markRoundingEvents.add(new MarkRoundingEvent(System.currentTimeMillis(), boat, course.getMarkSequence().get(currentLeg)));

        if (nextLeg == course.getMarkSequence().size()) {
            boat.setStatus(BoatStatus.FINISHED);
            boat.setSpeed(0);
            boat.setSailOut(true);
        }

        boat.setLegNumber(nextLeg);
    }


    /**
     * Detects if there has been a collision between the boat and another abstract boat after updating the position
     *
     * @param boat to be collision stuffed
     */
    private void collisionStuff(Boat boat) {
        List<BodyMass> objects = new ArrayList<>();
        for (Boat b : startingList) {
            if (!b.getId().equals(boat.getId())) {
                objects.add(b.getBodyMass());
            }
        }

        for (Mark mark : course.getMarks()) {
            objects.add(mark.getBodyMass());
        }
        for (BodyMass object: objects) {
            if (boat.hasCollided(object)) {
                handleCollision(boat.getBodyMass(), object);
            }
        }
    }


    /**
     * Handles the collision when one is detected by printing to the console
     * NOTE: Bumper car edition currently in play
     *
     * @param object   boat collision was detected from
     * @param obstacle obstacle the boat collided with
     */
    private void handleCollision(BodyMass object, BodyMass obstacle) {
        final double totalPushBack = 25; // meters
        GPSCalculator calculator = new GPSCalculator();
        double bearingOfCollision = calculator.getBearing(obstacle.getLocation(), object.getLocation());
        double ratio = object.getWeight() + obstacle.getWeight();
        double obstacleBackUpDistance = totalPushBack * (object.getWeight() / ratio);
        double objectBackUpDistance = totalPushBack * (obstacle.getWeight() / ratio);
        Coordinate newBoatCoordinate = calculator.toCoordinate(object.getLocation(), bearingOfCollision, objectBackUpDistance);
        object.setLocation(newBoatCoordinate);
        Coordinate newObstacleCoordinate = calculator.toCoordinate(obstacle.getLocation(), bearingOfCollision, -obstacleBackUpDistance);
        obstacle.setLocation(newObstacleCoordinate);
    }


    /**
     * Detects roundings and updates boat status.
     *
     * @param boat to update.
     */
    public void roundingStuff(Boat boat) {
        if (!mode.equals(RaceMode.CONTROLS_TUTORIAL)) {
            if (boat.getStatus().equals(BoatStatus.RACING) && detector.hasPassedDestination(boat, course)) {
                setNextLeg(boat, boat.getLegNumber() + 1);
            } else if (boat.getStatus().equals(BoatStatus.PRE_START) && boat.getLegNumber() == 0
                    && detector.hasPassedDestination(boat, course)) {
                statusOSCPenalty(boat);
            } else if (boat.getStatus().equals(BoatStatus.OCS) && currentTime.isAfter(startTime.plusSeconds(5))) {
                yachtEvents.add(new YachtEvent(System.currentTimeMillis(), boat.getId(), YachtEventCode.OCS_PENALTY_COMPLETE));
                boat.setStatus(BoatStatus.RACING);
                boat.setSpeed(boat.getBoatTWS(course.getWindSpeed(), course.getWindDirection()));
            }
        }
    }


    /**
     * Sets the boat to appropriate conditions when a boat has an OCS status.
     *
     * @param boat the boat which has the OCS status
     */
    private void statusOSCPenalty(Boat boat) {
        yachtEvents.add(new YachtEvent(System.currentTimeMillis(), boat.getId(), YachtEventCode.OVER_START_LINE_EARLY));
        boat.setStatus(BoatStatus.OCS);

        boat.setCoordinate(getStartPosition(boat, boat.getLength() * 6));

        boat.setSpeed(0);
        boat.setSailOut(true);
    }



    public boolean isFinished() {
        Collection<BoatStatus> finishedStatuses = Arrays.asList(BoatStatus.DNF, BoatStatus.DNS, BoatStatus.FINISHED, BoatStatus.DSQ);
        int numFinished = (int) startingList.stream()
                .filter(boat -> finishedStatuses.contains(boat.getStatus()))
                .count();
        return startingList.size() == numFinished && startingList.size() != 0;
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


    public ZonedDateTime getCurrentTime() {
        return currentTime;
    }


    public void setCurrentTime(ZonedDateTime currentTime) {
        this.currentTime = currentTime;
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


    public List<MarkRoundingEvent> popMarkRoundingEvents() {
        List<MarkRoundingEvent> events = markRoundingEvents;
        markRoundingEvents = new ArrayList<>();
        return events;
    }


    public List<YachtEvent> popYachtEvents() {
        List<YachtEvent> events = yachtEvents;
        yachtEvents = new ArrayList<>();
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


    public void setRaceType(RaceType raceType) {
        this.raceType = raceType;
    }


    public Regatta getRegatta() {
        return regatta;
    }


    public void setRegatta(Regatta regatta) {
        this.regatta = regatta;
    }


    /**
     * Gets a boat from the race given an ID.
     *
     * @param id the ID of the boat.
     * @return the boat with the specified ID, null otherwise.
     */
    public Boat getBoat(int id) {
        for (Boat boat : startingList) {
            if (boat.getId() == id) {
                return boat;
            }
        }

        return null;
    }


    public RaceMode getMode() {
        return mode;
    }


    public void setMode(RaceMode mode) {
        this.mode = mode;
    }
}