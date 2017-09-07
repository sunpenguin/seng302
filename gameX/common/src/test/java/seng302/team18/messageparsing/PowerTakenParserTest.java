package seng302.team18.messageparsing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.PowerTakenMessage;
import seng302.team18.message.PowerType;
import seng302.team18.message.PowerUpMessage;

/**
 * Created by dhl25 on 5/09/17.
 */
public class PowerTakenParserTest {

    private byte[] message;
    private PowerTakenMessage expected;
    private MessageBodyParser parser;


    @Before
    public void setUp() {
        parser = new PowerTakenParser();
        this.message = new byte[] { 1, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0 };
        expected = new PowerTakenMessage(1, 2, 3000);
    }


    @Test
    public void parseTest() {
        PowerTakenMessage actual = (PowerTakenMessage) parser.parse(message);
        Assert.assertEquals(expected, actual);
    }


}
