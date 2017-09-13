package seng302.team18.visualiser;

import seng302.team18.model.*;
import seng302.team18.util.GPSCalculator;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to hold race data for visualiser
 */
public class ClientRace {

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
    private RaceMode mode = RaceMode.RACE;
//    private Boat spectatorBoat = new Boat("Spectator boat", "Spec boat", 9000, 0);


    public ClientRace() {
        participantIds = new ArrayList<>();
        startingList = new ArrayList<>();
        course = new Course();
        id = 0;
        status = RaceStatus.NOT_ACTIVE;
        currentTime = ZonedDateTime.now(course.getTimeZone());
        startTime = ZonedDateTime.ofInstant(Instant.EPOCH, course.getTimeZone());
        raceType = RaceType.MATCH;
        GPSCalculator gps = new GPSCalculator();
//        spectatorBoat.setCoordinate(gps.getCentralCoordinate(course.getCourseLimits()));
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


    public void addParticipant(Boat boat) {
        // check that it is alright to add a boat at this point
        startingList.add(boat);
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


    public int getPlayerId() {
        return playerId;
    }


    public void setPlayerId(int playerId) {
        this.playerId = playerId;

        startingList.forEach(boat -> boat.setControlled(boat.getId().equals(playerId)));
    }


    public RaceMode getMode() {
        return mode;
    }


    public void setMode(RaceMode mode) {
        this.mode = mode;
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


    /**
     * Course getter.
     *
     * @return Course object
     */
    public Course getCourse() {
        return course;
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


    public void setCourse(Course course) {
        this.course = course;
    }


    public List<Integer> getParticipantIds() {
        return participantIds;
    }


    public void setParticipantIds(List<Integer> participantIds) {
        this.participantIds = participantIds;
    }


    public List<Boat> getStartingList() {
        return startingList;
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


    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
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

        final int newPlace = (int) startingList.stream().filter(b -> b.getLegNumber() >= nextLeg).count() + 1;
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
        boat.setLegNumber(nextLeg);
    }


    public void removePickUp(int id) {
        course.removePickUp(id);
    }


    public List<PickUp> getPickUps() {
        return course.getPickUps();
    }


    public PickUp getPickUp(int id) {
        return course.getPickUp(id);
    }


    /**
     * Get the PowerUp associated to the PickUp.
     *
     * @param id of the PickUp
     * @return PowerUp that the PickUp had.
     */
    public PowerUp getPowerUp(int id) {
        return getPickUp(id).getPower();
    }


    public Coordinate getDestination(int legNumber) {
        return course.getDestination(legNumber);
    }


    public int numSequences() {
        return course.getMarkSequence().size();
    }


    public Coordinate getCenter() {
        return course.getCenter();
    }


    public void activatePowerUp() {
        Boat boat = getBoat(playerId);
        if (boat.canActivatePower()) {
            boat.activatePowerUp();
        }
    }
}
