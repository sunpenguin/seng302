package seng302.team18.visualiser.messageinterpreting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AC35BoatStatusMessage;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.model.BoatStatus;
import seng302.team18.message.MessageBody;
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
        Boat boat1 = new Boat("Big Boat", "BB", 420, 1);
        Boat boat2 = new Boat("Medium Boat", "MB", 100, 1);
        Boat boat3 = new Boat("Small Boat", "SB", 69, 1);
        startingList = new ArrayList<>(Arrays.asList(boat1, boat2, boat3));
        race.setParticipantIds(Arrays.asList(420, 100, 69));
        race.setStartingList(startingList);
//        Map<Integer, List> boatStatus = new HashMap<>();
//        boatStatus.put(420, new ArrayList(Arrays.asList(2, 2, 0L)));
//        boatStatus.put(100, new ArrayList(Arrays.asList(2, 2, 10000000L)));
//        boatStatus.put(69, new ArrayList(Arrays.asList(2, 2, -9999998L)));
        List<AC35BoatStatusMessage> boatStates = new ArrayList<>();
        boatStates.add(new AC35BoatStatusMessage(420, 2, BoatStatus.RACING, 0));
        boatStates.add(new AC35BoatStatusMessage(100, 2, BoatStatus.RACING, 10000000));
        boatStates.add(new AC35BoatStatusMessage(69, 2, BoatStatus.RACING, -9999998L));
        message = new AC35RaceStatusMessage(1l, 1, 1l, 1, 0, boatStates);
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
