package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.visualiser.display.RaceClock;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * A MessageInterpreter that takes a AC35RaceStatusMessage and updates the time on a RaceClock.
 */
public class RaceClockInterpreter extends MessageInterpreter {

    private RaceClock raceClock;

    /**
     * Constructor for RaceClockInterpreter. Takes a RaceClock as a parameter which it updates every time a
     * AC35RaceStatusMessage is interpreted.
     * @param raceClock to be updated.
     */
    public RaceClockInterpreter(RaceClock raceClock) {
        this.raceClock = raceClock;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35RaceStatusMessage) {
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
            Instant startIn = Instant.ofEpochMilli(statusMessage.getStartTime());
            Instant currentIn = Instant.ofEpochMilli(statusMessage.getCurrentTime());
            raceClock.setTime(ChronoUnit.SECONDS.between(startIn, currentIn));
        }
    }
}
