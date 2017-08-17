package seng302.team18.send;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.MessageBody;
import seng302.team18.message.RequestMessage;
import seng302.team18.message.RequestType;

import java.io.IOException;

/**
 * Created by dhl25 on 16/07/17.
 */
public class MessageEncoderTest {

    /**
     * Class for testing abstract methods.
     */
    private class TestClass extends MessageEncoder {

        @Override
        protected byte[] generateBody(MessageBody message) {
            return new byte[0];
        }

        @Override
        protected short messageLength() {
            return 0;
        }
    }


    private byte[] head;
    private byte[] checksum;


    @Before
    public void setUp() throws IOException {
        MessageEncoder encoder = new TestClass();
        MessageBody request = new RequestMessage(RequestType.VIEWING);
        head = encoder.generateHead(request);
        checksum = encoder.generateChecksum(head, encoder.generateChecksum(head, encoder.generateBody(request)));
    }


    /**
     * In this test we ignore the 6 bytes used as a time stamp.
     */
    @Test
    public void generateHeadTest() {
        byte[] expected1 = { 71, -125, 55, };
        byte[] expected2 = {11, 22, 33, 99, 0, 0 };
        int expectedLength = 15;
        byte[] head1 = new byte[3];
        byte[] head2 = new byte[6];
        System.arraycopy(head, 0, head1, 0, head1.length);
        System.arraycopy(head, 6 + head1.length, head2, 0, head2.length);

        Assert.assertEquals(expectedLength, head.length);
        Assert.assertArrayEquals(expected1, head1);
        Assert.assertArrayEquals(expected2, head2);
    }


    @Test
    public void generateChecksumTest() {
        byte[] expected = { 28, -33, 68, 33 };

        Assert.assertArrayEquals(expected, checksum);
    }

}
