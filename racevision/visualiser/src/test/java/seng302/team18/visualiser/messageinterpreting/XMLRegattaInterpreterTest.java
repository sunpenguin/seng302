package seng302.team18.visualiser.messageinterpreting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.message.AC35XMLRegattaMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David-chan on 3/05/17.
 */
public class XMLRegattaInterpreterTest {

    private Race race;
    private MessageInterpreter interpreter;
    private List<AbstractBoat> boats;
    private String utcOffset = "+1";

    @Before
    public void setUp() {
        Boat boat = new Boat("test", "t", 0, 1);
        boats = new ArrayList<>();
        boats.add(boat);
        race = new Race();
        interpreter = new XMLRegattaInterpreter(race);

        MessageBody message = new AC35XMLRegattaMessage(12, "The Best Race", "The Best Course", 0, 0, utcOffset);
        interpreter.interpret(message);
    }

    /**
     * test to see if nothing in boats have changed
     */
    @Test
    public void boatsTest() {
        MessageBody message = new AC35XMLBoatMessage(boats);
        interpreter.interpret(message);

        Assert.assertEquals(0, race.getStartingList().size());
    }

    /**
     * test to see that ZoneId in course has changed.
     */
    @Test
    public void courseTest() {
        MessageBody message = new AC35XMLBoatMessage(boats);
        interpreter.interpret(message);

        Course expected = new Course();
        expected.setTimeZone(ZoneId.of("UTC" + utcOffset));
        Course actual = race.getCourse();
        Assert.assertEquals(expected.getCentralCoordinate(), actual.getCentralCoordinate());
        Assert.assertEquals(0d, expected.getWindDirection(), 0.01);
        Assert.assertEquals(expected.getTimeZone(), actual.getTimeZone());
        Assert.assertEquals(expected.getBoundaries().size(), actual.getBoundaries().size());
        for (int i = 0; i < expected.getBoundaries().size(); i++) {
            Assert.assertEquals(expected.getBoundaries().get(i), actual.getBoundaries().get(i));
        }
        Assert.assertEquals(expected.getCompoundMarks().size(), actual.getCompoundMarks().size());
        for (int i = 0; i < expected.getCompoundMarks().size(); i++) {
            Assert.assertEquals(expected.getCompoundMarks().get(i), actual.getCompoundMarks().get(i));
        }
    }

    /**
     * test to see that nothing in race has changed
     */
    @Test
    public void raceTest() {
        MessageBody message = new AC35XMLBoatMessage(boats);
        interpreter.interpret(message);

        Assert.assertEquals(RaceStatus.NOT_ACTIVE, race.getStatus());
        Assert.assertEquals(0, race.getId());
        ZonedDateTime expected = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault());
        Assert.assertEquals(expected, race.getCurrentTime());
        Assert.assertEquals(expected, race.getStartTime());
    }


}
