package seng302.team18.visualiser.send;

import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.BoatActionMessage;

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
        boolean autopilot = false;
        boolean sailsIn = false;
        boolean tackGybe = false;
        boolean upwind = false;
        boolean downwind = false;

        messageBody = new BoatActionMessage(autopilot, sailsIn, tackGybe, upwind, downwind);
        encoder = new BoatActionEncoder();
    }


    /**
     * Test to see when auto pilot is active, the status of action is encoded correctly.
     */
    @Test
    public void generateBodyAutoPilotTest() {
        messageBody.setAutopilot(true);
        encodedByte = encoder.generateBody(messageBody);

        assertEquals(1, encodedByte[0]);
    }


    /**
     * Test to see when sail in is active, the status of action is encoded correctly.
     */
    @Test
    public void generateBodySailInTest() {
        messageBody.setSailsIn(true);
        encodedByte = encoder.generateBody(messageBody);

        assertEquals(2, encodedByte[0]);
    }


    /**
     * Test to see when sail in  is not active (i.e. sail out), the status of action is encoded correctly.
     */
    @Test
    public void generateBodySailOutTest() {
        encodedByte = encoder.generateBody(messageBody);

        assertEquals(3, encodedByte[0]);
    }


    /**
     * Test to see when tack/gybe is active, the status of action is encoded correctly.
     */
    @Test
    public void generateBodyTackGybeTest() {
        messageBody.setTackGybe(true);
        encodedByte = encoder.generateBody(messageBody);

        assertEquals(4, encodedByte[0]);
    }


    /**
     * Test to see when up wind is active, the status of action is encoded correctly.
     */
    @Test
    public void generateBodyUpWindTest() {
        messageBody.setUpwind(true);
        encodedByte = encoder.generateBody(messageBody);

        assertEquals(5, encodedByte[0]);
    }


    /**
     * Test to see when down wind is active, the status of action is encoded correctly.
     */
    @Test
    public void generateBodyDownWindTest() {
        messageBody.setDownwind(true);
        encodedByte = encoder.generateBody(messageBody);

        assertEquals(6, encodedByte[0]);
    }
}
