package seng302.team18.model;

import seng302.team18.model.updaters.Updater;
import seng302.team18.util.GPSCalculator;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * A class to represent an individual race.
 */
public class Race {
    private final GPSCalculator gps = new GPSCalculator();
    private final RoundingDetector detector = new RoundingDetector();
    private int id;
    private RaceStatus status = RaceStatus.PRESTART;
    private RaceType raceType;
    private Regatta regatta = new Regatta();
    private Course course;
    private List<Integer> participantIds;
    private List<Boat> startingList;
    private ZonedDateTime startTime = ZonedDateTime.now();
    private ZonedDateTime currentTime;
    private List<MarkRoundingEvent> markRoundingEvents = new ArrayList<>();
    private List<YachtEvent> yachtEvents = new ArrayList<>();
    private List<PowerUpEvent> powerEvents = new ArrayList<>();
    private RaceMode mode = RaceMode.RACE;
    private List<Updater> updaters = new ArrayList<>();
    private int powerId = 0;
    private StartPositionSetter positionSetter;


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
     * @param startingList List holding all entered boats
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
     *
     * @param powerUps number of power ups to place.
     * @param prototype the base PickUp.
     * @param duration how long the PickUp will last in milliseconds.
     */
    public void addPickUps(int powerUps, PickUp prototype, double duration) {
        GPSCalculator calculator = new GPSCalculator();
        int maxId = powerId + powerUps;
        while (powerId < maxId) {
            Coordinate randomPoint = calculator.randomPoint(course.getCourseLimits());
            PickUp pickUp = makePickUp(powerId, randomPoint, prototype, duration);
            course.addPickUp(pickUp);
            powerId += 1;
        }
    }


    /**
     * Creates a single PickUp.
     *
     * @param id of the new PickUp.
     * @param randomPoint location of the new PickUp.
     * @param prototype the base PickUp.
     * @param duration how long the PickUp will last in milliseconds.
     * @return the new PickUp.
     */
    private PickUp makePickUp(int id, Coordinate randomPoint, PickUp prototype, double duration) {
        final double timeout =  System.currentTimeMillis() + duration;
        PickUp pickUp = prototype.clone();
        pickUp.setId(id);
        pickUp.setTimeout(timeout);
        pickUp.setPower(getRandomPower());
        pickUp.setLocation(randomPoint);
        return pickUp;
    }


    /**
     * Generates a random PowerUp to put in the PickUp.
     *
     * @return a random PowerUp.
     */
    private PowerUp getRandomPower() {
        PowerUp powerUp = new SpeedPowerUp(3);
        powerUp.setDuration(5000d);
        return powerUp;
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
            boat.setCoordinate(positionSetter.getBoatPosition(boat, course, startingList.size()));
            boat.setHeading(gps.getBearing(
                    boat.getCoordinate(),
                    course.getMarkRounding(0).getCoordinate()
            ));
            boat.setSpeed(boat.getBoatTWS(course.getWindSpeed(), course.getWindDirection()));
            boat.setRoundZone(Boat.RoundZone.ZONE1);
            boat.setStatus(BoatStatus.PRE_START);
        }
    }


    public void addParticipant(Boat boat) {
        // check that it is alright to add a boat at this point
        startingList.add(boat);
        setCourseForBoat(boat);
        participantIds.add(boat.getId());
    }


    /**
     * Removes a participant from the race
     *
     * @param boatID id for the participant to be removed
     */
    public void removeParticipant(int boatID) {
        for (Boat boat : startingList) {
            if (boat.getId().equals(boatID)) {
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
        this.startingList.addAll(startingList);
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
        for (Updater updater : updaters) {
            updater.update(this, time);
        }
    }


    public boolean isFinished() {
        return status.equals(RaceStatus.FINISHED);
    }


    public ZonedDateTime getStartTime() {
        return startTime;
    }


    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
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


    public List<PowerUpEvent> popPowerUpEvents() {
        List<PowerUpEvent> events = powerEvents;
        powerEvents = new ArrayList<>();
        return events;
    }


    public void addYachtEvent(YachtEvent yachtEvent) {
        yachtEvents.add(yachtEvent);
    }


    public void addMarkRoundingEvent(MarkRoundingEvent MarkRoundingEvent) {
        markRoundingEvents.add(MarkRoundingEvent);
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


    public RaceMode getMode() {
        return mode;
    }


    public void setMode(RaceMode mode) {
        this.mode = mode;
    }


    public RoundingDetector getDetector() {
        return detector;
    }


    public void setUpdaters(List<Updater> updaters) {
        this.updaters = updaters;
    }


    public MarkRounding getMarkRounding(int sequenceNumber) {
        return course.getMarkRounding(sequenceNumber);
    }


    public List<PickUp> getPickUps() {
        return course.getPickUps();
    }


    /**
     * Consumes a power up.
     *
     * @param boat that picked up the pick up
     * @param pickUp that was picked up
     */
    public void consumePowerUp(Boat boat, PickUp pickUp) {
        boat.setPowerUp(pickUp.getPower());
        List<PickUp> pickUps = course.getPickUps();
        pickUps.remove(pickUp);
        course.setPickUps(pickUps);
        powerEvents.add(new PowerUpEvent(boat.getId(), pickUp));
    }


    public void removeOldPickUps() {
        course.removeOldPickUps();
    }


    public Coordinate getCenter() {
        return course.getCenter();
    }


    public void setPositionSetter(StartPositionSetter positionSetter) {
        this.positionSetter = positionSetter;
    }


    public StartPositionSetter getPositionSetter() {
        return positionSetter;
    }
}