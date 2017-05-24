package seng302.team18.model;

/**
 * A class which stores information about a boundary mark.
 */
public class BoundaryMark implements GeographicLocation {

    private Integer sequenceID;
    private Coordinate coordinate;

    public BoundaryMark(int sequenceID, Coordinate coordinate) {
        this.sequenceID = sequenceID;
        this.coordinate = coordinate;
    }

    public Integer getSequenceID() {
        return sequenceID;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}
