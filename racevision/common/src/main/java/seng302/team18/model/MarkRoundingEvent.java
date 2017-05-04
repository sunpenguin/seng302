package seng302.team18.model;

/**
 * Represents a boat "rounding" (passing) a mark (or gate)
 */
public class MarkRoundingEvent {
    private final long time;
    private final Boat boat;
    private final CompoundMark compoundMark;

    /**
     * @param time         the time at which the boat rounded the mark
     * @param boat         the boat
     * @param compoundMark the mark
     */
    public MarkRoundingEvent(long time, Boat boat, CompoundMark compoundMark) {
        this.time = time;
        this.boat = boat;
        this.compoundMark = compoundMark;
    }

    public long getTime() {
        return time;
    }

    public Boat getBoat() {
        return boat;
    }

    public CompoundMark getCompoundMark() {
        return compoundMark;
    }
}
