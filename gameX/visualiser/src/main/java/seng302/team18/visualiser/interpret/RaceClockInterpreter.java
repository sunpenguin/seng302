package seng302.team18.visualiser.interpret;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.visualiser.display.Clock;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * A MessageInterpreter that takes a AC35RaceStatusMessage and updates the time on a StopWatchClock.
 */
public class RaceClockInterpreter extends MessageInterpreter {

    private Clock clock;

    /**
     * Constructor for RaceClockInterpreter. Takes a StopWatchClock as a parameter which it updates every time a
     * AC35RaceStatusMessage is interpreted.
     * @param clock to be updated.
     */
    public RaceClockInterpreter(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35RaceStatusMessage) {
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
            Instant startIn = Instant.ofEpochMilli(statusMessage.getStartTime());
            Instant currentIn = Instant.ofEpochMilli(statusMessage.getCurrentTime());
            clock.setTime(ChronoUnit.SECONDS.between(startIn, currentIn));
        }
    }
}
