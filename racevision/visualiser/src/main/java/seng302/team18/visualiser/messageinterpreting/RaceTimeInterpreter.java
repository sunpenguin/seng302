package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.messageparsing.AC35RaceStatusMessage;
import seng302.team18.messageparsing.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * Created by dhl25 on 27/04/17.
 */
public class RaceTimeInterpreter extends MessageInterpreter {

    private Race race;

    public RaceTimeInterpreter(Race race) {
        this.race = race;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35RaceStatusMessage) {
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
            Instant startIn = Instant.ofEpochMilli(statusMessage.getStartTime());
            Instant currentIn = Instant.ofEpochMilli(statusMessage.getCurrentTime());
            ZonedDateTime startTime = ZonedDateTime.ofInstant(startIn, race.getCourse().getTimeZone());
            ZonedDateTime currentTime = ZonedDateTime.ofInstant(currentIn, race.getCourse().getTimeZone());

            race.setStartTime(startTime);
            race.setCurrentTime(currentTime);

            updateBoatMarkTimes(race.getStartingList(), statusMessage.getCurrentTime());
        }
    }

    private void updateBoatMarkTimes(Iterable<Boat> boats, long time) {
        for (Boat boat : boats) {
            if (!boat.getTimeAtLastMark().equals(0L)) {
                boat.setTimeSinceLastMark((time - boat.getTimeAtLastMark()) / 1000);
            }
        }
    }
}
