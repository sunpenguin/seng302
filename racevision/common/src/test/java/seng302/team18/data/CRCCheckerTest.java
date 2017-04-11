package seng302.team18.data;

import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;

import static org.junit.Assert.assertEquals;

/**
 * Created by jds112 on 10/04/2017.
 */
public class CRCCheckerTest {
    String message;
    byte[] messageBytes;
    byte[] checkSum;
    CRCChecker checker;
    CRC32 crc;

    @Before
    public void setUp() throws Exception {
        checker = new CRCChecker();
        message = "Hello from Jess";
        messageBytes = message.getBytes();
        crc = new CRC32();
        crc.update(messageBytes);
        long checkSumValue = crc.getValue();
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(checkSumValue);
        checkSum = buffer.array();
    }

    @Test
    public void isValid() throws Exception {
        boolean expected = true;
        boolean result = checker.isValid(checkSum, messageBytes);
        assertEquals("The calculated checksum from the isValid method was not correct.", expected, result);
    }

}