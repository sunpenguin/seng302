package seng302.team18.visualiser.messageinterpreting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Yacht;
import seng302.team18.model.Course;
import seng302.team18.model.Race;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

/**
 * Created by david on 5/2/17.
 */
public class WindDirectionInterpreterTest {

    private Race race;
    private MessageInterpreter interpreter;
    private Yacht yacht;
    private double windDirection = 35.4;

    @Before
    public void setUp() {
        yacht = new Yacht("test", "t", 0);
        List<Yacht> yachts = new ArrayList<>();
        yachts.add(yacht);
        race = new Race();
        race.setStartingList(yachts);
        interpreter = new WindDirectionInterpreter(race);
        MessageBody message = new AC35RaceStatusMessage(0, 0, 0, windDirection, new ArrayList<>());
        interpreter.interpret(message);
    }

    /**
     * test to see if nothing in boats have changed
     */
    @Test
    public void boatsTest() {
        Yacht expected = new Yacht("test", "t", 0);
        Assert.assertEquals(1, race.getStartingList().size());
        Yacht actual = race.getStartingList().get(0);
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getTimeAtLastMark(), actual.getTimeAtLastMark());
        Assert.assertEquals(expected.getTimeSinceLastMark(), actual.getTimeSinceLastMark());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getNameShort(), actual.getNameShort());
        Assert.assertEquals(expected.getCoordinate(), actual.getCoordinate());
        Assert.assertEquals(expected.getHeading(), actual.getHeading(), 0.1);
        Assert.assertEquals(expected.getPlace(), actual.getPlace());
        Assert.assertEquals(expected.getSpeed(), actual.getSpeed(), 0.1);
        Assert.assertEquals(expected.getTimeTilNextMark(), actual.getTimeTilNextMark());
    }

    /**
     * test to see that ONLY wind direction has changed.
     */
    @Test
    public void courseTest() {
        Course expected = new Course();
        Course actual = race.getCourse();
        double expectedWindDirection = abs((windDirection - 180) % 360);
        Assert.assertEquals(expectedWindDirection, actual.getWindDirection(), 0.01);
        Assert.assertEquals(expected.getTimeZone(), actual.getTimeZone());
        Assert.assertEquals(expected.getCentralCoordinate(), actual.getCentralCoordinate());
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
        ZonedDateTime expectedTime = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault());
        Assert.assertEquals(0, race.getId());
        Assert.assertEquals(0, race.getStatus());
        Assert.assertEquals(expectedTime, race.getCurrentTime());
        Assert.assertEquals(expectedTime, race.getStartTime());
    }

}
