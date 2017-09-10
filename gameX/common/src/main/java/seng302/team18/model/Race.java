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
    private RaceStatus status;
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
    private List<Projectile> projectiles= new ArrayList<>();
    private int powerId = 0;
    private int nextProjectileId = 300;
    private final int PROJECTILE_SPEED = 50;
    private final int PROJECTILE_RADIUS = 15;
    private final int PROJECTILE_WEIGHT= 5;


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
            boat.setCoordinate(getStartPosition(boat, boat.getLength() * 3));
            boat.setHeading(gps.getBearing(
                    boat.getCoordinate(),
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
     * @param boat       boat to get starting position for
     * @param distBehind the distance behind the start line to place the boat (m)
     * @return position for boat to start at
     */
    public Coordinate getStartPosition(Boat boat, double distBehind) {
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
        if (participantIds.size() == 0) {
            this.startingList.addAll(startingList);
        } else {
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


    public void setPickUps(List<PickUp> pickUps) {
        course.setPickUps(pickUps);
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


    /**
     * Method to add a projectile to the race
     * Projectiles collected in powerUps
     *
     * @param boat the boat that creates the projectile
     * @param type the type of power up used
     */
    public void addProjectile(Boat boat, PowerType type) {
        Coordinate location = gps.toCoordinate(boat.getCoordinate(), boat.getHeading(), (boat.getLength() + PROJECTILE_RADIUS + 5));
        Projectile sharky = new TigerShark(nextProjectileId, PROJECTILE_RADIUS, PROJECTILE_WEIGHT, location, boat.getHeading(), PROJECTILE_SPEED);
        projectiles.add(sharky);
        nextProjectileId += 1;
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
                it.remove();
            }
        }
    }
}