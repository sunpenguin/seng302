package seng302.team18.test_mock.model;

import seng302.team18.message.AC35XMLRaceMessage;
import seng302.team18.message.XmlMessage;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;
import seng302.team18.model.RaceStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anton J on 10/07/2017.
 */
public class RaceXmlMessageBuilder {

    public AC35XMLRaceMessage buildXmlMessage(Race race) {
        AC35XMLRaceMessage message = new AC35XMLRaceMessage();
        message.setBoundaryMarks(race.getCourse().getBoundaries());
        message.setCompoundMarks(race.getCourse().getCompoundMarks());
        message.setStartTime(race.getStartTime().format(XmlMessage.DATE_TIME_FORMATTER));
        message.setStartPostponed(race.getStatus().equals(RaceStatus.POSTPONED));
        message.setRaceType(race.getRaceType());
        message.setRaceID(race.getId());

        Map<Integer, AC35XMLRaceMessage.EntryDirection> participants = new HashMap<>();
        for (Boat boat : race.getStartingList()) {
            participants.put(boat.getId(), getDefaultParticipantEntryDirection());
        }
        message.setParticipants(participants);

        message.setMarkRoundings(race.getCourse().getMarkRoundings());

        return message;
    }

    private AC35XMLRaceMessage.EntryDirection getDefaultParticipantEntryDirection() {
        return AC35XMLRaceMessage.EntryDirection.PORT;
    }
}
