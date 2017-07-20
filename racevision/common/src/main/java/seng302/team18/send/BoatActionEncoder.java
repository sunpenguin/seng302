package seng302.team18.send;

import seng302.team18.message.BoatActionMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.send.MessageEncoder;

/**
 * Encodes RequestMessages to byte arrays.
 */
public class BoatActionEncoder extends MessageEncoder {

    /**
     * Generates the body of the BoatActionMessage to be delivered
     * @param message to create the body of the message from.
     * @return The body of the message
     */
    @Override
    protected byte[] generateBody(MessageBody message) {
        byte[] body = null;
        if (message instanceof BoatActionMessage) {
            BoatActionMessage boatAction = (BoatActionMessage) message;
            body = new byte[1];
            body[0] = 0;
            if (boatAction.isAutopilot()) {
                body[0] = 1;
            } else if (boatAction.isTackGybe()) {
                body[0] = 4;
            } else if (boatAction.isUpwind()) {
                body[0] = 5;
            } else if (boatAction.isDownwind()) {
                body[0] = 6;
            } else if (boatAction.isSailsIn()) {
                body[0] = 2;
            } else if (!boatAction.isSailsIn()) {
                body[0] = 3;
            }
        }

        return body;
    }


    /**
     * returns the length of the message
     * @return The length of the message
     */
    @Override
    protected short messageLength() {
        return 1;
    }


}
