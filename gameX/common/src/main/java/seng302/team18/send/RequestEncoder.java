package seng302.team18.send;

import seng302.team18.message.MessageBody;
import seng302.team18.message.RequestMessage;
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


    /**
     * Generates the body for the request message
     *
     * @param message to create the body of the message from.
     * @return returns the message body in the form of a byte array
     */
    @Override
    protected byte[] generateBody(MessageBody message) {
        if (message instanceof RequestMessage) {
            RequestMessage requestMessage = (RequestMessage) message;
            return ByteCheck.intToByteArray(requestMessage.isParticipating() ? 1 : 0);
        }
        return null;
    }


    /**
     * Generates the checksum for the request message
     *
     * @param head of message to create checksum for
     * @param body of message to create checksum for
     * @return the generated checksum as a byte array
     * @throws IOException
     */
    @Override
    protected byte[] generateChecksum(byte[] head, byte[] body) throws IOException{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(head);
        outStream.write(body);
        byte[] crc = CRCGenerator.generateCRC(outStream.toByteArray());
        return crc;
    }


    /**
     * Returns the message length
     *
     * @return the length of the message as a short
     */
    @Override
    protected short messageLength() {
        return 4;
    }

}
