package seng302.team18.model;

/**
 *  A class that represents a mark on the race course
 */
public class Mark implements GeographicLocation {

    private String name;
    private Coordinate coordinates;
    private Integer id;
    private Coordinate targetCoordinates;

    public Mark(int id, Coordinate coordinates) {
        this.id = id;
        this.coordinates = coordinates;
        targetCoordinates = coordinates;
    }


    /**
     * Getter for the mark's coordinates
     * @return the coordinates
     */
    public Coordinate getCoordinate() {
        return this.coordinates;
    }


    /**
     * Setter for mark's coordinates
     * @param coordinates the coordinates
     */
    public void setCoordinate(Coordinate coordinates) {
        this.coordinates = coordinates;
    }


    /**
     * Getter for the mark name
     *
     * @return the name of the mark
     */
    public String getName() {
        return name;
    }


    /**
     * A setter for the name of the mark
     *
     * @param markName The name that the mark will  be set to
     */
    public void setName(String markName) {
        this.name = markName;
    }

    public Integer getId() {
        return id;
    }

    public Coordinate getTargetCoordinates() {
        return targetCoordinates;
    }

    public void setTargetCoordinates(Coordinate targetCoordinates) {
        this.targetCoordinates = targetCoordinates;
    }
}

