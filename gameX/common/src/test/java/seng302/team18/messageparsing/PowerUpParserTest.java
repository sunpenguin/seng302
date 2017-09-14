package seng302.team18.messageparsing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.PowerType;
import seng302.team18.message.PowerUpMessage;

/**
 * Test class for PowerUpParser.
 */
public class PowerUpParserTest {

    private byte[] message;
    private PowerUpMessage expected;
    private MessageBodyParser parser;


    @Before
    public void setUp() {
        parser = new PowerUpParser();
        this.message = new byte[] { 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -24, 3, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0 };
        expected = new PowerUpMessage(5, 0, 0, 1, 0, PowerType.SPEED, 10000);
    }


    @Test
    public void parseTest() {
        PowerUpMessage actual = (PowerUpMessage) parser.parse(message);
        Assert.assertEquals(expected, actual);
    }


}
