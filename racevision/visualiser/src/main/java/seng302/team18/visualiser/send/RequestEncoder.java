package seng302.team18.visualiser.send;

import seng302.team18.message.MessageBody;
import seng302.team18.message.RequestMessage;
import seng302.team18.util.ByteCheck;

/**
 * Encodes RequestMessages to byte arrays.
 */
public class RequestEncoder extends MessageEncoder {

    private byte[] body;

    /**
     * Constructor for the RequestEncoder.
     */
    public RequestEncoder() {}


    @Override
    protected byte[] generateBody(MessageBody message) {
        if (message instanceof RequestMessage) {
            RequestMessage requestMessage = (RequestMessage) message;
            body = ByteCheck.intToByteArray(requestMessage.getRequestType());
            return body;
        }
        return null;
    }


    @Override
    protected byte[] generateChecksum(MessageBody message) {
        return new byte[4];
    }


    @Override
    protected short messageLength() {
        return 4;
    }

}
