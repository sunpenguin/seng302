package seng302.team18.test_mock.interpret;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.BoatActionMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;

import java.util.Arrays;

/**
 * The tests for BoatActionInterpreter.
 */
public class BoatActionInterpreterTest {

    private Race race;
    private Boat player;
    private Boat notPlayer;
    private MessageInterpreter interpreter;
    private final int boatId = 101;


    @Before
    public void setUp() {
        race = new Race();
        player = new Boat("name", "shortName", boatId, 14.019);
        player.setHeading(0d);
        notPlayer = new Boat("name", "shortName", boatId + 1, 14.019);
        notPlayer.setHeading(0d);
        race.setStartingList(Arrays.asList(player, notPlayer));
        race.getCourse().setWindDirection(270d);
        interpreter = new BoatActionInterpreter(race, boatId);
    }


    @Test
    public void upWindTest() {
        double expected = 42d;
        BoatActionMessage upWind = new BoatActionMessage(boatId);
        upWind.setUpwind(true);
        interpreter.interpret(upWind);
        Assert.assertEquals(expected, player.getHeading(), 0.1);
        Assert.assertEquals(0, notPlayer.getHeading(), 0.1);
    }


    @Test
    public void downWindTest() {
        double expected = 48d;
        BoatActionMessage downWind = new BoatActionMessage(boatId);
        downWind.setDownwind(true);
        interpreter.interpret(downWind);
        Assert.assertEquals(expected, player.getHeading(), 0.1);
        Assert.assertEquals(0, notPlayer.getHeading(), 0.1);
    }


    @Test
    public void sailInTest() {
        BoatActionMessage sailIn = new BoatActionMessage(boatId);
        sailIn.setSailsIn(true);
        interpreter.interpret(sailIn);
        Assert.assertFalse(player.isSailOut());
    }


    @Test
    public void sailOutTest() {
        MessageBody sailOut = new BoatActionMessage(boatId);
        interpreter.interpret(sailOut);
        Assert.assertTrue(player.isSailOut());
    }

}
