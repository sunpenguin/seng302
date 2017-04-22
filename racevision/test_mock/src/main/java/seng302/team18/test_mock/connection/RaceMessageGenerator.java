package seng302.team18.test_mock.connection;

import seng302.team18.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hqi19 on 21/04/17.
 */
public class RaceMessageGenerator {

    private Race race;
    private String message;
//    private AC35XMLRaceMessage message = new AC35XMLRaceMessage();

    public RaceMessageGenerator(Race race) {
        this.race = race;
    }

    public String getRaceMessage() {
        String startTime = race.getStartTime().toString();
        String currentTime = race.getCurrentTime().toString();

//        List<Boat> boats = race.getStartingList();
//
//        List<BoundaryMark> boundaries = race.getCourse().getBoundaries();
//        List<CompoundMark> compoundMarks = race.getCourse().getCompoundMarks();
//        List<MarkRounding> markRoundings = race.getCourse().getMarkRoundings();
//        List<Integer> participantIDs = new ArrayList<>();
//        for (Boat b: boats) {
//            participantIDs.add(b.getId());
//        }
//        byte[] bytes = {Byte.parseByte(startTime), Byte.parseByte(currentTime)};
//        message.setRaceStartTime(startTime);
        message = startTime + currentTime;
        return message;
    }
}