package seng302.team18.parse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.ProjectileGoneMessage;

/**
 * Test class for ProjectileGoneParser.
 */
public class ProjectileGoneParserTest {

    private byte[] message;
    private ProjectileGoneMessage expected;
    private MessageBodyParser parser;


    @Before
    public void setUp() {
        parser = new ProjectileGoneParser();
        this.message = new byte[] { 10, 0, 0, 0 };
        expected = new ProjectileGoneMessage(10);
    }


    @Test
    public void parseTest() {
        ProjectileGoneMessage actual = (ProjectileGoneMessage) parser.parse(message);
        Assert.assertEquals(expected.getId(), actual.getId());
    }
}
