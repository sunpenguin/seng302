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
import seng302.team18.visualiser.interpret.americascup.WindDirectionInterpreter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 5/2/17.
 */
public class WindDirectionInterpreterTest {

    private ClientRace race;
    private MessageInterpreter interpreter;
    private Boat boat;
    private double windDirection = 35.4;
    private double windSpeed = 10;

    @Before
    public void setUp() {
        boat = new Boat("test", "t", 0, 1);
        List<Boat> boats = new ArrayList<>();
        boats.add(boat);
        race = new ClientRace();
        race.setStartingList(boats);
        interpreter = new WindDirectionInterpreter(race);
        MessageBody message = new AC35RaceStatusMessage(0, 0, 0, windDirection, windSpeed, new ArrayList<>());
        interpreter.interpret(message);
    }

    /**
     * test to see if nothing in boats have changed
     */
    @Test
    public void boatsTest() {
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
     * test to see that ONLY wind direction has changed.
     */
    @Test
    public void courseTest() {
        Course expected = new Course();
        Course actual = race.getCourse();
        double expectedWindDirection = windDirection;
        Assert.assertEquals(expectedWindDirection, actual.getWindDirection(), 0.01);
        Assert.assertEquals(expected.getTimeZone(), actual.getTimeZone());
        Assert.assertEquals(expected.getCenter(), actual.getCenter());
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
     * test to see that nothing in race has changed
     */
    @Test
    public void raceTest() {
        ZonedDateTime expectedTime = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault());
        Assert.assertEquals(0, race.getId());
        Assert.assertEquals(RaceStatus.NOT_ACTIVE, race.getStatus());
    }

}
