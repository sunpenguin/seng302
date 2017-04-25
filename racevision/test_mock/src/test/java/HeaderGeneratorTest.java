import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import seng302.team18.test_mock.connection.HeaderGenerator;
import seng302.team18.util.ByteCheck;

import java.io.IOException;
import java.sql.Timestamp;

/**
 * Created by jth102 on 25/04/17.
 */
public class HeaderGeneratorTest {

    final int SYNC_1_POS = 0;
    final int SYNC_1_LEN = 1;

    final int SYNC_2_POS = 1;
    final int SYNC_2_LEN = 1;

    final int MSG_TYPE_POS = 2;
    final int MSG_TYPE_LEN = 1;

    final int TIME_POS = 3;
    final int TIME_LEN = 6;

    final int SOURCE_ID_POS = 9;
    final int SOURCE_ID_LEN = 4;

    final int MSG_LEN_POS = 13;
    final int MSG_LEN_LEN = 2;

    @Before
    public void setup() {
    }

    @Test
    public void headerTest() throws IOException {
        short msgLen = 0;
        int type = 26;
        byte[] headerBytes = HeaderGenerator.generateHeader(type, msgLen);

        int expectedSync1 = 0x47;
        int expectedSync2 = 0x83;
        int expectedMsgType = 26;

        long expectedTime = System.currentTimeMillis();
        int expectedSourceID = 1568366138; // Get real value
        int expectedMsgLen = 0;

        int actualSync1 = ByteCheck.byteToIntConverter(headerBytes, SYNC_1_POS, SYNC_1_LEN);
        int actualSync2 = ByteCheck.byteToIntConverter(headerBytes, SYNC_2_POS, SYNC_2_LEN);
        int actualMsgType = ByteCheck.byteToIntConverter(headerBytes, MSG_TYPE_POS, MSG_TYPE_LEN);
        long actualTime = ByteCheck.byteToLongConverter(headerBytes, TIME_POS, TIME_LEN);
        int actualSourceID = ByteCheck.byteToIntConverter(headerBytes, SOURCE_ID_POS, SOURCE_ID_LEN);
        int actualMsgLen = ByteCheck.byteToIntConverter(headerBytes, MSG_LEN_POS, MSG_LEN_LEN);

        assertEquals(expectedSync1, actualSync1);
        assertEquals(expectedSync2, actualSync2);
        assertEquals(expectedMsgType, actualMsgType);
        assertEquals(expectedTime, actualTime, 10);
        assertEquals(expectedSourceID, actualSourceID);
        assertEquals(expectedMsgLen, actualMsgLen);
    }
}
