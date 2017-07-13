package seng302.team18.model;

/**
 * Stores the direction taken to round a mark.
 */
public class MarkRounding {
    private int sequenceNumber;
    private CompoundMark compoundMark;

    public MarkRounding(int sequenceNumber, CompoundMark compoundMark) {
        this.sequenceNumber = sequenceNumber;
        this.compoundMark = compoundMark;
    }

    public CompoundMark getCompoundMark() {
        return compoundMark;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }
}
