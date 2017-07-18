package seng302.team18.visualiser.send;

import org.junit.Test;
import seng302.team18.message.BoatActionMessage;

import static org.junit.Assert.assertEquals;

/**
 * The test class for BoatActionMessage.
 */
public class BoatActionMessageTest {

    private BoatActionMessage testBoatActionMessage = new BoatActionMessage(true, true,
            true, true, false);

    @Test
    public void messageTypeTest(){
        assertEquals(100, testBoatActionMessage.getType());
    }

    @Test
    public void autoPilotTest(){
        assertEquals(true, testBoatActionMessage.isAutopilot());
    }

    @Test
    public void sailsInTest(){
        assertEquals(true, testBoatActionMessage.isSailsIn());
    }


    @Test
    public void tackGybeTest(){
        assertEquals(true, testBoatActionMessage.isTackGybe());
    }

    @Test
    public void upwindTest(){
        assertEquals(true, testBoatActionMessage.isUpwind());
    }

    @Test
    public void downwindTest(){
        assertEquals(false, testBoatActionMessage.isDownwind());
    }
}
