package seng302.team18.message;

import seng302.team18.model.YachtEventCode;

/**
 * MessageBody that contains information about a yacht event.
 */
public class AC35YachtEventMessage implements MessageBody {

    private final long time;
    private final int boatId;
    private final YachtEventCode eventCode;


    /**
     * Constructor for an AC35YachtEventMessage.
     *
     * @param time      the time the event occurred (in EPOCH milliseconds)
     * @param boatId    the boat that caused the incident
     * @param eventCode the type of incident
     */
    public AC35YachtEventMessage(long time, int boatId, YachtEventCode eventCode) {
        this.time = time;
        this.boatId = boatId;
        this.eventCode = eventCode;
    }


    public long getTime() {
        return time;
    }


    public int getBoatId() {
        return boatId;
    }


    public YachtEventCode getEventCode() {
        return eventCode;
    }

    @Override
    public int getType() {
        return AC35MessageType.YACHT_EVENT.getCode();
    }
}
