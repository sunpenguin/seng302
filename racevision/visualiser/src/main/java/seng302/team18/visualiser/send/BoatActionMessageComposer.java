package seng302.team18.visualiser.send;

import seng302.team18.message.MessageBody;
import java.util.Arrays;
import java.util.List;

/**
 * Created by csl62 on 2/07/17.
 */
public class BoatActionMessageComposer extends MessageComposer{

    protected byte[] generateHead(MessageBody message) {
        return new byte[0];
    }

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

    @Override
    protected byte[] generateChecksum(MessageBody message) {
        return new byte[0];
    }

    @Override
    protected short messageLength() {
        return 0;
    }


}
