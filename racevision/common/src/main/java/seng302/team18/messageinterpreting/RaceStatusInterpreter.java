package seng302.team18.messageinterpreting;

import seng302.team18.data.AC35RaceStatusMessage;
import seng302.team18.data.MessageBody;
import seng302.team18.visualiser.controller.ControllerManager;

/**
 * Created by spe76 on 27/04/17.
 */
public class RaceStatusInterpreter extends MessageInterpreter {

    private ControllerManager controllerManager;

    @Override
    public void interpret(MessageBody message) {
        final int WARNING = 1;
        final int PRE_START = 10;
        if (message instanceof AC35RaceStatusMessage) {
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
            int status = statusMessage.getRaceStatus();
            if (status == WARNING || status == PRE_START) {
//                controllerManager.
            }
        }
    }
}
