package seng302.team18.util;

import org.junit.Assert;
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
        int actual1 = ByteCheck.ByteToIntConverter(b1, 0, 3);
        int expected1 = 65793; // 2^16 + 2^8 + 1
        int actual2 = ByteCheck.ByteToIntConverter(b2, 0, 1);
        int expected2 = 9; // 9 * 1
        int actual3 = ByteCheck.ByteToIntConverter(b3, 5, 3);
        int expected3 = 460035; // 2^16 * 7 + 2^8 * 5 + 1 * 3
        int actual4 = ByteCheck.ByteToIntConverter(b1, 0, 0);
        int expected4 = 0; // Length 0

        Assert.assertEquals(expected1, actual1);
        Assert.assertEquals(expected2, actual2);
        Assert.assertEquals(expected3, actual3);
        Assert.assertEquals(expected4, actual4);
    }
}
