package seng302.team18.model;

/**
 * Represents the occurrence of a yacht event.
 */
public class YachtEvent {

    private final long time;
    private final int boatId;
    private final YachtEventCode eventCode;


    /**
     * Constructor for a YachtEvent
     *
     * @param time      the time the event occurred (in EPOCH milliseconds)
     * @param boatId    the boat that caused the incident
     * @param eventCode the type of incident
     */
    public YachtEvent(long time, int boatId, YachtEventCode eventCode) {
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
}
