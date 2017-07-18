package seng302.team18.visualiser.messageinterpreting;

import javafx.scene.control.Label;
import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Race;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * Created by spe76 on 18/07/17.
 */
public class PreRaceStartTimeInterpreter extends MessageInterpreter {

    private Race race;

    /**
     * PLEASE WRITE ME
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
//            startTime = ZonedDateTime.ofInstant(Instant.EPOCH, course.getTimeZone()).now(course.getTimeZone()).plusSeconds(300);
            race.setStartTime(ZonedDateTime.ofInstant(Instant.ofEpochMilli(statusMessage.getStartTime()), race.getCourse().getTimeZone()));
        }
    }
}
