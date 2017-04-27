package seng302.team18.visualiser.display;

import seng302.team18.messageparsing.AC35RaceStatusMessage;
import seng302.team18.messageparsing.MessageBody;
import seng302.team18.messageinterpreting.MessageInterpreter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Created by spe76 on 27/04/17.
 */
public class RaceClockInterpreter extends MessageInterpreter {

    private RaceClock raceClock;
    private ZoneId timeZone;

    public RaceClockInterpreter(RaceClock raceClock, ZoneId timeZone) {
        this.raceClock = raceClock;
        this.timeZone = timeZone;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35RaceStatusMessage) {
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
            Instant startIn = Instant.ofEpochMilli(statusMessage.getStartTime());
            Instant currentIn = Instant.ofEpochMilli(statusMessage.getCurrentTime());
//            ZonedDateTime startTime = ZonedDateTime.ofInstant(startIn, timeZone);
//            ZonedDateTime currentTime = ZonedDateTime.ofInstant(currentIn, timeZone);

//            System.out.println("minused");
//            System.out.println((currentIn.minusSeconds(statusMessage.getStartTime() / 1000)).format(DateTimeFormatter.ISO_DATE_TIME));
//            System.out.println("original");
//            System.out.println(currentIn.format(DateTimeFormatter.ISO_DATE_TIME));
//            System.out.println("chrono");
//            System.out.println(ChronoUnit.SECONDS.between(startIn, currentIn));
//            System.out.println();
            raceClock.setTime(ChronoUnit.SECONDS.between(startIn, currentIn));
        }
    }
}
