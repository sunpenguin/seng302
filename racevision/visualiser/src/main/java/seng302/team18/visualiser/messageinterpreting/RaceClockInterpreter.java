package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.messageparsing.AC35RaceStatusMessage;
import seng302.team18.messageparsing.MessageBody;
import seng302.team18.visualiser.display.RaceClock;
import seng302.team18.visualiser.messageinterpreting.MessageInterpreter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Created by spe76 on 27/04/17.
 */
public class RaceClockInterpreter extends MessageInterpreter {

    private RaceClock raceClock;

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
