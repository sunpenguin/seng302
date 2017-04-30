package seng302.team18.visualiser.messageinterpreting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.messageparsing.AC35RaceStatusMessage;
import seng302.team18.messageparsing.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;

import java.util.*;

/**
 * Test class for EstimatedTimeInterpreter.
 */
public class EstimatedTimeInterpreterTest {

    private Race race;
    private List<Boat> startingList;
    private MessageInterpreter interpreter;
    private MessageBody message;

    @Before
    public void setUp() {
        race = new Race();
        Boat boat1 = new Boat("Big Boat", "BB", 420);
        Boat boat2 = new Boat("Medium Boat", "MB", 100);
        Boat boat3 = new Boat("Small Boat", "SB", 69);
        startingList = new ArrayList<>(Arrays.asList(boat1, boat2, boat3));
        race.setParticipantIds(Arrays.asList(420, 100, 69));
        race.setStartingList(startingList);
        Map<Integer, List> boatStatus = new HashMap<>();
        boatStatus.put(420, new ArrayList(Arrays.asList(2, 2, (long) 0)));
        boatStatus.put(100, new ArrayList(Arrays.asList(2, 2, (long) 10000000)));
        boatStatus.put(69, new ArrayList(Arrays.asList(2, 2, (long) -9999998)));
        message = new AC35RaceStatusMessage(1l, 1, 1l, 1, boatStatus);
        interpreter = new EstimatedTimeInterpreter(race);
    }

    @Test
    public void EstimatedTimeTest() {
        interpreter.interpret(message);
        Boat boatToCheck = race.getStartingList().get(0);
        Assert.assertEquals(0, boatToCheck.getTimeTilNextMark());
        boatToCheck = race.getStartingList().get(1);
        Assert.assertEquals(9999, boatToCheck.getTimeTilNextMark());
        boatToCheck = race.getStartingList().get(2);
        Assert.assertEquals(-9999, boatToCheck.getTimeTilNextMark());
    }

}
