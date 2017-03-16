package seng302;

import java.text.DecimalFormat;

public class Coordinate {

    private double latitude;
    private double longitude;


    /**
     * Coordinate class constructor
     * @param latitude value representing latitude
     * @param longitude value representing longitude
     */
    public Coordinate(double latitude, double longitude) {
        this.latitude = latitude; // TODO check these are valid
        this.longitude = longitude;
    }


    /**
     * latitude getter
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }


    /**
     * latitude setter
     * @param latitude value for the latitude
     */
    public void setLatitude(double latitude) { // TODO check validity
        this.latitude = latitude;
    }


    /**
     * longitude getter
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }


    /**
     * longitude setter
     * @param longitude value for the longitude
     */
    public void setLongitude(double longitude) { // TODO check validity
        this.longitude = longitude;
    }
}
