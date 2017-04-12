package seng302.team18.model;

/**
 *  A class that represents a mark on the race course
 */
public class Mark {

    private String name;
    private Coordinate coordinates;
    private Integer id;

    public Mark(String name, Coordinate coordinates) {
        this.name = name;
        this.coordinates = coordinates;
    }

    public Mark(int id, Coordinate coordinates) {
        this.id = id;
        this.coordinates = coordinates;
    }


    /**
     * Getter for the mark's coordinates
     * @return the coordinates
     */
    public Coordinate getCoordinates() {
        return this.coordinates;
    }


    /**
     * Setter for mark's coordinates
     * @param coordinates the coordinates
     */
    public void setCoordinates(Coordinate coordinates) {
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
}

