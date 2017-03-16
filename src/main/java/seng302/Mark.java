package seng302;

import java.util.ArrayList;

/**
 *  A class that represents a mark on the race course
 */
public class Mark {

    private String markName;
    private double latitude;
    private double longitude;


    public Mark(String markName, double latitude, double longitude) {
        this.markName = markName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Getter for the mark name
     *
     * @return the name of the mark
     */
    String getMarkName() {
        return markName;
    }


    /**
     * A setter for the name of the mark
     *
     * @param markName The name that the mark will  be set to
     */
    public void setMarkName(String markName) {
        this.markName = markName;
    }


}

