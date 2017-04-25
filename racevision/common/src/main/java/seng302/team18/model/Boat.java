package seng302.team18.model;


import javafx.beans.property.*;

/**
 * A class which represents a boat in the text based Application
 */

public class Boat {
    private StringProperty boatName;
    private String shortName;
    private DoubleProperty speed;
    private Leg leg;
    private Integer id;
    private double heading;
    private Coordinate coordinate;
    private Coordinate destination;
    private IntegerProperty place;
    private SimpleLongProperty timeTilNextMark;
    private Long timeSinceLastMark;
    private Long timeAtLastMark;

    /**
     * A constructor for the Boat class
     * @param boatName The name of the boat
     * @param shortName The name of the team the boat belongs to
     * @param id The id of the boat
     */
    public Boat(String boatName, String shortName, int id) {
        this.boatName = new SimpleStringProperty(boatName);
        this.shortName = shortName;
        this.id = id;
        speed = new SimpleDoubleProperty();
        place = new SimpleIntegerProperty();
        timeTilNextMark = new SimpleLongProperty();
        timeSinceLastMark = 0L;
        timeAtLastMark = 0L;
    }

    /**
     * A getter for the name of the boat
     * @return The boatName
     */
    public String getBoatName() {
        return boatName.get();
    }

    /**
     * A setter for the name of the boat
     * @param name The name the boatName will be set to
     */
    public void setBoatName(String name) {
        this.boatName.set(name);
    }


    public StringProperty boatNameProperty() {
        return boatName;
    }


    /**
     * A getter for the heading of the boat
     * @return The heading of the boat
     */
    public double getHeading() {
        return heading;
    }


    /**
     * A setter for the heading of the boat
     * @param heading The value the boat heading will be set to
     */
    public void setHeading(double heading) {
        this.heading = heading;
    }


    /**
     * A getter for the team name that the boat belongs to
     * @return The shortName
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * A setter for the team name that the boat belongs to
     * @param name The name that the teamName variable will be set to
     */
    public void setShortName(String name) {
        this.shortName = name;
    }

    /**
     * A getter for the speed of the boat
     * @return The speed of the boat
     */
    public double getSpeed() {
        return speed.get();
    }

    /**
     * A setter for the speed of the boat
     * @param speed the speed of the boat
     */
    public void setSpeed(double speed) {
        this.speed.setValue(speed);
    }

    public DoubleProperty speedProperty() {
        return speed;
    }

    public Leg getLeg() {
        return leg;
    }


    public void setLeg(Leg leg) {
        this.leg = leg;
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

    /**
     * An overidden toString for the boat objects which displays all of the boats variables
     * @return A string showing all the boats variables
     */
    @Override
    public String toString() {
        return "Boat{" +
                "boatName='" + boatName + '\'' +
                ", teamName='" + shortName + '\'' +
                '}';
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

    public void setId(Integer id) {
        this.id = id;
    }

    public long getTimeTilNextMark() {
        return timeTilNextMark.get();
    }

    public SimpleLongProperty timeTilNextMarkProperty() {
        return timeTilNextMark;
    }

    public void setTimeTilNextMark(long timeTilNextMark) {
        this.timeTilNextMark.set(timeTilNextMark);
    }

    public Long getTimeSinceLastMark() {
        return timeSinceLastMark;
    }

    public Long getTimeAtLastMark() {
        return timeAtLastMark;
    }

    public void setTimeAtLastMark(Long timeAtLastMark) {
        this.timeAtLastMark = timeAtLastMark;
    }
}
