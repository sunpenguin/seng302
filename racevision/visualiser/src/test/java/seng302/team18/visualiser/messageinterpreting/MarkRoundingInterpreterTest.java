package seng302.team18.visualiser.messageinterpreting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.AC35MarkRoundingMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Yacht;
import seng302.team18.model.Race;

import java.util.*;

/**
 * Test class for MarkRoundingInterpreter
 */
public class MarkRoundingInterpreterTest {
    private Race race;
    private List<Yacht> startingList;
    private MessageInterpreter interpreter;
    private MessageBody message;
    private Double delta = 0.001;

    @Before
    public void setUp() throws Exception {
        race = new Race();
        Yacht yacht1 = new Yacht("Big Boat", "BB", 420);
        Yacht yacht2 = new Yacht("Medium Boat", "MB", 100);
        Yacht yacht3 = new Yacht("Small Boat", "SB", 69);
        startingList = new ArrayList<>(Arrays.asList(yacht1, yacht2, yacht3));
        race.setParticipantIds(Arrays.asList(420, 100, 69));
        race.setStartingList(startingList);
        interpreter = new MarkRoundingInterpreter(race);
    }

    @Test
    public void interpretEstimatedTime1() throws Exception {
        Yacht yachtToCheck = startingList.get(0);
        yachtToCheck.setTimeAtLastMark(-100l);
        Assert.assertEquals(-100l, yachtToCheck.getTimeAtLastMark(), delta);

        message = new AC35MarkRoundingMessage(420, 25);
        interpreter.interpret(message);
        Assert.assertEquals(25, yachtToCheck.getTimeAtLastMark(), delta);
    }

    @Test
    public void interpretEstimatedTime2() throws Exception {
        Yacht yachtToCheck = startingList.get(1);
        yachtToCheck.setTimeAtLastMark(0l);
        Assert.assertEquals(0l, yachtToCheck.getTimeAtLastMark(), delta);

        message = new AC35MarkRoundingMessage(100, -100);
        interpreter.interpret(message);
        Assert.assertEquals(-100, yachtToCheck.getTimeAtLastMark(), delta);
    }

    @Test
    public void interpretEstimatedTime3() throws Exception {
        Yacht yachtToCheck = startingList.get(2);
        yachtToCheck.setTimeAtLastMark(100l);
        Assert.assertEquals(100l, yachtToCheck.getTimeAtLastMark(), delta);

        message = new AC35MarkRoundingMessage(69, 9999);
        interpreter.interpret(message);
        Assert.assertEquals(9999, yachtToCheck.getTimeAtLastMark(), delta);
    }

}