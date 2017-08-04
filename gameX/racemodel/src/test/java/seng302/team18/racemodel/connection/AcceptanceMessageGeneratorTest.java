package seng302.team18.racemodel.connection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by dhl25 on 7/07/17.
 */
public class AcceptanceMessageGeneratorTest {

    private byte[] message;

    @Before
    public void setUp() throws IOException {
        AcceptanceMessageGenerator generator = new AcceptanceMessageGenerator(12);
        message = generator.getPayload();
    }


    @Test
    public void getMessageTest() {
        byte[] expected = {12, 0, 0, 0};
        Assert.assertArrayEquals(expected, message);
    }
}
