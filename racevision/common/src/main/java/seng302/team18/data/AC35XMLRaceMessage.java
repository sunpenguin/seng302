package seng302.team18.data;

import seng302.team18.model.BoundaryMark;
import seng302.team18.model.CompoundMark;
import seng302.team18.model.MarkRounding;

import java.util.List;

/**
 * Created by dhl25 on 11/04/17.
 */
public class AC35XMLRaceMessage implements MessageBody {

    private String raceStartTime;
    private List<Integer> participantIDs;
    private List<CompoundMark> compoundMarks;
    private List<MarkRounding> markRoundings;
    private List<BoundaryMark> boundaryMarks;

    @Override
    public int getType() {
        return AC35MessageType.XML_RACE.getCode();
    }

    public String getRaceStartTime() {
        return raceStartTime;
    }

    public void setRaceStartTime(String raceStartTime) {
        this.raceStartTime = raceStartTime;
    }

    public List<Integer> getParticipantIDs() {
        return participantIDs;
    }

    public void setParticipantIDs(List<Integer> participantIDs) {
        this.participantIDs = participantIDs;
    }

    public List<CompoundMark> getCompoundMarks() {
        return compoundMarks;
    }

    public void setCompoundMarks(List<CompoundMark> compoundMarks) {
        this.compoundMarks = compoundMarks;
    }

    public List<MarkRounding> getMarkRoundings() {
        return markRoundings;
    }

    public void setMarkRoundings(List<MarkRounding> markRoundings) {
        this.markRoundings = markRoundings;
    }

    public List<BoundaryMark> getBoundaryMarks() {
        return boundaryMarks;
    }

    public void setBoundaryMarks(List<BoundaryMark> boundaryMarks) {
        this.boundaryMarks = boundaryMarks;
    }
}
