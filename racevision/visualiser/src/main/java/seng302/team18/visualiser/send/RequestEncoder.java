package seng302.team18.visualiser.send;

import seng302.team18.message.MessageBody;
import seng302.team18.message.RequestMessage;
import seng302.team18.util.ByteCheck;

/**
 * Created by David-chan on 2/07/17.
 */
public class RequestEncoder extends MessageEncoder {

    private byte[] body;

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
        return new byte[0];
    }

    @Override
    protected short messageLength() {
        return 4;
    }

}
