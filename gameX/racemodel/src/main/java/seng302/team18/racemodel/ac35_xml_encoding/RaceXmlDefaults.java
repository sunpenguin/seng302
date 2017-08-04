package seng302.team18.racemodel.ac35_xml_encoding;

import seng302.team18.message.AC35XMLRaceMessage;

/**
 * Defaults for when building a Race.xml message from model classes
 */
public class RaceXmlDefaults implements IRaceXmlDefaults {

    public AC35XMLRaceMessage.EntryDirection getParticipantEntryDirection() {
        return AC35XMLRaceMessage.EntryDirection.PORT;
    }
}
