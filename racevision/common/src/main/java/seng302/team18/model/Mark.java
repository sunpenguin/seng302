package seng302.team18.model;

/**
 * A class that represents a mark on the race course
 */
public class Mark implements GeographicLocation {

    private BoatInfo info;
    private Coordinate coordinates;

    public Mark(int id, Coordinate coordinates) {
        info = new BoatInfo();
        info.setId(id);
        this.coordinates = coordinates;
    }


    /**
     * Getter for the mark's coordinates
     *
     * @return the coordinates
     */
    public Coordinate getCoordinate() {
        return this.coordinates;
    }


    /**
     * Setter for mark's coordinates
     *
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
        return info.getName();
    }


    /**
     * A setter for the name of the mark
     *
     * @param markName The name that the mark will  be set to
     */
    public void setName(String markName) {
        info.setName(markName);
    }

    public Integer getId() {
        return info.getId();
    }

    public BoatInfo getInfo() {
        return info;
    }
}

