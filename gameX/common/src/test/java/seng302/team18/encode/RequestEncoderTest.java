package seng302.team18.encode;

import org.junit.Assert;
import org.junit.Test;
import seng302.team18.message.RequestMessage;
import seng302.team18.message.RequestType;

import java.io.IOException;

/**
 * Tests for RequestEncoder class
 */
public class RequestEncoderTest {

    private RequestMessage requestMessage = new RequestMessage(RequestType.RACING);
    private MessageEncoder encoder = new RequestEncoder();

    @Test
    public void generateBodyFirstIndex() throws IOException {
        byte[] actualMessage = encoder.generateBody(requestMessage);
        Assert.assertEquals(1, actualMessage[0]);
    }

    @Test
    public void messageLength() throws Exception {
        short actualLength = encoder.messageLength();
        Assert.assertEquals((short) 1, actualLength);
    }

}