package seng302.team18.model;

/**
 * Represents a boat "rounding" (passing) a mark (or gate)
 */
public class MarkRoundingEvent {

    private final long time;
    private final Boat boat;
    private final MarkRounding markRounding;

    /**
     * @param time         the time at which the boat rounded the mark (epoch milliseconds)
     * @param boat         the boat
     * @param markRounding the rounding completed
     */
    public MarkRoundingEvent(long time, Boat boat, MarkRounding markRounding) {
        this.time = time;
        this.boat = boat;
        this.markRounding = markRounding;
    }


    public long getTime() {
        return time;
    }


    public Boat getBoat() {
        return boat;
    }


    public MarkRounding getMarkRounding() {
        return markRounding;
    }
}
