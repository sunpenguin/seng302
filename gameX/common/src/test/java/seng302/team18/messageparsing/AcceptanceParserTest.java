package seng302.team18.messageparsing;

import org.junit.Test;
import seng302.team18.message.AcceptanceMessage;
import static org.junit.Assert.*;


/**
 * Test class to test Acceptance messages are parsed correctly
 */
public class AcceptanceParserTest {

    @Test
    public void parseTest() {
        byte[] bytes = {105, 0, 0, 0};
        MessageBodyParser parser = new AcceptanceParser();
        AcceptanceMessage message = (AcceptanceMessage) parser.parse(bytes);

        int expectedID = 105;
        int actualID = message.getSourceId();

        assertEquals(expectedID, actualID);
    }
}
