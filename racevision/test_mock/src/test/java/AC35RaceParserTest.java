import org.junit.Before;
import org.junit.Test;
import seng302.team18.model.*;
import seng302.team18.test_mock.XMLparsers.AC35RaceContainer;
import seng302.team18.test_mock.XMLparsers.AC35RaceParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by Justin on 20/04/2017.
 */
public class AC35RaceParserTest {

    private AC35RaceContainer ac35RaceContainer;

    @Before
    public void setup() {
        AC35RaceParser ac35RaceParser = new AC35RaceParser();
        ac35RaceContainer = ac35RaceParser.parse(this.getClass().getResourceAsStream("/AC35race.xml"));
    }

    @Test
    public void testParser() {
        // Retrieve everything from the container
        String startTime = ac35RaceContainer.getStartTime();
        List<BoundaryMark> boundaryMarks = ac35RaceContainer.getBoundaryMark();
        Map<Integer, CompoundMark> compoundMarkMap = ac35RaceContainer.getCompoundMarks();
        List<Integer> participantIDs = ac35RaceContainer.getParticipantIDs();
        List<MarkRounding> markRoundings = ac35RaceContainer.getMarkRoundings();

        // Assert the start time is correct
        assertEquals("2011-08-06T13:30:00-0700", startTime);
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
        Map<Integer, CompoundMark> actualCMM = new HashMap<>();
        // Set up compoundMark 1 ---------------------------------------------------------------------------------------
        List<Mark> startMarks = new ArrayList<>();

        Mark mark1 = new Mark(101, new Coordinate(-36.83, 174.83));
        mark1.setName("PRO");

        Mark mark2 = new Mark(102, new Coordinate(-36.84, 174.81));
        mark2.setName("PIN");

        startMarks.add(mark1);
        startMarks.add(mark2);
        actualCMM.put(1, new CompoundMark("Startline", startMarks, 1));

        // set up compoundMark2 ----------------------------------------------------------------------------------------
        List<Mark> m1Mark = new ArrayList<>();

        Mark mark3 = new Mark(103, new Coordinate(-36.63566590, 174.88543944));
        mark1.setName("M1");

        startMarks.add(mark1);

        actualCMM.put(3, new CompoundMark("M1", startMarks, 3));

        // set up compoundMark3 ----------------------------------------------------------------------------------------
        List<Mark> m2Mark = new ArrayList<>();

        Mark mark4 = new Mark(104, new Coordinate(-36.63566590, 174.88543944));
        mark1.setName("M2");

        startMarks.add(mark1);

        actualCMM.put(3, new CompoundMark("M2", startMarks, 3));

        // set up compoundMark 4 ---------------------------------------------------------------------------------------
        List<Mark> gateMarks = new ArrayList<>();

        Mark mark5 = new Mark(104, new Coordinate(-36.63566590, 174.97205159));
        mark1.setName("G1");

        Mark mark6 = new Mark(105, new Coordinate(-36.64566590, 174.98205159));
        mark2.setName("G2");

        gateMarks.add(mark5);
        gateMarks.add(mark6);
        actualCMM.put(4, new CompoundMark("Gate", startMarks, 4));


        for (int i = 1; i < 5; i++) {
            CompoundMark actual = compoundMarkMap.get(i);
            CompoundMark expected = actualCMM.get(i);

            for (Mark mark : actual.getMarks()) {
                System.out.println(mark.getName());
            }

            assertEquals(expected.getMarks().size(), actual.getMarks().size());

//            for (Mark mark : ) {
//
//            }
        }
        // =============================================================================================================


    }
}
