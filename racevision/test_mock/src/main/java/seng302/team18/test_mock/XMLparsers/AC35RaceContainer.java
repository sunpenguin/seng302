package seng302.team18.test_mock.XMLparsers;

import seng302.team18.model.BoundaryMark;
import seng302.team18.model.CompoundMark;
import seng302.team18.model.Mark;
import seng302.team18.model.MarkRounding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class that contains all information for a race.
 */
public class AC35RaceContainer {
    private String startTime;
    private List<BoundaryMark> boundaryMark;
    private Map<Integer, CompoundMark> compoundMarks;
    private List<Integer> participantIDs;
    private List<MarkRounding> markRoundings;

    /*
    Get start time for a race.
     */
    public String getStartTime() {
        return startTime;
    }

    /*
    Set the start time of a race.
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /*
    Get a list of boundary marks.
     */
    public List<BoundaryMark> getBoundaryMark() {
        return boundaryMark;
    }

    /*
    Set the value of a list of boundary marks.
     */
    public void setBoundaryMark(List<BoundaryMark> boundaryMark) {
        this.boundaryMark = boundaryMark;
    }

    /*
    Get a map of compound marks with its ID as key.
     */
    public Map<Integer, CompoundMark> getCompoundMarks() {
        return compoundMarks;
    }

    /*
    Set up a map compound marks with its ID as key.
     */
    public void setCompoundMarks(Map<Integer, CompoundMark> compoundMarks) {
        this.compoundMarks = compoundMarks;
    }

    /*
    Get a list of IDs for the participanting yachts.
     */
    public List<Integer> getParticipantIDs() {
        return participantIDs;
    }

    /*
    Set up a list of IDs for the participanting yachts.
     */
    public void setParticipantIDs(List<Integer> participantIDs) {
        this.participantIDs = participantIDs;
    }

    /*
    Get a list of mark roundings.
     */
    public List<MarkRounding> getMarkRoundings() {
        return markRoundings;
    }

    /*
    Set up a list of mark roundings.
     */
    public void setMarkRoundings(List<MarkRounding> markRoundings) {
        this.markRoundings = markRoundings;
    }
}