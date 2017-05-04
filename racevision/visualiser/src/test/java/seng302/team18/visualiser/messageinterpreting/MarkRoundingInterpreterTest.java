package seng302.team18.visualiser.messageinterpreting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.messageparsing.AC35MarkRoundingMessage;
import seng302.team18.messageparsing.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;

import java.util.*;

/**
 * Test class for MarkRoundingInterpreter
 */
public class MarkRoundingInterpreterTest {
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
        interpreter = new MarkRoundingInterpreter(race);
    }

    @Test
    public void interpretEstimatedTime1() throws Exception {
        Boat boatToCheck = startingList.get(0);
        boatToCheck.setTimeAtLastMark(-100l);
        Assert.assertEquals(-100l, boatToCheck.getTimeAtLastMark(), delta);

        message = new AC35MarkRoundingMessage(420, 25);
        interpreter.interpret(message);
        Assert.assertEquals(25, boatToCheck.getTimeAtLastMark(), delta);
    }

    @Test
    public void interpretEstimatedTime2() throws Exception {
        Boat boatToCheck = startingList.get(1);
        boatToCheck.setTimeAtLastMark(0l);
        Assert.assertEquals(0l, boatToCheck.getTimeAtLastMark(), delta);

        message = new AC35MarkRoundingMessage(100, -100);
        interpreter.interpret(message);
        Assert.assertEquals(-100, boatToCheck.getTimeAtLastMark(), delta);
    }

    @Test
    public void interpretEstimatedTime3() throws Exception {
        Boat boatToCheck = startingList.get(2);
        boatToCheck.setTimeAtLastMark(100l);
        Assert.assertEquals(100l, boatToCheck.getTimeAtLastMark(), delta);

        message = new AC35MarkRoundingMessage(69, 9999);
        interpreter.interpret(message);
        Assert.assertEquals(9999, boatToCheck.getTimeAtLastMark(), delta);
    }

}