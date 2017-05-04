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
 * Test class for FinishersListInterpreter.
 */
public class FinishersListInterpreterTest {
    private Race race;
    private List<Boat> startingList;
    private MessageInterpreter interpreter;
    private MessageBody message;
    private Double delta = 0.001;

    @Before
    public void setUp() throws Exception {
        race = new Race();
        Boat boat1 = new Boat("Big Boat", "BB", 420);
        Boat boat2 = new Boat("Medium Boat", "MB", 100);
        Boat boat3 = new Boat("Small Boat", "SB", 69);
        startingList = new ArrayList<>(Arrays.asList(boat1, boat2, boat3));
        race.setParticipantIds(Arrays.asList(420, 100, 69));
        race.setStartingList(startingList);
        interpreter = new FinishersListInterpreter(race);
    }

    @Test
    public void interpretFinishersList1() throws Exception {
        Map<Integer, List> boatStatus = new HashMap<>();
        boatStatus.put(420, new ArrayList(Arrays.asList(3, 2, 0)));
        boatStatus.put(100, new ArrayList(Arrays.asList(2, 2, 0)));
        boatStatus.put(69, new ArrayList(Arrays.asList(3, 2, 0)));
        message = new AC35RaceStatusMessage(1l, 1, 1l, 1, boatStatus);
        interpreter.interpret(message);
        Assert.assertEquals(2, race.getFinishedList().size());
    }

    @Test
    public void interpretFinishersList2() throws Exception {
        Map<Integer, List> boatStatus = new HashMap<>();
        boatStatus.put(420, new ArrayList(Arrays.asList(2, 2, 0)));
        boatStatus.put(100, new ArrayList(Arrays.asList(2, 2, 0)));
        boatStatus.put(69, new ArrayList(Arrays.asList(2, 2, 0)));
        message = new AC35RaceStatusMessage(1l, 1, 1l, 1, boatStatus);
        interpreter.interpret(message);
        Assert.assertEquals(0, race.getFinishedList().size());
    }

    @Test
    public void interpretFinishersList3() throws Exception {
        Map<Integer, List> boatStatus = new HashMap<>();
        boatStatus.put(420, new ArrayList(Arrays.asList(2, 2, 0)));
        boatStatus.put(100, new ArrayList(Arrays.asList(2, 2, 0)));
        boatStatus.put(69, new ArrayList(Arrays.asList(3, 2, 0)));
        message = new AC35RaceStatusMessage(1l, 1, 1l, 1, boatStatus);
        interpreter.interpret(message);
        Assert.assertEquals(1, race.getFinishedList().size());
        Boat boatToCheck = race.getFinishedList().get(0);
        Assert.assertEquals(69, boatToCheck.getId(), delta);
    }
}