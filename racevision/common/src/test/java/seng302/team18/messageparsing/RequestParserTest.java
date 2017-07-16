package seng302.team18.messageparsing;

import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.MessageBody;
import seng302.team18.util.ByteCheck;

import static org.junit.Assert.assertEquals;

/**
 * Test for Request message parser.
 */
public class RequestParserTest {

    byte[] requestByte;
    private RequestParser parser;
    private MessageBody requestMessage;


    /**
     * Set up byte to be parsed.
     */
    @Before
    public void setUp() {
        requestByte = ByteCheck.intToByteArray(1);
        parser = new RequestParser();
    }


    /**
     * Test to see if message being parsed correctly.
     */
    @Test
    public void parseTest() {
        requestMessage = parser.parse(requestByte);
        int expectedType = 55;
        int actualType = requestMessage.getType();

        assertEquals(expectedType, actualType);
    }
}
