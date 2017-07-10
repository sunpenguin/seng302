package seng302.team18.test_mock.model;

import seng302.team18.message.AC35XMLRaceMessage;
import seng302.team18.model.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to generate a new race.xml model for AC35 XML RaceMessage.
 */
public class RaceModel {
    private String startTime;
    private List<Integer> participantIDs = new ArrayList<>();
    private List<CompoundMark> compoundMarks = new ArrayList<>();
    private List<MarkRounding> markRoundings = new ArrayList<>();
    private List<BoundaryMark> boundaryMarks = new ArrayList<>();
    private int raceID;
    private AC35XMLRaceMessage raceMessage = new AC35XMLRaceMessage();


//    /**
//     * Method that create a AC35 XML RaceMessage.
//     * @return a AC35XMLRaceMessage
//     */
//    public AC35XMLRaceMessage getRaceMessage() {
//        final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss-Z");
//        startTime = ZonedDateTime.now().format(DATE_TIME_FORMATTER);
//
//        for (int i = 121; i < 125; i ++) {
//            participantIDs.add(i);
//        }
//
//        compoundMarks = generateCompoundMarks();
//        markRoundings = generateMarkRoundings();
//        boundaryMarks = generateBoundaryMarks();
//
//        raceID = 11080703;
//
//        raceMessage.setRaceID(raceID);
//        raceMessage.setBoundaryMarks(boundaryMarks);
//        raceMessage.setCompoundMarks(compoundMarks);
//        raceMessage.setMarkRoundings(markRoundings);
//        raceMessage.setParticipantIDs(participantIDs);
//        raceMessage.setStartTime(startTime);
//
//        return raceMessage;
//    }



}
