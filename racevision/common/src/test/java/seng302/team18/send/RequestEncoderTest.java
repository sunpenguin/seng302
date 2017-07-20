package seng302.team18.send;

import org.junit.Assert;
import org.junit.Test;
import seng302.team18.message.MessageBody;
import seng302.team18.message.RequestMessage;
import seng302.team18.send.MessageEncoder;
import seng302.team18.util.ByteCheck;
import seng302.team18.util.CRCGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests for RequestEncoder class
 */
public class RequestEncoderTest {

    private RequestMessage requestMessage = new RequestMessage(true);
    private MessageEncoder encoder = new MessageEncoder() {

        @Override
        protected byte[] generateBody(MessageBody message) {
            if (message instanceof RequestMessage) {
                RequestMessage requestMessage = (RequestMessage) message;
                return ByteCheck.intToByteArray(requestMessage.isParticipating() ? 1 : 0);
            }
            return null;
        }


        @Override
        protected byte[] generateChecksum(byte[] head, byte[] body) throws IOException {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            outStream.write(head);
            outStream.write(body);
            byte[] crc = CRCGenerator.generateCRC(outStream.toByteArray());
            return crc;
        }


        @Override
        protected short messageLength() {
            return 4;
        }
    };

    @Test
    public void generateBodyFirstIndex() {
        byte[] actualMessage = encoder.generateBody(requestMessage);
        Assert.assertEquals(1, actualMessage[0]);
    }

    @Test
    public void generateBodyOtherIndex() {
        byte[] actualMessage = encoder.generateBody(requestMessage);
        Assert.assertEquals(0, actualMessage[1]);
    }

    @Test
    public void messageLength() throws Exception {
        short actualLength = encoder.messageLength();
        Assert.assertEquals((short) 4, actualLength);
    }

}