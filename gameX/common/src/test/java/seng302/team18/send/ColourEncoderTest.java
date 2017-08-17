package seng302.team18.send;

import javafx.scene.paint.Color;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.ColourMessage;
import seng302.team18.message.MessageBody;

import java.io.IOException;

/**
 * Created by dhl25 on 17/08/17.
 */
public class ColourEncoderTest {


    private ColourEncoder encoder;
    private MessageBody message;


    @Before
    public void setUp() {
        encoder = new ColourEncoder();
        message = new ColourMessage(Color.ALICEBLUE, 102);
    }

    @Test
    public void generateBodyTest() throws IOException {
        byte[] expected = { 102, 0, 0, 0, -16, -8, -1 };
        Assert.assertArrayEquals(expected, encoder.generateBody(message));
    }


    @Test
    public void messageLengthTest() {
        int expected = 7;
        Assert.assertEquals(expected, encoder.messageLength());
    }
}
