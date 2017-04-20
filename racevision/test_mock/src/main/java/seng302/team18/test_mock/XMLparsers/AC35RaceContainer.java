package seng302.team18.test_mock.XMLparsers;

import seng302.team18.model.BoundaryMark;
import seng302.team18.model.CompoundMark;
import seng302.team18.model.Mark;
import seng302.team18.model.MarkRounding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hqi19 on 20/04/17.
 */
public class AC35RaceContainer {
    private String startTime;
    private List<BoundaryMark> boundaryMark;
    private Map<Integer, CompoundMark> compoundMarks;
    private List<Integer> participantIDs;
    private List<MarkRounding> markRoundings;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public List<BoundaryMark> getBoundaryMark() {
        return boundaryMark;
    }

    public void setBoundaryMark(List<BoundaryMark> boundaryMark) {
        this.boundaryMark = boundaryMark;
    }

    public Map<Integer, CompoundMark> getCompoundMarks() {
        return compoundMarks;
    }

    public void setCompoundMarks(Map<Integer, CompoundMark> compoundMarks) {
        this.compoundMarks = compoundMarks;
    }

    public List<Integer> getParticipantIDs() {
        return participantIDs;
    }

    public void setParticipantIDs(List<Integer> participantIDs) {
        this.participantIDs = participantIDs;
    }

    public List<MarkRounding> getMarkRoundings() {
        return markRoundings;
    }

    public void setMarkRoundings(List<MarkRounding> markRoundings) {
        this.markRoundings = markRoundings;
    }
}
