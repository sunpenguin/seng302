package seng302.team18.message;

import seng302.team18.model.CompoundMark;
import seng302.team18.model.Coordinate;
import seng302.team18.model.MarkRounding;
import seng302.team18.model.RaceType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MessageBody that contains the race information from the XML file sent by the AC35 streaming protocol.
 */
public class AC35XMLRaceMessage implements XmlMessage {

    private int raceID;
    private RaceType raceType;
    private String startTime;
    private boolean isStartPostponed;
    private Map<Integer, EntryDirection> participants;
    private List<CompoundMark> compoundMarks;
    private List<MarkRounding> markRoundings;
    private List<Coordinate> boundaryMarks;


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


    public Map<Integer, EntryDirection> getParticipants() {
        return Collections.unmodifiableMap(participants);
    }


    public void setParticipants(Map<Integer, EntryDirection> participants) {
        this.participants = participants;
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
    public List<Coordinate> getBoundaryMarks() {
        return boundaryMarks;
    }


    /**
     * Setter for the list of boundary marks.
     *
     * @param boundaryMarks the list of boundary marks.
     */
    public void setBoundaryMarks(List<Coordinate> boundaryMarks) {
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


    public RaceType getRaceType() {
        return raceType;
    }


    public void setRaceType(RaceType raceType) {
        this.raceType = raceType;
    }


    public boolean isStartPostponed() {
        return isStartPostponed;
    }


    public void setStartPostponed(boolean startPostponed) {
        isStartPostponed = startPostponed;
    }


    public enum EntryDirection {
        PORT("Port"),
        STARBOARD("Stbd");

        private final String value;

        private final static Map<String, EntryDirection> MAPPING = initializeMapping();

        EntryDirection(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static EntryDirection fromValue(String value) {
            return MAPPING.get(value);
        }

        private static Map<String, EntryDirection> initializeMapping() {
            return Arrays.stream(values()).collect(Collectors.toMap(EntryDirection::toString, rt -> rt));
        }
    }


}
