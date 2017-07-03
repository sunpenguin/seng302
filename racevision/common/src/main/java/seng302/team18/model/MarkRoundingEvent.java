package seng302.team18.model;

/**
 * Represents a yacht "rounding" (passing) a mark (or gate)
 */
public class MarkRoundingEvent {

    private final long time;
    private final Yacht yacht;
    private final CompoundMark compoundMark;

    /**
     * @param time         the time at which the yacht rounded the mark
     * @param yacht         the yacht
     * @param compoundMark the mark
     */
    public MarkRoundingEvent(long time, Yacht yacht, CompoundMark compoundMark) {
        this.time = time;
        this.yacht = yacht;
        this.compoundMark = compoundMark;
    }

    public long getTime() {
        return time;
    }

    public Yacht getYacht() {
        return yacht;
    }

    public CompoundMark getCompoundMark() {
        return compoundMark;
    }
}
