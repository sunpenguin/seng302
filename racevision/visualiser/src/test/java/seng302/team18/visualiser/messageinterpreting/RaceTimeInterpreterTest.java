package seng302.team18.visualiser.messageinterpreting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.Course;
import seng302.team18.model.Race;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by david on 4/29/17.
 */
public class RaceTimeInterpreterTest {

    private Race race;
    private MessageInterpreter raceTimeInterpreter;
    private Boat boat;
    private MessageBody message;

    @Before
    public void setUp() {
        boat = new Boat("test", "t", 0);
        boat.setTimeAtLastMark(3000L);
        List<Boat> boats = new ArrayList<>();
        boats.add(boat);
        race = new Race();
        race.setStartingList(boats);
        raceTimeInterpreter = new RaceTimeInterpreter(race);
        message = new AC35RaceStatusMessage(13000, 0, 2000, 0, new ArrayList<>());
        raceTimeInterpreter.interpret(message);
    }

    /**
     * test to see if ONLY the time values in boat have changed
     */
    @Test
    public void boatsTest() {
        Boat expected = new Boat("test", "t", 0);
        expected.setTimeAtLastMark(3000L);
        expected.setTimeSinceLastMark(10L);
        Assert.assertEquals(1, race.getStartingList().size());
        Boat actual = race.getStartingList().get(0);
        Assert.assertEquals(expected.getTimeAtLastMark(), actual.getTimeAtLastMark());
        Assert.assertEquals(expected.getTimeSinceLastMark(), actual.getTimeSinceLastMark());
        // everything below should not change at all.
        Assert.assertEquals(expected.getBoatName(), actual.getBoatName());
        Assert.assertEquals(expected.getShortName(), actual.getShortName());
        Assert.assertEquals(expected.getCoordinate(), actual.getCoordinate());
        Assert.assertEquals(expected.getHeading(), actual.getHeading(), 0.1);
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getPlace(), actual.getPlace());
        Assert.assertEquals(expected.getSpeed(), actual.getSpeed(), 0.1);
        Assert.assertEquals(expected.getTimeTilNextMark(), actual.getTimeTilNextMark());
    }

    /**
     * test to see that nothing in course has changed.
     */
    @Test
    public void courseTest() {
        Course expected = new Course();
        Course actual = race.getCourse();
        Assert.assertEquals(expected.getCentralCoordinate(), actual.getCentralCoordinate());
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
     * test to see that the time in race has changed
     */
    @Test
    public void raceTest() {
        ZonedDateTime expected = ZonedDateTime.ofInstant(Instant.EPOCH, race.getCourse().getTimeZone()).plusSeconds(2);
        Assert.assertEquals(expected, race.getStartTime());
        Assert.assertEquals(0, race.getId());
        Assert.assertEquals(0, race.getStatus());
    }
}
