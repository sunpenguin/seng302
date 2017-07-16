package seng302.team18.message;

import seng302.team18.model.Coordinate;

/**
 * MessageBody that contains information about a boats location / status.
 */
public class AC35BoatLocationMessage implements MessageBody {
    private Integer sourceId;
    private Coordinate coordinate;
    private Double heading;
    private Double speed;
    private Boolean sailsOut;

    /**
     * Constructor for AC35BoatLocationMessage.
     *
     * @param sourceId   of the boat.
     * @param coordinate of the boat.
     * @param heading    of the boat.
     * @param speed      of the boat in knots.
     */
    public AC35BoatLocationMessage(int sourceId, Coordinate coordinate, double heading, double speed, boolean sailsOut) {
        this.sourceId = sourceId;
        this.coordinate = coordinate;
        this.heading = heading;
        this.speed = speed;
        this.sailsOut = sailsOut;
    }

    @Override
    public int getType() {
        return AC35MessageType.BOAT_LOCATION.getCode();
    }

    /**
     * Getter for boat source id.
     *
     * @return the boats sourceId.
     */
    public Integer getSourceId() {
        return sourceId;
    }

    /**
     * Getter for boats coordinate.
     *
     * @return the boats coordinate.
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Getter for the boats heading.
     *
     * @return the boats heading.
     */
    public Double getHeading() {
        return heading;
    }

    /**
     * Getter for boats speed.
     *
     * @return the boats speed.
     */
    public Double getSpeed() {
        return speed;
    }


    /**
     * Getter for sails out.
     *
     * @return if the boat has sails out.
     */
    public Boolean getSailsOut() {
        return sailsOut;
    }
}
