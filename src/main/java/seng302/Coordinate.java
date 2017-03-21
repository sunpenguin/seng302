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

        if (-180.0 <= longitude && longitude <= 180.0) {
            if (-90 <= latitude && latitude <= 90.0) {
                this.latitude = latitude; // TODO check these are valid
                this.longitude = longitude;
            } else {
                System.err.println("Latitude must be between -90 and 90");
            }
        } else {
            System.err.println("Longitude must be between -180 and 180");
        }
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
