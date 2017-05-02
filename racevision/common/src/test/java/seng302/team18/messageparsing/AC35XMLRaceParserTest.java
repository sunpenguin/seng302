package seng302.team18.messageparsing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.model.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The tests for AC35 XML Race parser.
 */
public class AC35XMLRaceParserTest {
    private AC35XMLRaceParser raceParser;
    private InputStream fileToStream;
    private AC35XMLRaceMessage raceMessageToTest;

    private List<CompoundMark> expectedCompoundMarks;
    private List<MarkRounding> expectedMarkRounding;
    private List<BoundaryMark> expectedBoundaries;

    private Integer byteArrayLength = 99999;
    private Double delta = 0.001;

    @Before
    public void setUp() throws IOException {
        if (fileToStream == null) {
            raceParser = new AC35XMLRaceParser();
            fileToStream = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream("race1.xml"));

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[byteArrayLength];

            while ((nRead = fileToStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            byte[] bytes = buffer.toByteArray();
            MessageBody message = raceParser.parse(bytes);
            raceMessageToTest = (AC35XMLRaceMessage) message;

            setUpExpectedValues();
        }
    }

    private void setUpExpectedValues() {
        // List of expected marks
        expectedCompoundMarks = new ArrayList<>();
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
                new Mark(128, new Coordinate(57.6715240, 11.8444950)),
                new Mark(129, new Coordinate(57.6715240, 11.8444950))));
        expectedCompoundMarks.add(new CompoundMark("Mark4", marks6, 6));

        // List of expected mark roundings
        expectedMarkRounding = new ArrayList<>();
        expectedMarkRounding.add(new MarkRounding(1, expectedCompoundMarks.get(0)));
        expectedMarkRounding.add(new MarkRounding(2, expectedCompoundMarks.get(1)));
        expectedMarkRounding.add(new MarkRounding(3, expectedCompoundMarks.get(2)));
        expectedMarkRounding.add(new MarkRounding(4, expectedCompoundMarks.get(3)));
        expectedMarkRounding.add(new MarkRounding(5, expectedCompoundMarks.get(4)));
        expectedMarkRounding.add(new MarkRounding(6, expectedCompoundMarks.get(5)));

        // List of Boundary Marks
        expectedBoundaries = new ArrayList<>();
        expectedBoundaries.add(new BoundaryMark(1, new Coordinate(57.6739450, 11.8417100)));
        expectedBoundaries.add(new BoundaryMark(2, new Coordinate(57.6709520, 11.8485010)));
        expectedBoundaries.add(new BoundaryMark(3, new Coordinate(57.6690260, 11.8472790)));
        expectedBoundaries.add(new BoundaryMark(4, new Coordinate(57.6693140, 11.8457610)));
        expectedBoundaries.add(new BoundaryMark(5, new Coordinate(57.6665370, 11.8432910)));
        expectedBoundaries.add(new BoundaryMark(6, new Coordinate(57.6641400, 11.8385840)));
        expectedBoundaries.add(new BoundaryMark(7, new Coordinate(57.6629430, 11.8332030)));
        expectedBoundaries.add(new BoundaryMark(8, new Coordinate(57.6629480, 11.8249660)));
        expectedBoundaries.add(new BoundaryMark(9, new Coordinate(57.6686890, 11.8250920)));
        expectedBoundaries.add(new BoundaryMark(10, new Coordinate(57.6708220, 11.8321340)));
    }

    @Test
    public void parseAC35XMLRaceStartTimeTest() {
        String expectedTime = "2015-08-29T13:10:00+02:00";
        String actualTime = raceMessageToTest.getRaceStartTime();
        Assert.assertEquals(expectedTime, actualTime);
    }

    @Test
    public void parseAC35XMLRaceParticipantIDsTest() {
        List<Integer> expectedIDs = new ArrayList<>(Arrays.asList(101, 102, 103, 104, 105, 106));
        List<Integer> actualIDs = raceMessageToTest.getParticipantIDs();
        Assert.assertEquals(expectedIDs, actualIDs);
    }

    @Test
    public void parseAC35XMLCompoundMarksTest() {
        List<CompoundMark> actualCompoundMarks = raceMessageToTest.getCompoundMarks();
        // Checks for multiple things
        Assert.assertEquals(expectedCompoundMarks.size(), actualCompoundMarks.size());
        Assert.assertEquals(expectedCompoundMarks.get(0).getMidCoordinate(), actualCompoundMarks.get(0).getMidCoordinate());
        Assert.assertEquals(expectedCompoundMarks.get(3).getName(), actualCompoundMarks.get(3).getName());
        Assert.assertEquals(
                expectedCompoundMarks.get(5).getMarks().get(0).getCoordinate().getLatitude(),
                actualCompoundMarks.get(5).getMarks().get(0).getCoordinate().getLatitude(),
                delta);
    }

    @Test
    public void parseAC35XMLRaceMarkRoundingTest() {
        List<MarkRounding> actualMarkRoundings = raceMessageToTest.getMarkRoundings();
        Assert.assertEquals(expectedMarkRounding.size(), actualMarkRoundings.size());
    }

    @Test
    public void parseAC35XMLRaceBoundariesTest() {
        List<BoundaryMark> actualBoundaries = raceMessageToTest.getBoundaryMarks();

        Assert.assertEquals(expectedBoundaries.size(), actualBoundaries.size());
        Assert.assertEquals(expectedBoundaries.get(4).getSequenceID(), actualBoundaries.get(4).getSequenceID());
        Assert.assertEquals(
                expectedBoundaries.get(9).getCoordinate().getLatitude(),
                actualBoundaries.get(9).getCoordinate().getLatitude(),
                delta);
    }
}
