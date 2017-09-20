package seng302.team18.encode;

import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.BoatActionMessage;

import static org.junit.Assert.assertEquals;

/**
 * The test class for BoatActionMessage.
 */
public class BoatActionMessageTest {

    private BoatActionMessage testBoatActionMessage;


    @Before
    public void setUp() {
        testBoatActionMessage = new BoatActionMessage(555);
    }


    @Test
    public void messageTypeTest(){
        assertEquals(100, testBoatActionMessage.getType());
    }


    @Test
    public void autoPilotTest(){
        testBoatActionMessage.setAutoPilot();
        assertEquals(1, testBoatActionMessage.getAction());
    }


    @Test
    public void sailsInTest(){
        testBoatActionMessage.setSailIn();
        assertEquals(2, testBoatActionMessage.getAction());
    }


    @Test
    public void tackGybeTest(){
        testBoatActionMessage.setTackGybe();
        assertEquals(4, testBoatActionMessage.getAction());
    }


    @Test
    public void upwindTest(){
        testBoatActionMessage.setUpwind();
        assertEquals(5, testBoatActionMessage.getAction());
    }


    @Test
    public void downwindTest(){
        testBoatActionMessage.setDownwind();
        assertEquals(6, testBoatActionMessage.getAction());
    }
}
