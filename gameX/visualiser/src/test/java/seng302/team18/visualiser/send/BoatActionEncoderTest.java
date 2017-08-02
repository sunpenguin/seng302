package seng302.team18.visualiser.send;

import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.BoatActionMessage;
import seng302.team18.util.ByteCheck;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Test for BoatAction message encoder.
 */
public class BoatActionEncoderTest {

    private BoatActionMessage messageBody;
    private BoatActionEncoder encoder;
    private byte[] encodedByte;


    /**
     * Set up a BoatAction message to be encoded.
     */
    @Before
    public void setUp() {
        int id = 341;

        messageBody = new BoatActionMessage(id);
        encoder = new BoatActionEncoder();
    }


    /**
     * Test to see whether the id field is the same if it is encoded correctly.
     */
    @Test
    public void generateBodySourceIdTest() throws IOException {
        final int ID_INDEX = 0;
        final int ID_LENGTH = 4;
        encodedByte = encoder.generateBody(messageBody);
        int actualID = ByteCheck.byteToInt(encodedByte, ID_INDEX, ID_LENGTH);
        int expectedID = 341;

        assertEquals(expectedID, actualID);
    }


    /**
     * Test to see when auto pilot is active, the status of action is encoded correctly.
     */
    @Test
    public void generateBodyAutoPilotTest() throws IOException {
        messageBody.setAutoPilot();
        encodedByte = encoder.generateBody(messageBody);

        assertEquals(1, encodedByte[4]);
    }


    /**
     * Test to see when sail in is active, the status of action is encoded correctly.
     */
    @Test
    public void generateBodySailInTest() throws IOException {
        messageBody.setSailIn();
        encodedByte = encoder.generateBody(messageBody);

        assertEquals(2, encodedByte[4]);
    }


    /**
     * Test to see when sail in  is not active (i.e. sail out), the status of action is encoded correctly.
     */
    @Test
    public void generateBodySailOutTest() throws IOException {
        messageBody.setSailOut();
        encodedByte = encoder.generateBody(messageBody);

        assertEquals(3, encodedByte[4]);
    }


    /**
     * Test to see when tack/gybe is active, the status of action is encoded correctly.
     */
    @Test
    public void generateBodyTackGybeTest() throws IOException {
        messageBody.setTackGybe();
        encodedByte = encoder.generateBody(messageBody);

        assertEquals(4, encodedByte[4]);
    }


    /**
     * Test to see when up wind is active, the status of action is encoded correctly.
     */
    @Test
    public void generateBodyUpWindTest() throws IOException {
        messageBody.setUpwind();
        encodedByte = encoder.generateBody(messageBody);

        assertEquals(5, encodedByte[4]);
    }


    /**
     * Test to see when down wind is active, the status of action is encoded correctly.
     */
    @Test
    public void generateBodyDownWindTest() throws IOException {
        messageBody.setDownwind();
        encodedByte = encoder.generateBody(messageBody);

        assertEquals(6, encodedByte[4]);
    }
}
