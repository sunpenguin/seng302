package seng302.team18.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;

/**
 * Created by David-chan on 30/06/17.
 */
public interface IBoat {


    /**
     * A getter for the name of the boat
     *
     * @return The boatName
     */
    String getName();


    /**
     * A getter for the heading of the boat
     *
     * @return The heading of the boat
     */
    double getHeading();


    /**
     * A setter for the heading of the boat
     *
     * @param heading The value the boat heading will be set to
     */
    void setHeading(double heading);


    /**
     * A getter for the team name that the boat belongs to
     *
     * @return The shortName
     */
    String getShortName();


    /**
     * A getter for the speed of the boat
     *
     * @return The speed of the boat in knots
     */
    double getSpeed();

    /**
     * A setter for the speed of the boat
     *
     * @param speed the speed of the boat in knots
     */
    void setSpeed(double speed);


    DoubleProperty speedProperty();


    int getLegNumber();


    IntegerProperty legNumberProperty();


    void setLegNumber(int legNumber);


    Coordinate getCoordinate();


    void setCoordinate(Coordinate coordinate);


    Coordinate getDestination();


    void setDestination(Coordinate destination);


    int getPlace();


    void setPlace(int place);


    IntegerProperty placeProperty();


    Integer getId();


    long getTimeTilNextMark();


    void setTimeTilNextMark(long timeTilNextMark);


    Long getTimeSinceLastMark();


    void setTimeSinceLastMark(long timeSinceLastMark);


    Long getTimeAtLastMark();


    void setTimeAtLastMark(Long timeAtLastMark);


    int getStatus();


    void setStatus(int status);


    boolean isControlled();


    void setControlled(boolean controlled);

}
