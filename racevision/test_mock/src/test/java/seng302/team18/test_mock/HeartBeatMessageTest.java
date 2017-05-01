package seng302.team18.test_mock;

import org.junit.Test;
import seng302.team18.test_mock.connection.HeartBeatMessageGenerator;
import seng302.team18.util.ByteCheck;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Test for heartbeat message generator class.
 */
public class HeartBeatMessageTest {

    /*
    Test the generated sequence num starts from 0 and increment by 1 every time being called.
     */
    @Test
    public void getPayloadTest() throws IOException {
        HeartBeatMessageGenerator heart = new HeartBeatMessageGenerator();
        for (int i = 0; i < 5; i ++) {
            int heartBeat = ByteCheck.byteToIntConverter(heart.getPayload(),0, 1);
            assertEquals(i,heartBeat);
        }
    }

}
