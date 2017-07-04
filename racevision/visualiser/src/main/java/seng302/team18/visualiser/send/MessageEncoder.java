package seng302.team18.visualiser.send;

import seng302.team18.message.MessageBody;
import seng302.team18.message.RequestMessage;
import seng302.team18.util.ByteCheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by David-chan on 2/07/17.
 */
public abstract class MessageEncoder {


    // TODO write javadoc
    public byte[] compose(MessageBody message) throws IOException {
        byte[] head = generateHead(message, messageLength());
        byte[] body = generateBody(message);
        byte[] checksum = generateChecksum(head, body);

        byte[] combined = new byte[head.length + body.length + checksum.length];
        System.arraycopy(head, 0, combined, 0, head.length);
        System.arraycopy(body, 0, combined, head.length, body.length);
        System.arraycopy(checksum, 0, combined, head.length + body.length, checksum.length);

        return combined;
    }

    private byte[] generateHead(MessageBody message, short lengthOfMessage) throws IOException {

        if (message instanceof RequestMessage) {
            RequestMessage requestMessage = (RequestMessage) message;
            byte syncByte1 = 0x47;
            byte syncByte2 = (byte) 0x83;
            byte messageType = (byte) requestMessage.getType();
            byte[] timestampBytes = ByteCheck.getCurrentTime6Bytes();

            //TODO just like HeaderGenerate from test_mock, there needs to be a better way to generate source id
            byte[] sourceID = new byte[4];
            sourceID[0] = 11;
            sourceID[1] = 22;
            sourceID[2] = 33;
            sourceID[4] = 99;

            byte[] messageLen = ByteCheck.shortToByteArray(lengthOfMessage);

            ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();
            outputSteam.write(syncByte1);
            outputSteam.write(syncByte2);
            outputSteam.write(messageType);
            outputSteam.write(timestampBytes);
            outputSteam.write(sourceID);
            outputSteam.write(messageLen);

            return outputSteam.toByteArray();
        }

        return null;
    }

    protected abstract byte[] generateBody(MessageBody message);

    protected abstract byte[] generateChecksum(byte[] head, byte[] body) throws IOException;

    abstract protected short messageLength();
}
