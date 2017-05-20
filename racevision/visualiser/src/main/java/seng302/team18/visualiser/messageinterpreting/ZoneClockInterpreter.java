package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.visualiser.display.ZoneTimeClock;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * A MessageInterpreter that takes a AC35RaceStatusMessage and updates the time on a ZoneTimeClock.
 */
public class ZoneClockInterpreter extends MessageInterpreter {

    private ZoneTimeClock clock;
    private ZoneId zoneId;

    /**
     * Constructor for ZoneClockInterpreter. Takes a ZoneTimeClock as a parameter which it updates every time a
     * AC35RaceStatusMessage is interpreted.
     * @param clock to be updated.
     * @param zoneId which we want to display
     */
    public ZoneClockInterpreter(ZoneTimeClock clock, ZoneId zoneId) {
        this.clock = clock;
        this.zoneId = zoneId;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35RaceStatusMessage) {
            System.out.println("status message got ");
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
            Instant currentIn = Instant.ofEpochMilli(statusMessage.getCurrentTime());
            clock.setTime(ZonedDateTime.ofInstant(currentIn, zoneId));
        }
    }
}
