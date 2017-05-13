package seng302.team18.visualiser.util;

import seng302.team18.model.Boat;

/**
 * Class to hold a data of a boats position as it statrs a leg
 */
public class SparklineDataPoint {

    private String boatName;
    private int boatPlace;
    private int leg;

    /**
     * Constructor, takes a boat and creates a sparkline data point that holds
     * the boats name, place and leg number.
     * @param boat
     */
    public SparklineDataPoint(Boat boat){
        boatName = boat.getBoatName();
        boatPlace = boat.getPlace();
        leg = boat.getBoatLegNumber();
    }

    public String getBoatName() {
        return boatName;
    }

    public int getBoatPlace() {
        return boatPlace;
    }

    public int getLeg() {
        return leg;
    }
}
