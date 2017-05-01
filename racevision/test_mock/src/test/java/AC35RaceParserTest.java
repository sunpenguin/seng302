import org.junit.Before;
import org.junit.Test;
import seng302.team18.messageparsing.AC35XMLRaceMessage;
import seng302.team18.messageparsing.AC35XMLRaceParser;
import seng302.team18.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AC35RaceParserTest {

    private AC35XMLRaceMessage raceMessage;

    @Before
    public void setup() {
        AC35XMLRaceParser ac35RaceParser = new AC35XMLRaceParser();
        raceMessage = ac35RaceParser.parse(this.getClass().getResourceAsStream("/AC35race.xml"));
    }

    @Test
    public void testParser() {
        // Retrieve everything from the container
        String startTime = raceMessage.getRaceStartTime();
        List<BoundaryMark> boundaryMarks = raceMessage.getBoundaryMarks();
        List<CompoundMark> actualCompoundMarkMap = raceMessage.getCompoundMarks();
        List<Integer> participantIDs = raceMessage.getParticipantIDs();
        List<MarkRounding> markRoundings = raceMessage.getMarkRoundings();

        // Assert the start time is correct
        assertEquals("2011-08-06T13:30:00-07:00", startTime);
        // =============================================================================================================

        // Assert the boundary marks are correct
        List<BoundaryMark> actualBoundarymarks = new ArrayList();
        actualBoundarymarks.add(new BoundaryMark(1, new Coordinate(-36.8325, 174.8325)));
        actualBoundarymarks.add(new BoundaryMark(2, new Coordinate(-36.82883, 174.81983)));
        actualBoundarymarks.add(new BoundaryMark(3, new Coordinate(-36.82067, 174.81983)));
        actualBoundarymarks.add(new BoundaryMark(4, new Coordinate(-36.811, 174.8265)));

        for (int i = 0; i < 4; i++) {
            assertEquals(actualBoundarymarks.get(i).getSequenceID(), boundaryMarks.get(i).getSequenceID());
            assertEquals(actualBoundarymarks.get(i).getCoordinate().getLatitude(),
                    boundaryMarks.get(i).getCoordinate().getLatitude(), 0.000001);
            assertEquals(actualBoundarymarks.get(i).getCoordinate().getLongitude(),
                    boundaryMarks.get(i).getCoordinate().getLongitude(), 0.000001);
        }
        // =============================================================================================================

        // Assert the compoundMarks are correct
        Map<Integer, CompoundMark> expectedCMM = new HashMap<>();
        // Set up compoundMark 1 ---------------------------------------------------------------------------------------
        List<Mark> startMarks = new ArrayList<>();

        Mark mark1 = new Mark(101, new Coordinate(-36.83, 174.83));
        mark1.setName("PRO");

        Mark mark2 = new Mark(102, new Coordinate(-36.84, 174.81));
        mark2.setName("PIN");

        startMarks.add(mark1);
        startMarks.add(mark2);
        expectedCMM.put(1, new CompoundMark("StartLine", startMarks, 1));

        // set up compoundMark2 ----------------------------------------------------------------------------------------
        List<Mark> m1Mark = new ArrayList<>();

        Mark mark3 = new Mark(103, new Coordinate(-36.63566590, 174.88543944));
        mark3.setName("M1");

        m1Mark.add(mark3);

        expectedCMM.put(2, new CompoundMark("M1", m1Mark, 2));

        // set up compoundMark3 ----------------------------------------------------------------------------------------
        List<Mark> m2Mark = new ArrayList<>();

        Mark mark4 = new Mark(104, new Coordinate(-36.83, 174.80));
        mark4.setName("M2");

        m2Mark.add(mark4);

        expectedCMM.put(3, new CompoundMark("M2", m2Mark, 3));

        // set up compoundMark 4 ---------------------------------------------------------------------------------------
        List<Mark> gateMarks = new ArrayList<>();

        Mark mark5 = new Mark(105, new Coordinate(-36.63566590, 174.97205159));
        mark5.setName("G1");

        Mark mark6 = new Mark(106, new Coordinate(-36.64566590, 174.98205159));
        mark6.setName("G2");

        gateMarks.add(mark5);
        gateMarks.add(mark6);
        expectedCMM.put(4, new CompoundMark("Gate", gateMarks, 4));


        for (int i = 1; i < 5; i++) {
            CompoundMark actualCM = actualCompoundMarkMap.get(i);
            CompoundMark expectedCM = expectedCMM.get(i);
            List<Mark> actual = actualCompoundMarkMap.get(i).getMarks();
            List<Mark> expected = expectedCMM.get(i).getMarks();

            assertEquals(expected.size(), actual.size());
            assertEquals(expectedCM.getName(), actualCM.getName());

            for (int j = 0; j < actual.size(); j++) {
                assertEquals(expected.get(j).getId(), actual.get(j).getId());
//                assertEquals(expected.get(j).getName(), actual.get(j).getName()); TODO: if mark names are used we need to test them here
                assertEquals(expected.get(j).getCoordinate().getLatitude(), actual.get(j).getCoordinate().getLatitude(), 0.000001);
                assertEquals(expected.get(j).getCoordinate().getLongitude(), actual.get(j).getCoordinate().getLongitude(), 0.000001);
            }

        }
        // =============================================================================================================

        // Assert the MarkRoundings are correct TODO: if these get used in future, we will need to test them here

        // Assert participantIDs are correct
        List<Integer> expectedIDs = new ArrayList<>();
        expectedIDs.add(200);

        assertEquals(expectedIDs.size(), participantIDs.size());

        for (int i = 0; i < participantIDs.size(); i++) {
            assertEquals(expectedIDs.get(i), participantIDs.get(i));
        }
    }
}
