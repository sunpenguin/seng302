package seng302.team18.encode;

import seng302.team18.message.BoatActionMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.util.ByteCheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Encodes RequestMessages to byte arrays.
 */
public class BoatActionEncoder extends MessageEncoder {

    /**
     * Generates the body of the BoatActionMessage to be delivered
     *
     * @param message to create the body of the message from.
     * @return The body of the message
     */
    @Override
    protected byte[] generateBody(MessageBody message) throws IOException {
        ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();

        if (message instanceof BoatActionMessage) {
            BoatActionMessage boatAction = (BoatActionMessage) message;
            byte[] id = ByteCheck.intToByteArray(boatAction.getId());
            byte action = boatAction.getAction();
            outputSteam.write(id);
            outputSteam.write(action);
        }

        return outputSteam.toByteArray();
    }


    /**
     * Returns the length of the message.
     *
     * @return The length of the message.
     */
    @Override
    protected short messageLength() {
        return 5;
    }


}
