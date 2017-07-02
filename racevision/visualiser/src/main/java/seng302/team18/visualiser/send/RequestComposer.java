package seng302.team18.visualiser.send;

import seng302.team18.message.MessageBody;
import seng302.team18.message.RequestMessage;
import seng302.team18.util.ByteCheck;

/**
 * Created by dhl25 on 2/07/17.
 */
public class RequestComposer extends MessageComposer {

    @Override
    protected byte[] generateHead(MessageBody message) {
        return new byte[0];
    }

    @Override
    protected byte[] generateBody(MessageBody message) {
        if (message instanceof RequestMessage) {
            RequestMessage requestMessage = (RequestMessage) message;
            byte[] body = ByteCheck.intToByteArray(requestMessage.getRequestType());
            return body;
        }
        return null;
    }

    @Override
    protected byte[] generateChecksum(MessageBody message) {
        return new byte[0];
    }
}
