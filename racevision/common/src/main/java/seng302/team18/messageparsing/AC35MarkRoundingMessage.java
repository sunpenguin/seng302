package seng302.team18.messageparsing;

/**
 * Created by dhl25 on 25/04/17.
 */
public class AC35MarkRoundingMessage implements MessageBody {
    private int boatId;
    private long time;

    public AC35MarkRoundingMessage(int boatId, long time) {
        this.boatId = boatId;
        this.time = time;
    }

    @Override
    public int getType() {
        return AC35MessageType.MARK_ROUNDING.getCode();
    }

    public int getBoatId() {
        return boatId;
    }

    public long getTime() {
        return time;
    }
}
