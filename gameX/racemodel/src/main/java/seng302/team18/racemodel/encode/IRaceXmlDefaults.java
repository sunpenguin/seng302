package seng302.team18.racemodel.encode;

import seng302.team18.message.AC35XMLRaceMessage;

/**
 * This interface provides a skeleton for classes that supply the default values used when building a Race.xml message
 * from build classes.
 * <p>
 * When the build supports all the data currently given by defaults, this interface and its implementations will no
 * longer be necessary
 */
public interface IRaceXmlDefaults {

    AC35XMLRaceMessage.EntryDirection getParticipantEntryDirection();
}
