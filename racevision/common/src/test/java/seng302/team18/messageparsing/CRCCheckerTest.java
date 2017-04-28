package seng302.team18.messageparsing;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by jds112 on 10/04/2017.
 */
public class CRCCheckerTest {
    byte[] messageBytes;
    byte[] headerBytes;
    byte[] checkSum;
    byte[] wrongCheckSum;
    CRCChecker checker;

    @Before
    public void setUp() throws Exception {
        byte[] bytes = {1, 11, 3, 10, -50, -53, 59, 121, 79, 1, 0, 0, 0, 0, 78, -91, -16, 21, -52, 33, -125, 107, 85, 11, -50, -53, 59, 121, 79, 1, 0, 0, 0, 0, -37, -92, 126, 22, -50, 33, -19, 107, 85, 41, -50, -53, 59, 121, 79, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5};
        byte[] head = {71, -125, 44, -94, 35, 93, 28, 83, 1, -54, 0, 0, 0, 63, 0};
        byte[] crc = {-87, -119, 34, 62};
        byte[] wrong = {-87, 0, 34, 40};
        messageBytes = bytes;
        headerBytes = head;
        checkSum = crc;
        wrongCheckSum = wrong;
        checker = new CRCChecker();
    }

    @Test
    public void isValid() throws Exception {
        boolean expected = true;
        boolean result = checker.isValid(checkSum, messageBytes, headerBytes);
        assertEquals("The calculated checksum from the isValid method was not correct.", expected, result);
        expected = false;
        result = checker.isValid(wrongCheckSum, messageBytes, headerBytes);
        assertEquals("The calculated checksum from the isValid method was not correct.", expected, result);
    }

}