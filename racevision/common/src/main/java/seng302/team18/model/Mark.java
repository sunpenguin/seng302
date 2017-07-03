package seng302.team18.model;

/**
 * A class that represents a mark on the race course
 */
public class Mark extends Boat implements GeographicLocation {

    private Coordinate coordinates;

    public Mark(int id, Coordinate coordinates) {
        super(id);
        this.coordinates = coordinates;
    }

    @Override
    public BoatType getType() {
        return BoatType.MARK;
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
}

