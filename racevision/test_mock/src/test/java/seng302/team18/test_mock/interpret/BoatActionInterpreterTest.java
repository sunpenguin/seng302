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
 * Created by dhl25 on 7/07/17.
 */
public class BoatActionInterpreterTest {

    private Race race;
    private Boat player;
    private Boat notPlayer;
    private MessageInterpreter interpreter;


    @Before
    public void setUp() {
        final int boatId = 101;
        race = new Race();
        player = new Boat("name", "shortName", boatId);
        player.setHeading(0d);
        notPlayer = new Boat("name", "shortName", boatId + 1);
        notPlayer.setHeading(0d);
        race.setStartingList(Arrays.asList(player, notPlayer));
        race.getCourse().setWindDirection(270d);
        interpreter = new BoatActionInterpreter(race, boatId);
    }


    @Test
    public void upWindTest() {
        double expected = 357d;
        MessageBody upWind = new BoatActionMessage(false, false, false, false, true, false);
        interpreter.interpret(upWind);
        Assert.assertEquals(expected, player.getHeading(), 0.1);
        Assert.assertEquals(0, notPlayer.getHeading(), 0.1);
    }


    @Test
    public void downWindTest() {
        double expected = 3d;
        MessageBody downWind = new BoatActionMessage(false, false, false, false, false, true);
        interpreter.interpret(downWind);
        Assert.assertEquals(expected, player.getHeading(), 0.1);
        Assert.assertEquals(0, notPlayer.getHeading(), 0.1);
    }
}
