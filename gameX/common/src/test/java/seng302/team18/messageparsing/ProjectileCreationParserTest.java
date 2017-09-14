package seng302.team18.messageparsing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.ProjectileCreationMessage;

/**
 * Class to test projectile creation parser
 */
public class ProjectileCreationParserTest {

    private byte[] message;
    private ProjectileCreationMessage expected;
    private MessageBodyParser parser;


    @Before
    public void setUp() {
        parser = new ProjectileCreationParser();
        this.message = new byte[] { 10, 0, 0, 0 };
        expected = new ProjectileCreationMessage(10);
    }


    @Test
    public void parseTest() {
        ProjectileCreationMessage actual = (ProjectileCreationMessage) parser.parse(message);
        Assert.assertEquals(expected.getProjectile_id(), actual.getProjectile_id());
    }
}
