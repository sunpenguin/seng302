package seng302.team18.visualiser.interpret;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * A MessageInterpreter that takes a AC35RaceStatusMessage and updates the current time and start time of a Race and
 * also updates races boats time elapsed since passing last mark.
 */
public class RaceTimeInterpreter extends MessageInterpreter {

    private Race race;

    /**
     * Constructor for RaceTimeInterpreter. Takes a Race as a parameter which it updates every time a
     * AC35RaceStatusMessage is interpreted.
     * @param race to be updated.
     */
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

    /**
     * Updates the time elapsed since passing last mark for all given boats.
     * @param boats to be updated.
     * @param time elapsed since passing last mark.
     */
    private void updateBoatMarkTimes(Iterable<Boat> boats, long time) {
        for (Boat boat : boats) {
            if (!boat.getTimeAtLastMark().equals(0L)) {
                boat.setTimeSinceLastMark((time - boat.getTimeAtLastMark()) / 1000);
            }
        }
    }
}
