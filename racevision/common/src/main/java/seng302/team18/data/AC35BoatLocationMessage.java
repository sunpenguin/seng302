package seng302.team18.data;

import seng302.team18.model.Coordinate;

/**
 * Created by dhl25 on 10/04/17.
 */
public class AC35BoatLocationMessage implements MessageBody {
    private Integer sourceID;
    private Coordinate coordinate;
    private Double heading;
    private Double speed;

    public AC35BoatLocationMessage(int sourceID, Coordinate coordinate, double heading, double speed) {
        this.sourceID = sourceID;
        this.coordinate = coordinate;
        this.heading = heading;
        this.speed = speed;
    }

    @Override
    public AC35MessageType getType() {
        return AC35MessageType.BOAT_LOCATION;
    }

    public Integer getSourceID() {
        return sourceID;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Double getHeading() {
        return heading;
    }

    public Double getSpeed() {
        return speed;
    }
}
