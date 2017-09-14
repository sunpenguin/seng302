package seng302.team18.model;

import seng302.team18.message.PowerType;
import seng302.team18.model.updaters.Updater;
import seng302.team18.util.GPSCalculator;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
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
    private List<Projectile> projectiles = new ArrayList<>();
    private List<Projectile> newProjectileList = new ArrayList<>();
    private List<Projectile> removedProjectileList = new ArrayList<>();
    private int powerId = 0;
    private int nextProjectileId = 0;
    private StartPositionSetter positionSetter;
//    private Boat spectatorBoat = new Boat("Spectator boat", "Spec boat", 9000, 0);


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
     * @param powerUps  number of power ups to place.
     * @param prototype the base PickUp.
     * @param duration  how long the PickUp will last in milliseconds.
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
     * @param id          of the new PickUp.
     * @param randomPoint location of the new PickUp.
     * @param prototype   the base PickUp.
     * @param duration    how long the PickUp will last in milliseconds.
     * @return the new PickUp.
     */
    private PickUp makePickUp(int id, Coordinate randomPoint, PickUp prototype, double duration) {
        final double timeout = System.currentTimeMillis() + duration;
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
        int max = 1;
        PowerUp powerUp = null;

        int randomNum = ThreadLocalRandom.current().nextInt(0, max + 1);
        if (randomNum == 0) {
            powerUp = new SpeedPowerUp(3);
        } else if (randomNum == 1) {
            powerUp = new SharkPowerUp();
        }

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
     * @param boatID id of the participant to be removed
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
     * @param id     the id of the target boat
     * @param status the status of the boat
     */
    public void setBoatStatus(Integer id, BoatStatus status) {
        for (Boat boat : startingList) {
            if (boat.getId().equals(id)) {
                boat.setStatus(status);
            }
        }
    }


    /**
     * @return all entered boats.
     */
    public List<Boat> getStartingList() {
        return startingList;
    }


    /**
     * Sets the contents of the starting list to be the same as the given list
     *
     * @param startingList the list of starters to use
     */
    public void setStartingList(List<Boat> startingList) {
        this.startingList.clear();
        this.startingList.addAll(startingList);
    }


    /**
     * @return the course for this race
     */
    public Course getCourse() {
        return course;
    }


    /**
     * @param course the course to use for this race
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


    /**
     * Checks if this race has finished
     *
     * @return true if this race has finished, else false
     */
    public boolean isFinished() {
        return status.equals(RaceStatus.FINISHED);
    }


    /**
     * @return the start time of this race
     */
    public ZonedDateTime getStartTime() {
        return startTime;
    }


    /**
     * Sets the starting time of this race
     *
     * @param startTime the time at which to start the race
     */
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


    /**
     * Returns all the enqueued MarkRoundingEvents and clears the list
     *
     * @return the enqueued MarkRoundingEvents
     */
    public List<MarkRoundingEvent> popMarkRoundingEvents() {
        List<MarkRoundingEvent> events = markRoundingEvents;
        markRoundingEvents = new ArrayList<>();
        return events;
    }


    /**
     * Pops all the YachtEvents in the queue
     *
     * @return the popped YachtEvents
     */
    public List<YachtEvent> popYachtEvents() {
        List<YachtEvent> events = yachtEvents;
        yachtEvents = new ArrayList<>();
        return events;
    }


    /**
     * Pops all the PowerUpEvents off the queue
     *
     * @return the popped PowerUpEvents
     */
    public List<PowerUpEvent> popPowerUpEvents() {
        List<PowerUpEvent> events = powerEvents;
        powerEvents = new ArrayList<>();
        return events;
    }

    public List<Projectile> popNewProjectileIds(){
        List<Projectile> events = newProjectileList;
        newProjectileList = new ArrayList<>();
        return events;
    }

    public List<Projectile> popRemovedProjectiles(){
        List<Projectile> events = removedProjectileList;
        removedProjectileList = new ArrayList<>();
        return events;
    }


    /**
     * Adds an event to the queue
     *
     * @param yachtEvent the event to be enqueued
     */
    public void addYachtEvent(YachtEvent yachtEvent) {
        yachtEvents.add(yachtEvent);
    }


    /**
     * Adds an event to the queue
     *
     * @param markRoundingEvent the event to be enqueued
     */
    public void addMarkRoundingEvent(MarkRoundingEvent markRoundingEvent) {
        markRoundingEvents.add(markRoundingEvent);
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
     * @param boat   that picked up the pick up
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


    /**
     * Method to add a projectile to the race
     * Projectiles collected in powerUps
     *
     * @param boat the boat that creates the projectile
     * @param type the type of power up used
     */
    public void addProjectile(Boat boat, PowerType type) {
        Projectile sharky = new TigerShark(nextProjectileId, new Coordinate(0,0), boat.getHeading());
        Coordinate location = gps.toCoordinate(boat.getCoordinate(), boat.getHeading(), (boat.getLength() + sharky.getBodyMass().getRadius() + 5));
        sharky.setLocation(location);
        projectiles.add(sharky);
        nextProjectileId += 1;
        newProjectileList.add(sharky);
//        System.out.println("hi");
//        System.out.println(projectiles);
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    /**
     * Method to remove a projectile from the race
     *
     * @param projectile_id the id of the projectile to be removed
     */
    public void removeProjectile(int projectile_id) {
        for (Iterator<Projectile> it = projectiles.iterator(); it.hasNext();) {
            Projectile projectile = it.next();
            if (projectile.getId() == projectile_id) {
                removedProjectileList.add(projectile);
                it.remove();
            }
        }
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