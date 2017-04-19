package seng302.team18.data;

import seng302.team18.model.Coordinate;

/**
 * Created by dhl25 on 10/04/17.
 */
public class AC35BoatLocationMessage implements MessageBody {
    private Integer sourceId;
    private Coordinate coordinate;
    private Double heading;
    private Double speed;

    public AC35BoatLocationMessage(int sourceId, Coordinate coordinate, double heading, double speed) {
        this.sourceId = sourceId;
        this.coordinate = coordinate;
        this.heading = heading;
        this.speed = speed;
    }

    @Override
    public int getType() {
        return AC35MessageType.BOAT_LOCATION.getCode();
    }

    public Integer getSourceId() {
        return sourceId;
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
