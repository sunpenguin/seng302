package seng302.team18.model;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * A class which stores information about a boat.
 */

public class Boat implements GeographicLocation, IBoat {
    private String boatName;
    private String shortName;
    private DoubleProperty speed;
    //Set to -1 initially to prevent null pointer problems
    private IntegerProperty boatLegNumber = new SimpleIntegerProperty(-1);
    private Integer id;
    private double heading;
    private Coordinate coordinate;
    private Coordinate destination;
    private IntegerProperty place;
    private Long timeTilNextMark;
    private Long timeSinceLastMark;
    private Long timeAtLastMark;
    private int status;
    private boolean isControlled;
    private boolean sailOut;
    private boolean autoPilot;
    private boolean tackGybe;
    private boolean upWind;
    private boolean downWind;

    /**
     * A constructor for the Boat class
     *
     * @param boatName  The name of the boat
     * @param shortName The name of the team the boat belongs to
     * @param id        The id of the boat
     */
    public Boat(String boatName, String shortName, int id) {
        this.boatName = boatName;
        this.shortName = shortName;
        this.id = id;
        speed = new SimpleDoubleProperty();
        place = new SimpleIntegerProperty();
        timeTilNextMark = 0L;
        timeSinceLastMark = 0L;
        timeAtLastMark = 0L;
        isControlled = true;
        sailOut = false;
        autoPilot = false;
        tackGybe = false;
        upWind = false;
        downWind = false;
    }

    /**
     * A getter for the name of the boat
     *
     * @return The boatName
     */
    public String getName() {
        return boatName;
    }


    /**
     * A getter for the heading of the boat
     *
     * @return The heading of the boat
     */
    public double getHeading() {
        return heading;
    }


    /**
     * A setter for the heading of the boat
     *
     * @param heading The value the boat heading will be set to
     */
    public void setHeading(double heading) {
        this.heading = heading;
    }


    /**
     * A getter for the team name that the boat belongs to
     *
     * @return The shortName
     */
    public String getShortName() {
        return shortName;
    }


    /**
     * A getter for the speed of the boat
     *
     * @return The speed of the boat
     */
    public double getSpeed() {
        return speed.get();
    }

    /**
     * A setter for the speed of the boat
     *
     * @param speed the speed of the boat
     */
    public void setSpeed(double speed) {
        this.speed.setValue(speed);
    }


    public DoubleProperty speedProperty() {
        return speed;
    }


    public int getLegNumber() {
        return boatLegNumber.get();
    }


    public IntegerProperty legNumberProperty() {
        return boatLegNumber;
    }


    public void setLegNumber(int boatLegNumber) {
        this.boatLegNumber.set(boatLegNumber);
    }


    public Coordinate getCoordinate() {
        return coordinate;
    }


    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }


    public Coordinate getDestination() {
        return destination;
    }


    public void setDestination(Coordinate destination) {
        this.destination = destination;
    }


    public int getPlace() {
        return place.get();
    }


    public void setPlace(int place) {
        this.place.set(place);
    }


    public IntegerProperty placeProperty() {
        return place;
    }


    public Integer getId() {
        return id;
    }


    public long getTimeTilNextMark() {
        return timeTilNextMark;
    }


    public void setTimeTilNextMark(long timeTilNextMark) {
        this.timeTilNextMark = timeTilNextMark;
    }


    public Long getTimeSinceLastMark() {
        return timeSinceLastMark;
    }


    public void setTimeSinceLastMark(long timeSinceLastMark) {
        this.timeSinceLastMark = timeSinceLastMark;
    }


    public Long getTimeAtLastMark() {
        return timeAtLastMark;
    }


    public void setTimeAtLastMark(Long timeAtLastMark) {
        this.timeAtLastMark = timeAtLastMark;
    }

    public boolean isSailOut() {
        return sailOut;
    }

    public void setSailOut(boolean sailOut) {
        this.sailOut = sailOut;
    }

    @Override
    public String toString() {
        return "Boat{" +
                "boatName=" + boatName +
                ", shortName='" + shortName + '\'' +
                ", speed=" + speed +
                ", leg=" + boatLegNumber +
                ", id=" + id +
                ", heading=" + heading +
                ", coordinate=" + coordinate +
                ", destination=" + destination +
                ", place=" + place +
                ", timeTilNextMark=" + timeTilNextMark +
                ", timeSinceLastMark=" + timeSinceLastMark +
                ", timeAtLastMark=" + timeAtLastMark +
                '}';
    }

    public int getStatus() {
        return status;
    }


    public void setStatus(int status) {
        this.status = status;
    }


    public boolean isControlled() {
        return isControlled;
    }


    public void setControlled(boolean controlled) {
        isControlled = controlled;
    }

    public void setAutoPilot(boolean autoPilot) {
        this.autoPilot = autoPilot;
    }

    public boolean isAutoPilot() {return autoPilot; }

    public void setTackGybe(boolean tackGybe) {
        this.tackGybe = tackGybe;
    }

    public boolean isTackGybe() {return tackGybe; }

    public void setUpWind(boolean upWind) {
        this.upWind = upWind;
    }

    public boolean isUpWind() {return upWind; }

    public void setDownWind(boolean downWind) {
        this.downWind = downWind;
    }

    public boolean isDownWind() {return downWind; }
}
