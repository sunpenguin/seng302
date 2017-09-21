package seng302.team18.racemodel.generate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.AC35MessageType;

import java.io.IOException;

/**
 * Class to test projectile creation message generator
 */
public class ProjectileCreationMessageGeneratorTest {
    private byte[] expected;
    private MessageGenerator generator;

    @Before
    public void setUp() {
        generator = new ProjectileCreationMessageGenerator(1);
        expected = new byte[] { 1, 0, 0, 0 };
    }



    @Test
    public void payloadTest() throws IOException {
        byte[] actual = generator.getPayload();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void fullMessageTest() {
        byte[] actualMessage = generator.getMessage();
        byte actual = actualMessage[2];
        Assert.assertEquals((byte) AC35MessageType.PROJECTILE_CREATION.getCode(), actual);
    }

}
