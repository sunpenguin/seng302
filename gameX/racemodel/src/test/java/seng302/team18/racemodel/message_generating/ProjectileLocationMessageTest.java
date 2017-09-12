package seng302.team18.racemodel.message_generating;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.AC35MessageType;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Projectile;
import seng302.team18.model.TigerShark;
import seng302.team18.util.ByteCheck;

import java.io.IOException;
import java.util.Arrays;

/**
 * Class to test projectile location messages
 */
public class ProjectileLocationMessageTest {
    private byte[] expected;
    private Projectile projectile;
    private MessageGenerator generator;

    @Before
    public void setUp() {
        projectile = new TigerShark(1,(new Coordinate(55,55)), 90);
        generator = new ProjectileMessageGenerator(AC35MessageType.PROJECTILE_LOCATION.getCode(), projectile);
        expected = new byte[] {1,0,0,0,  -57,113,28,39,  -57,113,28,39,   0,64,122,100,0,0};
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
        Assert.assertEquals((byte) AC35MessageType.PROJECTILE_LOCATION.getCode(), actual);
    }
}
