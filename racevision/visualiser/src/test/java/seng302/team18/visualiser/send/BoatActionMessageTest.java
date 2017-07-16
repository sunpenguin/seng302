package seng302.team18.visualiser.send;

import org.junit.Test;
import seng302.team18.message.BoatActionMessage;

import static org.junit.Assert.assertEquals;

/**
 * Created by csl62 on 3/07/17.
 */
public class BoatActionMessageTest {

    private BoatActionMessage testBoatActionMessage = new BoatActionMessage(true, true,
            true, true, true);

    @Test
    public void messageTypeTest(){
        assertEquals(testBoatActionMessage.getType(), 100);
    }

    @Test
    public void autoPilotTest(){
        assertEquals(testBoatActionMessage.getType(), 100);
    }

    @Test
    public void sailsInTest(){
        assertEquals(testBoatActionMessage.getType(), 100);
    }

    @Test
    public void sailsOutTest(){
        assertEquals(testBoatActionMessage.getType(), 100);
    }

    @Test
    public void tackGybeTest(){
        assertEquals(testBoatActionMessage.getType(), 100);
    }

    @Test
    public void upwindTest(){
        assertEquals(testBoatActionMessage.getType(), 100);
    }

    @Test
    public void downwindTest(){
        assertEquals(testBoatActionMessage.getType(), 100);
    }
}
