package seng302.team18.visualiser.messageinterpreting;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import seng302.team18.messageparsing.AC35MarkRoundingMessage;
import seng302.team18.messageparsing.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Justin on 3/05/2017.
 */
public class MarkRoundingInterpreterTest {
    private Race race;
    private List<Boat> boats;
    private MessageBody messageY;
    private MessageBody messageM;
    private MessageBody messageR;
    private MessageInterpreter yInterpreter;
    private MessageInterpreter mInterpreter;
    private MessageInterpreter rInterpreter;

    @Before public void setup() {
        race = new Race();
        Boat yBoat = new Boat("Yellow Boat", "Y Boat", 110);
        Boat mBoat = new Boat("Magenta Boat", "M Boat", 101);
        Boat rBoat = new Boat("Magenta Boat", "M Boat", 100);
        boats = Arrays.asList(yBoat, mBoat, rBoat);
        race.setStartingList(boats);
        race.setParticipantIds(Arrays.asList(110, 101, 100));
        messageY = new AC35MarkRoundingMessage(110, 1493805000L);
        messageM = new AC35MarkRoundingMessage(101, 0L);
        messageR = new AC35MarkRoundingMessage(100, -239086465L);
        yInterpreter = new MarkRoundingInterpreter(race);
        mInterpreter = new MarkRoundingInterpreter(race);
        rInterpreter = new MarkRoundingInterpreter(race);
    }

    @Test public void interpreterTest() {
        yInterpreter.interpret(messageY);
        mInterpreter.interpret(messageM);
        rInterpreter.interpret(messageR);

        long expectedYTimeLastMark = 1493805000L;
        long expectedMTimeLastMark = 0;
        long expectedRTimeLastMark = -239086465L;

        Boat yBoat = race.getStartingList().get(0);
        Boat mBoat = race.getStartingList().get(1);
        Boat rBoat = race.getStartingList().get(2);

        long actualYTimeLastMark = yBoat.getTimeAtLastMark();
        long acutalMTimeLastMark = mBoat.getTimeAtLastMark();
        long acutalRTimeLastMark = rBoat.getTimeAtLastMark();

        assertEquals(expectedYTimeLastMark, actualYTimeLastMark);
        assertEquals(expectedMTimeLastMark, acutalMTimeLastMark);
        assertEquals(expectedRTimeLastMark, acutalRTimeLastMark);
    }
}
