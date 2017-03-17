package seng302;

import java.util.ArrayList;

/**
 *  A class that represents a mark on the race course
 */
public class Mark {

    private String markName;
    private Coordinate markCoordinates;


    public Mark(String markName, Coordinate coordinates) {
        this.markName = markName;
        this.markCoordinates = coordinates;
    }


    /**
     * Getter for the mark's coordinates
     * @return the coordinates
     */
    public Coordinate getMarkCoordinates() {
        return this.markCoordinates;
    }


    /**
     * Setter for mark's coordinates
     * @param coordinates the coordinates
     */
    public void setMarkCoordinates(Coordinate coordinates) {
        this.markCoordinates = coordinates;
    }


    /**
     * Getter for the mark name
     *
     * @return the name of the mark
     */
    public String getMarkName() {
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

