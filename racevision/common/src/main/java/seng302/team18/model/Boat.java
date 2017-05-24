package seng302.team18.model;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * A class which stores information about a boat.
 */

public class Boat implements GeographicLocation {
    private String boatName;
    private String shortName;
    private DoubleProperty speed;
    private DoubleProperty knotsSpeed;
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
        knotsSpeed = new SimpleDoubleProperty();
        place = new SimpleIntegerProperty();
        timeTilNextMark = 0L;
        timeSinceLastMark = 0L;
        timeAtLastMark = 0L;
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

    public int getLegNumber() {
        return boatLegNumber.get();
    }

    public IntegerProperty boatLegNumberProperty() {
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

    public DoubleProperty knotsSpeedProperty() {
        return knotsSpeed;
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

    public double getKnotsSpeed() {
        return knotsSpeed.get();
    }

    public void setKnotsSpeed(double knotsSpeed) {
        this.knotsSpeed.set(knotsSpeed);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
