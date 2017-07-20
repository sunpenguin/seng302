package seng302.team18.send;

import seng302.team18.message.MessageBody;
import seng302.team18.message.RequestMessage;
import seng302.team18.send.MessageEncoder;
import seng302.team18.util.ByteCheck;
import seng302.team18.util.CRCGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Encodes RequestMessages to byte arrays.
 */
public class RequestEncoder extends MessageEncoder {


    /**
     * Constructor for the RequestEncoder.
     */
    public RequestEncoder() {}


    @Override
    protected byte[] generateBody(MessageBody message) {
        if (message instanceof RequestMessage) {
            RequestMessage requestMessage = (RequestMessage) message;
            return ByteCheck.intToByteArray(requestMessage.isParticipating() ? 1 : 0);
        }
        return null;
    }


    @Override
    protected byte[] generateChecksum(byte[] head, byte[] body) throws IOException{
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

}
