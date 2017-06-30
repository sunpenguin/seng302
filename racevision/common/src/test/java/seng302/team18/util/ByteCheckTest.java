package seng302.team18.util;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class for ByteCheck.
 * @author Sunguin Peng
 */
public class ByteCheckTest {

    private byte[] b1;
    private byte[] b2;
    private byte[] b3;

    @Before
    public void setUp() {
        // Set b1
        b1 = new byte[4];
        b1[0] = 1;
        b1[1] = 1;
        b1[2] = 1;
        b1[3] = 1;

        // Set b2
        b2 = new byte[1];
        b2[0] = 9;

        // Set b3
        b3 = new byte[8];
        b3[0] = 2;
        b3[1] = 4;
        b3[2] = 6;
        b3[3] = 8;
        b3[4] = 1;
        b3[5] = 3;
        b3[6] = 5;
        b3[7] = 7;
    }

    @Test
    public void ByteToIntConverterTest() {
        int actual1 = ByteCheck.byteToInt(b1, 0, 3);
        int expected1 = 65793; // 2^16 + 2^8 + 1
        int actual2 = ByteCheck.byteToInt(b2, 0, 1);
        int expected2 = 9; // 9 * 1
        int actual3 = ByteCheck.byteToInt(b3, 5, 3);
        int expected3 = 460035; // 2^16 * 7 + 2^8 * 5 + 1 * 3
        int actual4 = ByteCheck.byteToInt(b1, 0, 0);
        int expected4 = 0; // Length 0

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
        assertEquals(expected3, actual3);
        assertEquals(expected4, actual4);
    }

    @Test
    public void ByteToLongConverterTest() {
        long actual1 = ByteCheck.byteToLong(b1, 0, 4);
        long expected1 = 16843009; // 2^24 + 2^16 + 2^8 + 1
        long actual2 = ByteCheck.byteToLong(b2, 0, 1);
        long expected2 = 9; // 9 * 1
        long actual3 = ByteCheck.byteToLong(b3, 1, 6);
        long expected3 = 5510460343812L; // (2^40 * 5) + (2^32 * 3) + 2^24 * 1 + (2^16 * 8) + (2^8 * 6) + 1 * 4
        long actual4 = ByteCheck.byteToLong(b1, 0, 0);
        long expected4 = 0; // Length 0

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
        assertEquals(expected3, actual3);
        assertEquals(expected4, actual4);
    }

    @Test
    public void intToByteTest() {
        int expectedInt1 = 46576;
        byte[] bytes1 = ByteCheck.intToByteArray(expectedInt1);
        int actualInt1 = ByteCheck.byteToInt(bytes1, 0, 4);

        int expectedInt2 = 6;
        byte[] bytes2 = ByteCheck.intToByteArray(expectedInt2);
        int actualInt2 = ByteCheck.byteToInt(bytes2, 0, 4);

        int expectedInt3 = 1265541335;
        byte[] bytes3 = ByteCheck.intToByteArray(expectedInt3);
        int actualInt3 = ByteCheck.byteToInt(bytes3, 0, 4);

        assertEquals(expectedInt1, actualInt1);
        assertEquals(expectedInt2, actualInt2);
        assertEquals(expectedInt3, actualInt3);
    }

    @Test
    public void shortToByteTest() {
        short expectedShort1 = 4;
        byte[] bytes1 = ByteCheck.shortToByteArray(expectedShort1);
        int acutualShort1 = ByteCheck.byteToInt(bytes1, 0, 2);

        short expectedShort2 = 16000;
        byte[] bytes2 = ByteCheck.shortToByteArray(expectedShort2);
        int acutualShort2 = ByteCheck.byteToInt(bytes2, 0, 2);

        short expectedShort3 = 6587;
        byte[] bytes3 = ByteCheck.shortToByteArray(expectedShort3);
        int acutualShort3 = ByteCheck.byteToInt(bytes3, 0, 2);

        assertEquals(expectedShort1, acutualShort1);
        assertEquals(expectedShort2, acutualShort2);
        assertEquals(expectedShort3, acutualShort3);
    }
}
