package seng302.team18.visualiser.messageinterpreting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.message.AC35XMLRaceMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.*;

import java.util.*;

/**
 * Created by david on 5/2/17.
 */
public class XMLRaceInterpreterTest {

    private Race race;
    private MessageInterpreter interpreter;
    private List<AbstractBoat> boats;
    private List<CompoundMark> compoundMarks;
    private List<Coordinate> boundaryMarks;
    private final String time = "2017-05-02T22:45:55+1200";

    @Before
    public void setUp() {
        Boat boat = new Boat("test", "t", 0, 1);
        boats = new ArrayList<>();
        boats.add(boat);
        race = new Race();
        interpreter = new XMLRaceInterpreter(race);

        // boundaries
        boundaryMarks = new ArrayList<>();
        boundaryMarks.add(new Coordinate(0d, 0d));

        // compound marks
        compoundMarks = new ArrayList<>();
        List<Mark> marks = new ArrayList<>();
        marks.add(new Mark(1, new Coordinate(0d, 0d)));
        compoundMarks.add(new CompoundMark("cm name", marks, 12));

        AC35XMLRaceMessage message = new AC35XMLRaceMessage();
        message.setRaceID(12);
        message.setStartTime(time);
        message.setBoundaryMarks(boundaryMarks);
        message.setCompoundMarks(compoundMarks);

        Map<Integer, AC35XMLRaceMessage.EntryDirection> ids = new HashMap<>();
        for (Integer i : Arrays.asList(1, 2, 3)) {
            ids.put(i, AC35XMLRaceMessage.EntryDirection.PORT);
        }

        message.setParticipants(ids);
        message.setMarkRoundings(new ArrayList<>());

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
     * test to see that boundaries and compound marks in course has changed.
     */
    @Test
    public void courseTest() {
        MessageBody message = new AC35XMLBoatMessage(boats);
        interpreter.interpret(message);

        Course expected = new Course();
        expected.setCourseLimits(boundaryMarks);
        expected.setCompoundMarks(compoundMarks);
        Course actual = race.getCourse();
        Assert.assertEquals(expected.getCentralCoordinate(), actual.getCentralCoordinate());
        Assert.assertEquals(0d, expected.getWindDirection(), 0.01);
        Assert.assertEquals(expected.getTimeZone(), actual.getTimeZone());
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

        Assert.assertEquals(RaceStatus.NOT_ACTIVE, race.getStatus());
        Assert.assertEquals(0, race.getId());
    }

}
