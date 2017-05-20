package seng302.team18.message;

/**
 * MessageBody that contains information about a mark rounding.
 */
public class AC35MarkRoundingMessage implements MessageBody {
    private int boatId;
    private long time;

    /**
     * Constructor for AC35MarkRoundingMessage
     * @param boatId
     * @param time
     */
    public AC35MarkRoundingMessage(int boatId, long time) {
        this.boatId = boatId;
        this.time = time;
    }

    @Override
    public int getType() {
        return AC35MessageType.MARK_ROUNDING.getCode();
    }

    /**
     * Getter for the boats id.
     * @return the boats id.
     */
    public int getBoatId() {
        return boatId;
    }

    /**
     * Getter for the time at which the mark was past.
     * @return the time at which the mark was past.
     */
    public long getTime() {
        return time;
    }
}
