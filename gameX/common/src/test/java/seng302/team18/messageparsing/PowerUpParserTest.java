package seng302.team18.messageparsing;

import com.google.common.io.ByteStreams;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.MessageBody;
import seng302.team18.message.PowerType;
import seng302.team18.message.PowerUpMessage;
import seng302.team18.model.*;
import seng302.team18.util.ByteCheck;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by dhl25 on 5/09/17.
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
