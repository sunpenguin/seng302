package seng302.team18.visualiser.messageinterpreting;

import javafx.scene.control.Label;
import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Race;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * The interpreter class for setting the pre-race start time.
 */
public class PreRaceStartTimeInterpreter extends MessageInterpreter {

    private Race race;

    /**
     * Constructor for PreRaceStartTimeInterpreter.
     *
     * @param race to be updated.
     */
    public PreRaceStartTimeInterpreter(Race race) {
        this.race = race;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35RaceStatusMessage) {
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
            Instant instant = Instant.ofEpochMilli(statusMessage.getStartTime());
            ZonedDateTime startTime = ZonedDateTime.ofInstant(instant, race.getCourse().getTimeZone());
            race.setStartTime(startTime);
        }
    }
}
