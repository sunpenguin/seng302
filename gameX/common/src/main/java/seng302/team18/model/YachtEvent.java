package seng302.team18.model;

/**
 * Represents the occurrence of a yacht event.
 */
public class YachtEvent {

    private final long time;
    private final Boat boat;
    private final YachtEventCode eventCode;


    /**
     * Constructor for a YachtEvent
     *
     * @param time      the time the event occurred (in EPOCH milliseconds)
     * @param boat      the boat that caused the incident
     * @param eventCode the type of incident
     */
    public YachtEvent(long time, Boat boat, YachtEventCode eventCode) {
        this.time = time;
        this.boat = boat;
        this.eventCode = eventCode;
    }


    public long getTime() {
        return time;
    }


    public Boat getBoat() {
        return boat;
    }


    public YachtEventCode getEventCode() {
        return eventCode;
    }
}
