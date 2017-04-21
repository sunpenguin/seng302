package seng302.team18.data;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.model.CompoundMark;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Mark;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by spe76 on 21/04/17.
 */
public class AC35XMLRaceParserTest {
    private AC35XMLRaceParser raceParser;
    InputStream fileToStream;

    @Before
    public void setUp() {
        raceParser = new AC35XMLRaceParser();
        fileToStream = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream("race1.xml"));
    }

    @Test
    public void parseAC35XMLRaceTest1() throws IOException {
        byte[] bytes = new byte[5256];
        fileToStream.read(bytes, 0, 5256); // length of the XML file
        MessageBody message = raceParser.parse(bytes);
        AC35XMLRaceMessage raceMessage = (AC35XMLRaceMessage) message;

        String expectedTime = "2015-08-29T13:10:00+02:00";
        String actualTime = raceMessage.getRaceStartTime();
        Assert.assertEquals(expectedTime, actualTime);

        List<Integer> expectedIDs = new ArrayList<>(Arrays.asList(101, 102, 103, 104, 105, 106));
        List<Integer> actualIDs = raceMessage.getParticipantIDs();
        Assert.assertEquals(expectedIDs, actualIDs);
//        new CompoundMark(compoundMarkName, marks, compoundMarkID)

        List<CompoundMark> expectedCompoundMarks = new ArrayList<>();
        List<Mark> marks1 = new ArrayList<>(Arrays.asList(
                new Mark(122, new Coordinate(57.6703330, 11.8278330)),
                new Mark(123, new Coordinate(57.6703330, 11.8278330))));
        expectedCompoundMarks.add(new CompoundMark("Mark0", marks1, 1));
        List<Mark> marks2 = new ArrayList<>(Arrays.asList(
                new Mark(131, new Coordinate(57.6675700, 11.8359880))));
        expectedCompoundMarks.add(new CompoundMark("Mark1", marks2, 2));
        List<Mark> marks3 = new ArrayList<>(Arrays.asList(
                new Mark(124, new Coordinate(57.6708220, 11.8433900)),
                new Mark(125, new Coordinate(57.6708220, 11.8433900))));
        expectedCompoundMarks.add(new CompoundMark("Mark2", marks3, 3));
        List<Mark> marks4 = new ArrayList<>(Arrays.asList(
                new Mark(126, new Coordinate(57.6650170, 11.8279170)),
                new Mark(127, new Coordinate(57.6650170, 11.8279170))));
        expectedCompoundMarks.add(new CompoundMark("Mark3", marks4, 4));
        List<Mark> marks5 = new ArrayList<>(Arrays.asList(
                new Mark(124, new Coordinate(57.6708220, 11.8433900)),
                new Mark(125, new Coordinate(57.6708220, 11.8433900))));
        expectedCompoundMarks.add(new CompoundMark("Mark2", marks5, 5));
        List<Mark> marks6 = new ArrayList<>(Arrays.asList(
                new Mark(126, new Coordinate(57.6650170, 11.8279170)),
                new Mark(127, new Coordinate(57.6650170, 11.8279170))));
        expectedCompoundMarks.add(new CompoundMark("Mark3", marks6, 6));
        List<Mark> marks7 = new ArrayList<>(Arrays.asList(
                new Mark(124, new Coordinate(57.6708220, 11.8433900)),
                new Mark(125, new Coordinate(57.6708220, 11.8433900))));
        expectedCompoundMarks.add(new CompoundMark("Mark2", marks7, 7));
        List<Mark> marks8 = new ArrayList<>(Arrays.asList(
                new Mark(126, new Coordinate(57.6650170, 11.8279170)),
                new Mark(127, new Coordinate(57.6650170, 11.8279170))));
        expectedCompoundMarks.add(new CompoundMark("Mark3", marks8, 8));
        List<Mark> marks9 = new ArrayList<>(Arrays.asList(
                new Mark(124, new Coordinate(57.6708220, 11.8433900)),
                new Mark(125, new Coordinate(57.6708220, 11.8433900))));
        expectedCompoundMarks.add(new CompoundMark("Mark2", marks9, 9));
        List<Mark> marks10 = new ArrayList<>(Arrays.asList(
                new Mark(126, new Coordinate(57.6650170, 11.8279170)),
                new Mark(127, new Coordinate(57.6650170, 11.8279170))));
        expectedCompoundMarks.add(new CompoundMark("Mark3", marks10, 10));
        List<Mark> marks11 = new ArrayList<>(Arrays.asList(
                new Mark(128, new Coordinate(57.6715240, 11.8444950)),
                new Mark(129, new Coordinate(57.6715240, 11.8444950))));
        expectedCompoundMarks.add(new CompoundMark("Mark4", marks11, 11));

        List<CompoundMark> actualCompoundMarks = raceMessage.getCompoundMarks();
        // Checks for multiple things
        Assert.assertEquals(expectedCompoundMarks.size(), actualCompoundMarks.size());
        Assert.assertEquals(expectedCompoundMarks.get(0).getMidCoordinate(), actualCompoundMarks.get(0).getMidCoordinate());
    }
}
