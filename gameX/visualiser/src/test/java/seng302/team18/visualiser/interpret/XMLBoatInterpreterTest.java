package seng302.team18.visualiser.interpret;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.AbstractBoat;
import seng302.team18.model.Boat;
import seng302.team18.model.Course;
import seng302.team18.model.RaceStatus;
import seng302.team18.visualiser.ClientRace;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 5/2/17.
 */
public class XMLBoatInterpreterTest {

    private ClientRace race;
    private MessageInterpreter interpreter;
    private List<AbstractBoat> boats;

    @Before
    public void setUp() {
        Boat boat = new Boat("test", "t", 0, 1);
        boats = new ArrayList<>();
        boats.add(boat);
        race = new ClientRace();
        interpreter = new XMLBoatInterpreter(race);
    }

    /**
     * test to see if the boat was added to the race.
     */
    @Test
    public void boatsTest() {
        Assert.assertEquals(0, race.getStartingList().size());
        MessageBody message = new AC35XMLBoatMessage(boats);
        interpreter.interpret(message);

        Boat expected = new Boat("test", "t", 0, 1);
        Assert.assertEquals(1, race.getStartingList().size());
        Boat actual = race.getStartingList().get(0);
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getTimeAtLastMark(), actual.getTimeAtLastMark());
        Assert.assertEquals(expected.getTimeSinceLastMark(), actual.getTimeSinceLastMark());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getShortName(), actual.getShortName());
        Assert.assertEquals(expected.getCoordinate(), actual.getCoordinate());
        Assert.assertEquals(expected.getHeading(), actual.getHeading(), 0.1);
        Assert.assertEquals(expected.getPlace(), actual.getPlace());
        Assert.assertEquals(expected.getSpeed(), actual.getSpeed(), 0.1);
        Assert.assertEquals(expected.getTimeTilNextMark(), actual.getTimeTilNextMark());
    }

    /**
     * test to see that nothing has changed.
     */
    @Test
    public void courseTest() {
        MessageBody message = new AC35XMLBoatMessage(boats);
        interpreter.interpret(message);

        Course expected = new Course();
        Course actual = race.getCourse();
        Assert.assertEquals(0d, expected.getWindDirection(), 0.01);
        Assert.assertEquals(expected.getTimeZone(), actual.getTimeZone());
        Assert.assertEquals(expected.getCenter(), actual.getCenter());
        Assert.assertEquals(expected.getCourseLimits().size(), actual.getCourseLimits().size());
        for (int i = 0; i < expected.getCourseLimits().size(); i++) {
            Assert.assertEquals(expected.getCourseLimits().get(i), actual.getCourseLimits().get(i));
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

        ZonedDateTime expectedTime = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault());
        Assert.assertEquals(0, race.getId());
        Assert.assertEquals(RaceStatus.NOT_ACTIVE, race.getStatus());
    }
}
