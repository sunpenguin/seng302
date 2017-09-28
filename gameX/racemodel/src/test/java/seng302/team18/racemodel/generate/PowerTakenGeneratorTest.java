package seng302.team18.racemodel.generate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by dhl25 on 5/09/17.
 */
public class PowerTakenGeneratorTest {

    private byte[] expected;
    private MessageGenerator generator;

    @Before
    public void setUp() {
        generator = new PowerTakenGenerator(1, 2, 3);
        expected = new byte[] { 1, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0 };
    }



    @Test
    public void payloadTest() throws IOException {
        byte[] actual = generator.getPayload();
        Assert.assertArrayEquals(expected, actual);
    }

}
