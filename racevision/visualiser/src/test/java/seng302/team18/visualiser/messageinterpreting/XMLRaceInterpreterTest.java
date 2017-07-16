package seng302.team18.visualiser.messageinterpreting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.message.AC35XMLRaceMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by david on 5/2/17.
 */
public class XMLRaceInterpreterTest {

    private Race race;
    private MessageInterpreter interpreter;
    private List<AbstractBoat> boats;
    private List<CompoundMark> compoundMarks;
    private List<BoundaryMark> boundaryMarks;
    private final String time = "2017-05-02T22:45:55.692+12:00";

    @Before
    public void setUp() {
        Boat boat = new Boat("test", "t", 0);
        boats = new ArrayList<>();
        boats.add(boat);
        race = new Race();
        interpreter = new XMLRaceInterpreter(race);

        // boundaries
        boundaryMarks = new ArrayList<>();
        boundaryMarks.add(new BoundaryMark(0, new Coordinate(0d, 0d)));

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
        message.setParticipantIDs(new ArrayList<>(Arrays.asList(1, 2, 3)));
        message.setMarkRoundings(new ArrayList<>());

        interpreter.interpret(message);
    }

//    ZonedDateTime startTime = ZonedDateTime.parse(raceMessage.getStartTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
//    race.setStartTime(startTime);
//    race.setParticipantIds(raceMessage.getParticipantIDs());
//
//    Course course = race.getCourse();
//    course.setCompoundMarks(raceMessage.getCompoundMarks());
//    course.setBoundaries(raceMessage.getBoundaryMarks());

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
        expected.setBoundaries(boundaryMarks);
        expected.setCompoundMarks(compoundMarks);
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

        ZonedDateTime expectedStart = ZonedDateTime.parse(time);
        ZonedDateTime expectedCurrent = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault());
        Assert.assertEquals(expectedCurrent, race.getCurrentTime());
        Assert.assertEquals(expectedStart, race.getStartTime());
        Assert.assertEquals(RaceStatus.NOT_ACTIVE, race.getStatus());
        Assert.assertEquals(0, race.getId());
    }

}
