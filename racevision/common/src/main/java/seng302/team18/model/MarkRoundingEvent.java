package seng302.team18.model;

/**
 * Created by afj19 on 28/04/17.
 */
public class MarkRoundingEvent {
    private final long time;
    private final Boat boat;
    private final CompoundMark compoundMark;

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
