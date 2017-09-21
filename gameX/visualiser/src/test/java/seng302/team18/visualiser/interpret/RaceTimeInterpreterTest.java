package seng302.team18.visualiser.interpret;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.Course;
import seng302.team18.model.RaceStatus;
import seng302.team18.visualiser.ClientRace;
import seng302.team18.visualiser.interpret.americascup.RaceTimeInterpreter;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 4/29/17.
 */
public class RaceTimeInterpreterTest {

    private ClientRace race;
    private MessageInterpreter raceTimeInterpreter;
    private Boat boat;
    private MessageBody message;

    @Before
    public void setUp() {
        boat = new Boat("test", "t", 0, 1);
        boat.setTimeAtLastMark(3000L);
        List<Boat> boats = new ArrayList<>();
        boats.add(boat);
        race = new ClientRace();
        race.setStartingList(boats);
        raceTimeInterpreter = new RaceTimeInterpreter(race);
        message = new AC35RaceStatusMessage(13000, 0, 2000, 0, 0, new ArrayList<>());
        raceTimeInterpreter.interpret(message);
    }

    /**
     * test to see if ONLY the time values in boat have changed
     */
    @Test
    public void boatsTest() {
        Boat expected = new Boat("test", "t", 0, 1);
        expected.setTimeAtLastMark(3000L);
        expected.setTimeSinceLastMark(10L);
        Assert.assertEquals(1, race.getStartingList().size());
        Boat actual = race.getStartingList().get(0);
        Assert.assertEquals(expected.getTimeAtLastMark(), actual.getTimeAtLastMark());
        Assert.assertEquals(expected.getTimeSinceLastMark(), actual.getTimeSinceLastMark());
        // everything below should not change at all.
        Assert.assertEquals(expected.getName(), actual.getName());
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
        Assert.assertEquals(expected.getCenter(), actual.getCenter());
        Assert.assertEquals(expected.getTimeZone(), actual.getTimeZone());
        Assert.assertEquals(expected.getLimits().size(), actual.getLimits().size());
        for (int i = 0; i < expected.getLimits().size(); i++) {
            Assert.assertEquals(expected.getLimits().get(i), actual.getLimits().get(i));
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
        Assert.assertEquals(RaceStatus.NOT_ACTIVE, race.getStatus());
    }
}
