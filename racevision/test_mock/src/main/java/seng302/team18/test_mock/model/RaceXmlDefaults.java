package seng302.team18.test_mock.model;

import seng302.team18.message.AC35XMLRaceMessage;

/**
 * Created by afj19 on 10/07/17.
 */
public class RaceXmlDefaults implements IRaceXmlDefaults {

    public AC35XMLRaceMessage.EntryDirection getParticipantEntryDirection() {
        return AC35XMLRaceMessage.EntryDirection.PORT;
    }
}
