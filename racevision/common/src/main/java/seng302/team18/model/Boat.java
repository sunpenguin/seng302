package seng302.team18.model;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import seng302.team18.util.GPSCalculations;

import java.util.List;

/**
 * A class which stores information about a boat.
 */

public class Boat extends AbstractBoat implements GeographicLocation, IBoat {
    private PolarPattern boatPolar = new AC35PolarPattern();
    private DoubleProperty speed;
    //Set to -1 initially to prevent null pointer problems
    private IntegerProperty boatLegNumber = new SimpleIntegerProperty(-1);
    private Integer id;
    private double boatLength;
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
    public Boat(String boatName, String shortName, int id, double boatLength) {
        super(id, boatName, shortName);
        this.boatLength = boatLength;
        speed = new SimpleDoubleProperty();
        place = new SimpleIntegerProperty(1);
        timeTilNextMark = 0L;
        timeSinceLastMark = 0L;
        timeAtLastMark = 0L;
        isControlled = true;
        sailOut = true; // Starts with luffing
        autoPilot = false;
        tackGybe = false;
        upWind = false;
        downWind = false;
    }


    @Override
    public BoatType getType() {
        return BoatType.YACHT;
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

    @Override
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
                "boatName=" + getName() +
                ", shortName='" + getShortName() + '\'' +
                ", speed=" + speed +
                ", leg=" + boatLegNumber +
                ", id=" + getId() +
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

    public double getLength() {
        return boatLength;
    }


    /**
     * Uses boats polar pattern to calculate boats TWS
     *
     * @param windSpeed double, the speed of the wind
     * @param windHeading double, the direction of the wind
     * @return double, the tws of the boat
     */
    public double getBoatTWS(double windSpeed, double windHeading) {
        double twa = getTrueWindAngle(windHeading);
        System.out.println(twa);
        return boatPolar.getSpeedForBoat(twa, windSpeed);
    }

    /**
     * Gets true wind angle for boat.
     * NOTE: Expects wind heading as
     *
     * @param windHeading double, Heading of the wind
     * @return double, True wind angle for the boat.
     */
    public double getTrueWindAngle(double windHeading) {

        double flippedWindDirection = (windHeading + 180) % 360; // flipping wind direction
        double theta = 180 - flippedWindDirection;
        double boatPlusTheta = heading + theta;
        double windPlusTheta = windHeading + theta; //will be 180

        if (boatPlusTheta > 360) {
            boatPlusTheta = boatPlusTheta - 360;
        }

        if (boatPlusTheta < 0) {
            boatPlusTheta = 360 + boatPlusTheta;
        }

        double trueWindAngle;

        if (boatPlusTheta > 180) {
            double angleFrom180 = boatPlusTheta - 180;
            trueWindAngle = 180 - angleFrom180;
        } else {
            trueWindAngle = boatPlusTheta;
        }

        return trueWindAngle;
    }

    /**
     * Method to check if boat has collided with another boat
     * TODO: jth102, sbe67 25/07, handle collisions with more than one obstacle
     *
     * @param obstacles  list of Abstract Boats to check if boat has collied
     * @return the obstacle the boat has collided, null is not collision
     */
    public AbstractBoat hasCollided(List<AbstractBoat> obstacles){
        AbstractBoat collidedWith = null;
        GPSCalculations calculator = new GPSCalculations();
        double collisionZone;
        double distanceBetween;
        for(AbstractBoat obstacle : obstacles) {
            if (!obstacle.equals(this)) {
                collisionZone = (obstacle.getLength()) + (boatLength);
                distanceBetween = calculator.distance(coordinate, (obstacle).getCoordinate());

                if (distanceBetween < collisionZone) {
                    collidedWith = obstacle;
                }
            }
        }
        return collidedWith;
    }
}
