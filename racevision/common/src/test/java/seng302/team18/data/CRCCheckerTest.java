package seng302.team18.data;

import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;

import static com.sun.xml.internal.ws.dump.LoggingDumpTube.Position.Before;
import static org.junit.Assert.assertEquals;

/**
 * Created by jds112 on 10/04/2017.
 */
public class CRCCheckerTest {
    String message;
    byte[] messageBytes;
    byte[] checkSum;
    byte[] wrongCheckSum;
    CRCChecker checker;

    @Before
    public void setUp() throws Exception {
        checker = new CRCChecker();
        message = "Hello from Jess";
        checkSum = BigInteger.valueOf(0x514DAC35).toByteArray(); // calculated checksum which is known to be correct
        wrongCheckSum = BigInteger.valueOf(0x800EF01).toByteArray(); // random checksum which is known to be incorrect
        messageBytes = message.getBytes();
    }

    @Test
    public void isValid() throws Exception {
        boolean expected = true;
        boolean result = checker.isValid(checkSum, messageBytes);
        assertEquals("The calculated checksum from the isValid method was not correct.", expected, result);
        expected = false;
        result = checker.isValid(wrongCheckSum, messageBytes);
        assertEquals("The calculated checksum from the isValid method was not correct.", expected, result);
    }

}