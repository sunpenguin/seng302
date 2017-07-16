package seng302.team18.visualiser.send;

import seng302.team18.message.BoatActionMessage;
import seng302.team18.message.MessageBody;
import java.util.Arrays;
import java.util.List;

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
            List<Boolean> boatActions = Arrays.asList(boatAction.isAutopilot(), boatAction.isSailsIn(),
                    boatAction.isSailsOut(), boatAction.isTackGybe(), boatAction.isUpwind(), boatAction.isDownwind());
            for (int i = 0; i < boatActions.size(); i++) {
                if (boatActions.get(i)) {
                    body[0] |= (1 << i);
                }
            }

        }

        return body;
    }

    /**
     * Generates the checksum of the message to be delivered
     * @param head of message to create checksum for
     * @param body of message to create checksum for
     * @return The checksum of the message
     */
    protected byte[] generateChecksum(byte[] head, byte[] body) {
        return new byte[0];
    }


    /**
     * returns the length of the message
     * @return The length of the message
     */
    @Override
    protected short messageLength() {
        return 0;
    }


}
