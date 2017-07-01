package seng302.team18.message;

import seng302.team18.model.BoundaryMark;
import seng302.team18.model.CompoundMark;
import seng302.team18.model.MarkRounding;

import java.util.List;

/**
 * MessageBody that contains the race information from the XML file sent by the AC35 streaming protocol.
 */
public class AC35XMLRaceMessage implements MessageBody, XmlMessage {

    private String startTime;
    private List<Integer> participantIDs;
    private List<CompoundMark> compoundMarks;
    private List<MarkRounding> markRoundings;
    private List<BoundaryMark> boundaryMarks;
    private int raceID;

    @Override
    public int getType() {
        return AC35MessageType.XML_RACE.getCode();
    }

    /**
     * Getter for the start time of the race.
     *
     * @return the start time of the race in Epoch milliseconds.
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Setter for the start time of the race.
     *
     * @param startTime the start time of the race in Epoch milliseconds.
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Getter for the list of participant IDs.
     *
     * @return the list of participant IDs.
     */
    public List<Integer> getParticipantIDs() {
        return participantIDs;
    }

    /**
     * Setter for the list of participant IDs.
     *
     * @param participantIDs the list of participant IDs.
     */
    public void setParticipantIDs(List<Integer> participantIDs) {
        this.participantIDs = participantIDs;
    }

    /**
     * Getter for the list of compound marks (gates and marks).
     *
     * @return the list of compound marks.
     */
    public List<CompoundMark> getCompoundMarks() {
        return compoundMarks;
    }

    /**
     * Setter for the list of compound marks (gates and marks).
     *
     * @param compoundMarks the list of compound marks.
     */
    public void setCompoundMarks(List<CompoundMark> compoundMarks) {
        this.compoundMarks = compoundMarks;
    }

    /**
     * Getter for the list of mark roundings.
     *
     * @return the list of mark roundings.
     */
    public List<MarkRounding> getMarkRoundings() {
        return markRoundings;
    }

    /**
     * Setter for the list of mark roundings.
     *
     * @param markRoundings the list of mark roundings.
     */
    public void setMarkRoundings(List<MarkRounding> markRoundings) {
        this.markRoundings = markRoundings;
    }

    /**
     * Getter for the list of boundary marks.
     *
     * @return the list of boundary marks.
     */
    public List<BoundaryMark> getBoundaryMarks() {
        return boundaryMarks;
    }

    /**
     * Setter for the list of boundary marks.
     *
     * @param boundaryMarks the list of boundary marks.
     */
    public void setBoundaryMarks(List<BoundaryMark> boundaryMarks) {
        this.boundaryMarks = boundaryMarks;
    }

    /**
     * Getter for the race ID.
     *
     * @return the race ID.
     */
    public int getRaceID() {
        return raceID;
    }

    /**
     * Setter for the race ID.
     *
     * @param raceID the race ID.
     */
    public void setRaceID(int raceID) {
        this.raceID = raceID;
    }
}
