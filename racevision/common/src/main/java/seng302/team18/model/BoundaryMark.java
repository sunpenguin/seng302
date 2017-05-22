package seng302.team18.model;

/**
 * Created by dhl25 on 11/04/17.
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
