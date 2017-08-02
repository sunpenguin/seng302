package seng302.team18.visualiser.send;

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
            byte[] action = generateAction(boatAction);
            outputSteam.write(id);
            outputSteam.write(action);
        }

        return outputSteam.toByteArray();
    }


    /**
     * Sets the byte to the appropriate number according to the BoatActionMessage.
     *
     * @param boatAction message to be used.
     * @return encoded byte of the action.
     */
    private byte[] generateAction(BoatActionMessage boatAction) {
        byte[] action = new byte[1];
        action[0] = boatAction.getAction();

//        if (boatAction.isAutopilot()) {
//            action[0] = BoatActionStatus.AUTOPILOT.action();
//        } else if (boatAction.isTackGybe()) {
//            action[0] = BoatActionStatus.TACK_GYBE.action();
//        } else if (boatAction.isUpwind()) {
//            action[0] = BoatActionStatus.UPWIND.action();
//        } else if (boatAction.isDownwind()) {
//            action[0] = BoatActionStatus.DOWNWIND.action();
//        } else if (boatAction.isSailsIn()) {
//            action[0] = BoatActionStatus.SAIL_IN.action();
//        } else if (!boatAction.isSailsIn()) {
//            action[0] = BoatActionStatus.SAIL_OUT.action();
//        }

        return action;
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
