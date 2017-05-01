package seng302.team18.data;

import org.junit.Before;
import org.junit.Test;
import seng302.team18.messageparsing.CRCChecker;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.Assert.assertEquals;

/**
 * Created by jds112 on 10/04/2017.
 */
public class CRCCheckerTest {
//    String message;
//    byte[] messageBytes;
//    byte[] checkSum;
//    byte[] wrongCheckSum;
//    private CRCChecker checker;
//
//    @Before
//    public void setUp() throws Exception {
//        checker = new CRCChecker();
//        message = "Hello from Jess";
//        int checkInt = 0x800EFB03;
//        checkSum = Integer.valueOf(0x800EFB03).toByteArray(); // calculated checksum which is known to be correct
//        ByteBuffer b = ByteBuffer.allocate(4);
//        b.putInt(checkInt);
        //b.order(ByteOrder.LITTLE_ENDIAN);
//        checkSum = b.array();
//        System.out.println(checkInt);
//        b.clear();
//        b.allocate(8);
//        b.put(checkSum);
//        System.out.println(b.getInt());

//        messageBytes = message.getBytes();
//        wrongCheckSum = BigInteger.valueOf(0x800EF01).toByteArray(); // random checksum which is known to be incorrect
//        ByteBuffer bb = ByteBuffer.allocate(messageBytes.length);
//        bb.put(messageBytes);
//        bb.order(ByteOrder.LITTLE_ENDIAN);
//
//    }
//
//    @Test
//    public void isValid() throws Exception {
//        boolean expected = true;
//        boolean result = checker.isValid(checkSum, messageBytes);
//        assertEquals("The calculated checksum from the isValid method was not correct.", expected, result);
//        expected = false;
//        result = checker.isValid(wrongCheckSum, messageBytes);
//        assertEquals("The calculated checksum from the isValid method was not correct.", expected, !result);
//    }

}