package seng302.team18.model;

/**
 * Created by david on 4/12/17.
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

    public int getSequenceNumber() {
        return sequenceNumber;
    }
}
